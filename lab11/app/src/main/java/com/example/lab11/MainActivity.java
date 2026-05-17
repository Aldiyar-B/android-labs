package com.example.lab11;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private SQLiteDatabase database;

    private Button btnStudents, btnSubjects, btnGrades, btnQuiz;
    private ListView listDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStudents = findViewById(R.id.btnStudents);
        btnSubjects = findViewById(R.id.btnSubjects);
        btnGrades = findViewById(R.id.btnGrades);
        btnQuiz = findViewById(R.id.btnQuiz);

        listDB = findViewById(R.id.listDB);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        btnStudents.setOnClickListener(v -> showStudents());
        btnSubjects.setOnClickListener(v -> showSubjects());
        btnGrades.setOnClickListener(v -> showGrades());

        btnQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intent);
        });
    }

    private void showStudents() {
        ArrayList<HashMap<String, String>> data = new ArrayList<>();

        Cursor cursor = database.rawQuery(
                "SELECT name, group_name FROM students",
                null
        );

        while (cursor.moveToNext()) {
            HashMap<String, String> item = new HashMap<>();

            item.put("title", cursor.getString(0));
            item.put("description", "Группа: " + cursor.getString(1));

            data.add(item);
        }

        cursor.close();
        setAdapter(data);
    }

    private void showSubjects() {
        ArrayList<HashMap<String, String>> data = new ArrayList<>();

        Cursor cursor = database.rawQuery(
                "SELECT subject_name, teacher FROM subjects",
                null
        );

        while (cursor.moveToNext()) {
            HashMap<String, String> item = new HashMap<>();

            item.put("title", cursor.getString(0));
            item.put("description", "Преподаватель: " + cursor.getString(1));

            data.add(item);
        }

        cursor.close();
        setAdapter(data);
    }

    private void showGrades() {
        ArrayList<HashMap<String, String>> data = new ArrayList<>();

        Cursor cursor = database.rawQuery(
                "SELECT student_name, subject_name, grade FROM grades",
                null
        );

        while (cursor.moveToNext()) {
            HashMap<String, String> item = new HashMap<>();

            item.put("title", cursor.getString(0));
            item.put(
                    "description",
                    cursor.getString(1) + " — оценка: " + cursor.getInt(2)
            );

            data.add(item);
        }

        cursor.close();
        setAdapter(data);
    }

    private void setAdapter(ArrayList<HashMap<String, String>> data) {
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                data,
                R.layout.listview_item,
                new String[]{"title", "description"},
                new int[]{R.id.textTitle, R.id.textDescription}
        );

        listDB.setAdapter(adapter);
    }
}