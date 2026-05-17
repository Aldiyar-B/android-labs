package com.example.lab11;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "student_db.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE students (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "group_name TEXT NOT NULL)");

        db.execSQL("CREATE TABLE subjects (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "subject_name TEXT NOT NULL, " +
                "teacher TEXT NOT NULL)");

        db.execSQL("CREATE TABLE grades (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_name TEXT NOT NULL, " +
                "subject_name TEXT NOT NULL, " +
                "grade INTEGER NOT NULL)");

        db.execSQL("INSERT INTO students (name, group_name) VALUES " +
                "('Батаев Алдияр', 'АПМ-25')," +
                "('Иванов Иван', 'АПМ-25')," +
                "('Петров Петр', 'АПМ-24')," +
                "('Сидорова Анна', 'АПМ-23')");

        db.execSQL("INSERT INTO subjects (subject_name, teacher) VALUES " +
                "('Современные технологии разработки ПО', 'Эстрайх И.В.')," +
                "('Системы искусственного интеллекта', 'Бакаев М.А.')," +
                "('Базы данных', 'Иванова Н.П.')");

        db.execSQL("INSERT INTO grades (student_name, subject_name, grade) VALUES " +
                "('Батаев Алдияр', 'Современные технологии разработки ПО', 5)," +
                "('Иванов Иван', 'Базы данных', 4)," +
                "('Петров Петр', 'Системы искусственного интеллекта', 5)," +
                "('Сидорова Анна', 'Базы данных', 3)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS students");
        db.execSQL("DROP TABLE IF EXISTS subjects");
        db.execSQL("DROP TABLE IF EXISTS grades");
        onCreate(db);
    }
}