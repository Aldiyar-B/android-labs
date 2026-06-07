package com.example.lab13;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_READ_STORAGE = 10;

    private File currentDir;
    private File demoDir;
    private File[] filesInCurrentDir = new File[0];

    private TextView tvTitle;
    private TextView tvPath;
    private TextView tvCount;
    private EditText etSearch;
    private Button btnClear;
    private Button btnDemo;
    private Button btnStorage;
    private Button btnBack;
    private ListView listViewFiles;

    private final ArrayList<FileItem> items = new ArrayList<>();
    private FileAdapter adapter;
    private String query = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();
        adapter = new FileAdapter(this, items);
        listViewFiles.setAdapter(adapter);

        File appStorage = getExternalFilesDir(null);
        demoDir = new File(appStorage != null ? appStorage : getFilesDir(), "demo-files");
        prepareDemoFiles();
        currentDir = demoDir;

        btnDemo.setOnClickListener(v -> openDirectory(demoDir));
        btnStorage.setOnClickListener(v -> openDeviceStorage());
        btnBack.setOnClickListener(v -> goUp());
        btnClear.setOnClickListener(v -> etSearch.setText(""));
        listViewFiles.setOnItemClickListener((parent, view, position, id) -> {
            if (position >= 0 && position < items.size()) {
                openItem(items.get(position).getFile());
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                query = s.toString().trim();
                btnClear.setEnabled(!query.isEmpty());
                showFiles();
            }
        });

        openDirectory(demoDir);
    }

    private void bindViews() {
        tvTitle = findViewById(R.id.tvTitle);
        tvPath = findViewById(R.id.tvPath);
        tvCount = findViewById(R.id.tvCount);
        etSearch = findViewById(R.id.etSearch);
        btnClear = findViewById(R.id.btnClear);
        btnDemo = findViewById(R.id.btnDemo);
        btnStorage = findViewById(R.id.btnStorage);
        btnBack = findViewById(R.id.btnBack);
        listViewFiles = findViewById(R.id.listViewFiles);
    }

    private void prepareDemoFiles() {
        if (demoDir == null) {
            return;
        }
        if (!demoDir.exists() && !demoDir.mkdirs()) {
            Toast.makeText(this, "Не удалось создать демонстрационную папку", Toast.LENGTH_SHORT).show();
            return;
        }

        writeFile("notes.txt", "Лабораторная работа 13\nФайл создан приложением.\n");
        writeFile("page.html", "<html><body><h1>ЛР 13</h1><p>Открытие через FileProvider.</p></body></html>");
        writeFile("data.json", "{ \"lab\": 13, \"action\": \"open file\" }\n");
        writeFile("table.csv", "name,type\nnotes.txt,text\npage.html,html\n");

        // Короткое чтение созданного файла: так в проекте есть и запись, и чтение через java.io.
        readFile(new File(demoDir, "notes.txt"));
    }

    private void writeFile(String fileName, String text) {
        File file = new File(demoDir, fileName);
        if (file.exists()) {
            return;
        }
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(text.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            Toast.makeText(this, "Ошибка записи файла " + fileName, Toast.LENGTH_SHORT).show();
        }
    }

    private String readFile(File file) {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[(int) file.length()];
            int read = inputStream.read(buffer);
            return read > 0 ? new String(buffer, 0, read, StandardCharsets.UTF_8) : "";
        } catch (IOException e) {
            return "";
        }
    }

    private void openDeviceStorage() {
        if (hasStoragePermission()) {
            openDirectory(Environment.getExternalStorageDirectory());
        } else {
            requestStoragePermission();
        }
    }

    private boolean hasStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        }
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            } catch (Exception e) {
                startActivity(new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION));
            }
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_READ_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_STORAGE
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openDirectory(Environment.getExternalStorageDirectory());
        }
    }

    private void openDirectory(File directory) {
        if (directory == null || !directory.exists()) {
            Toast.makeText(this, "Папка недоступна", Toast.LENGTH_SHORT).show();
            return;
        }
        currentDir = directory;
        filesInCurrentDir = directory.listFiles();
        if (filesInCurrentDir == null) {
            filesInCurrentDir = new File[0];
            Toast.makeText(this, "Нет доступа к содержимому", Toast.LENGTH_SHORT).show();
        }
        if (directory.equals(demoDir)) {
            tvTitle.setText("Демо-файлы");
        } else if (directory.equals(Environment.getExternalStorageDirectory())) {
            tvTitle.setText("Память устройства");
        } else {
            tvTitle.setText(directory.getName());
        }
        tvPath.setText(directory.getAbsolutePath());
        showFiles();
    }

    private void showFiles() {
        items.clear();
        String normalizedQuery = query.toLowerCase(Locale.ROOT);

        List<File> filtered = new ArrayList<>();
        for (File file : filesInCurrentDir) {
            if (normalizedQuery.isEmpty()
                    || file.getName().toLowerCase(Locale.ROOT).contains(normalizedQuery)) {
                filtered.add(file);
            }
        }

        filtered.sort((first, second) -> {
            if (first.isDirectory() != second.isDirectory()) {
                return first.isDirectory() ? -1 : 1;
            }
            return first.getName().compareToIgnoreCase(second.getName());
        });

        int folders = 0;
        for (File file : filtered) {
            if (file.isDirectory()) {
                folders++;
            }
            items.add(new FileItem(file));
        }
        adapter.notifyDataSetChanged();
        tvCount.setText("Папок: " + folders + "  Файлов: " + (filtered.size() - folders));
    }

    private void openItem(File file) {
        if (file.isDirectory()) {
            openDirectory(file);
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Открыть файл")
                .setMessage(file.getName() + "\nФайл будет передан внешней программе через FileProvider.")
                .setNegativeButton("Отмена", null)
                .setPositiveButton("Выбрать программу", (dialog, which) -> openFile(file))
                .show();
    }

    private void openFile(File file) {
        String mimeType = getMimeType(file);
        if (mimeType == null) {
            mimeType = "*/*";
        }

        Uri uri = FileProvider.getUriForFile(
                this,
                getPackageName() + ".fileprovider",
                file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, mimeType);
        intent.setClipData(ClipData.newRawUri(file.getName(), uri));
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(Intent.createChooser(intent, "Открыть с помощью"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Нет приложения для открытия файла", Toast.LENGTH_SHORT).show();
        }
    }

    private String getMimeType(File file) {
        String name = file.getName();
        int dot = name.lastIndexOf('.');
        if (dot < 0 || dot == name.length() - 1) {
            return null;
        }
        String extension = name.substring(dot + 1).toLowerCase(Locale.ROOT);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    private void goUp() {
        if (currentDir == null) {
            return;
        }
        File root = Environment.getExternalStorageDirectory();
        if (currentDir.equals(demoDir) || currentDir.equals(root)) {
            Toast.makeText(this, "Это корневой каталог", Toast.LENGTH_SHORT).show();
            return;
        }
        File parent = currentDir.getParentFile();
        if (parent != null) {
            openDirectory(parent);
        }
    }
}
