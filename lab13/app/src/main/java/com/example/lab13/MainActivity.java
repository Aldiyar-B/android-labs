package com.example.lab13;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    private File currentDir;
    private TextView tvCurrentPath;
    private ListView listViewFiles;
    private Button btnBack;

    private ArrayList<File> fileItems;   // элементы текущей папки
    private FileAdapter adapter;         // наш кастомный адаптер с иконками

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCurrentPath = findViewById(R.id.tvCurrentPath);
        listViewFiles = findViewById(R.id.listViewFiles);
        btnBack = findViewById(R.id.btnBack);

        fileItems = new ArrayList<>();
        adapter = new FileAdapter(this, fileItems);
        listViewFiles.setAdapter(adapter);

        // стартовая папка – внешнее хранилище приложения
        currentDir = getExternalFilesDir(null);

        btnBack.setOnClickListener(v -> goUp());
        listViewFiles.setOnItemClickListener(
                (parent, view, position, id) -> onFileClick(position));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkStoragePermission()) {
            showFiles(currentDir);
        } else {
            requestStoragePermission();
        }
    }

    // Проверка доступа к файлам (для Android 11+)
    private boolean checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        }
        return true; // Для старых версий упрощено
    }

    // Запрос специального разрешения у пользователя
    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(
                        Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(
                        String.format("package:%s", getApplicationContext().getPackageName())));
                startActivity(intent);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
            }
        }
    }

    // Отрисовка списка файлов текущей папки
    private void showFiles(File dir) {
        if (dir == null) return;
        tvCurrentPath.setText("Путь: " + dir.getAbsolutePath());
        fileItems.clear();

        File[] filesInDir = dir.listFiles();
        if (filesInDir != null) {
            // Сортировка: сначала папки, затем файлы; внутри – по имени
            Arrays.sort(filesInDir, new Comparator<File>() {
                @Override
                public int compare(File f1, File f2) {
                    if (f1.isDirectory() && !f2.isDirectory()) return -1;
                    if (!f1.isDirectory() && f2.isDirectory()) return 1;
                    return f1.getName().compareToIgnoreCase(f2.getName());
                }
            });
            fileItems.addAll(Arrays.asList(filesInDir));
        } else {
            Toast.makeText(this, "Нет доступа или папка пуста",
                    Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
    }

    // Обработка нажатия на элемент списка
    private void onFileClick(int position) {
        if (position < 0 || position >= fileItems.size()) return;
        File selectedFile = fileItems.get(position);

        if (selectedFile.isDirectory()) {
            currentDir = selectedFile;
            showFiles(currentDir);
        } else {
            // Если это файл – пытаемся его открыть
            openFile(selectedFile);
        }
    }

    // Кнопка "Наверх"
    private void goUp() {
        if (currentDir == null) return;
        File parentDir = currentDir.getParentFile();

        if (parentDir != null && currentDir.getPath().equals(
                Environment.getExternalStorageDirectory().getPath())) {
            Toast.makeText(this, "Это корневой каталог",
                    Toast.LENGTH_SHORT).show();
        } else if (parentDir != null) {
            currentDir = parentDir;
            showFiles(currentDir);
        }
    }

    // Метод генерации безопасной ссылки URI и отправки Интента
    private void openFile(File file) {
        // 1. Получаем MIME-тип файла на основе расширения
        String mimeType = getMimeType(file.getAbsolutePath());
        if (mimeType == null) {
            mimeType = "*/*"; // Если тип неизвестен – выбор из всех приложений
        }
        // 2. Создаём безопасный URI через наш FileProvider
        String authority = getPackageName() + ".fileprovider";
        Uri fileUri = FileProvider.getUriForFile(this, authority, file);

        // 3. Создаём интент для просмотра файла
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, mimeType);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            startActivity(Intent.createChooser(intent, "Открыть файл с помощью:"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Нет приложений для открытия этого файла",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // Вспомогательная функция определения типа файла (.txt, .jpg, .pdf и т.д.)
    private String getMimeType(String url) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension == null || extension.isEmpty()) {
            int i = url.lastIndexOf('.');
            if (i > 0) {
                extension = url.substring(i + 1).toLowerCase();
            }
        }
        if (extension != null) {
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return null;
    }
}
