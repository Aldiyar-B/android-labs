package com.example.lab09;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LinksActivity extends AppCompatActivity {

    private Button btnLink1, btnLink2, btnLink3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_links);

        btnLink1 = findViewById(R.id.btnLink1);
        btnLink2 = findViewById(R.id.btnLink2);
        btnLink3 = findViewById(R.id.btnLink3);

        btnLink1.setOnClickListener(v -> chooseBrowser("https://example.com"));
        btnLink2.setOnClickListener(v -> chooseBrowser("https://www.google.com"));
        btnLink3.setOnClickListener(v -> chooseBrowser("https://nstu.ru"));
    }

    private void chooseBrowser(String url) {
        String[] options = {"Собственный браузер", "Системный браузер"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выберите, чем открыть ссылку");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                openInMyBrowser(url);
            } else {
                openInSystemBrowser(url);
            }
        });
        builder.show();
    }

    private void openInMyBrowser(String url) {
        Intent intent = new Intent(LinksActivity.this, BrowserActivity.class);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private void openInSystemBrowser(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "На устройстве нет браузера", Toast.LENGTH_SHORT).show();
        }
    }
}