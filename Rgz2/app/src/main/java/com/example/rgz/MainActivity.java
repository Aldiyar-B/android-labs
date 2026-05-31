package com.example.rgz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

// Главный экран "AI Dev Advisor". ЛР1-2: текст, проект, счётчик инструментов в базе.
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int count = new DBHelper(this).getAllTools().size();
        ((TextView) findViewById(R.id.toolsCount)).setText("В базе инструментов: " + count);

        findViewById(R.id.btnSelect).setOnClickListener(v ->
                startActivity(new Intent(this, ParamsActivity.class)));
        findViewById(R.id.btnCatalog).setOnClickListener(v ->
                startActivity(new Intent(this, CatalogActivity.class)));
        findViewById(R.id.btnMap).setOnClickListener(v ->
                startActivity(new Intent(this, MapsActivity.class)));
    }
}
