package com.example.lab11;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "student_db.db";

    // Ставим 2, чтобы база пересоздалась после добавления новых таблиц
    private static final int DB_VERSION = 2;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Таблица студентов
        db.execSQL("CREATE TABLE students (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "group_name TEXT NOT NULL)");

        // Таблица дисциплин
        db.execSQL("CREATE TABLE subjects (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "subject_name TEXT NOT NULL, " +
                "teacher TEXT NOT NULL)");

        // Таблица оценок
        db.execSQL("CREATE TABLE grades (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_name TEXT NOT NULL, " +
                "subject_name TEXT NOT NULL, " +
                "grade INTEGER NOT NULL)");

        // Таблица вопросов викторины
        db.execSQL("CREATE TABLE questions (" +
                "_id INTEGER PRIMARY KEY, " +
                "place_id TEXT NOT NULL, " +
                "place_name TEXT NOT NULL, " +
                "question_text TEXT NOT NULL, " +
                "correct_answer_id INTEGER NOT NULL, " +
                "correct_comment TEXT NOT NULL, " +
                "info_url TEXT NOT NULL)");

        // Таблица ответов викторины
        db.execSQL("CREATE TABLE answers (" +
                "_id INTEGER PRIMARY KEY, " +
                "question_id INTEGER NOT NULL, " +
                "answer_text TEXT NOT NULL)");

        insertStudentsData(db);
        insertQuizData(db);
    }

    private void insertStudentsData(SQLiteDatabase db) {
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

    private void insertQuizData(SQLiteDatabase db) {

        // Вопрос 1 — Москва
        db.execSQL("INSERT INTO questions VALUES (" +
                "1, " +
                "'moscow', " +
                "'Москва', " +
                "'Какая достопримечательность расположена рядом с Красной площадью?', " +
                "102, " +
                "'Московский Кремль расположен рядом с Красной площадью и является одной из главных достопримечательностей Москвы.', " +
                "'https://en.wikipedia.org/wiki/Moscow_Kremlin')");

        db.execSQL("INSERT INTO answers VALUES " +
                "(101, 1, 'Эйфелева башня')," +
                "(102, 1, 'Московский Кремль')," +
                "(103, 1, 'Колизей')," +
                "(104, 1, 'Статуя Свободы')");

        // Вопрос 2 — Париж
        db.execSQL("INSERT INTO questions VALUES (" +
                "2, " +
                "'paris', " +
                "'Париж', " +
                "'Какая достопримечательность является одним из главных символов Парижа?', " +
                "202, " +
                "'Эйфелева башня находится в Париже и является одной из самых известных достопримечательностей Франции.', " +
                "'https://en.wikipedia.org/wiki/Eiffel_Tower')");

        db.execSQL("INSERT INTO answers VALUES " +
                "(201, 2, 'Колизей')," +
                "(202, 2, 'Эйфелева башня')," +
                "(203, 2, 'Биг-Бен')," +
                "(204, 2, 'Сиднейский оперный театр')");

        // Вопрос 3 — Рио
        db.execSQL("INSERT INTO questions VALUES (" +
                "3, " +
                "'rio', " +
                "'Рио-де-Жанейро', " +
                "'Какая статуя является одним из символов Рио-де-Жанейро?', " +
                "302, " +
                "'Статуя Христа-Искупителя расположена на горе Корковаду и считается символом Рио-де-Жанейро.', " +
                "'https://en.wikipedia.org/wiki/Christ_the_Redeemer_(statue)')");

        db.execSQL("INSERT INTO answers VALUES " +
                "(301, 3, 'Статуя Свободы')," +
                "(302, 3, 'Христос-Искупитель')," +
                "(303, 3, 'Медный всадник')," +
                "(304, 3, 'Родина-мать')");

        // Вопрос 4 — Сидней
        db.execSQL("INSERT INTO questions VALUES (" +
                "4, " +
                "'sydney', " +
                "'Сидней', " +
                "'Какое здание считается архитектурным символом Сиднея?', " +
                "401, " +
                "'Сиднейский оперный театр является одним из самых узнаваемых зданий Австралии.', " +
                "'https://en.wikipedia.org/wiki/Sydney_Opera_House')");

        db.execSQL("INSERT INTO answers VALUES " +
                "(401, 4, 'Сиднейский оперный театр')," +
                "(402, 4, 'Лувр')," +
                "(403, 4, 'Тауэрский мост')," +
                "(404, 4, 'Пизанская башня')");
    }

    public Quiz getQuizByPlace(String placeId) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor questionCursor = db.rawQuery(
                "SELECT _id, place_id, place_name, question_text, correct_answer_id, correct_comment, info_url " +
                        "FROM questions WHERE place_id = ?",
                new String[]{placeId}
        );

        if (!questionCursor.moveToFirst()) {
            questionCursor.close();
            return null;
        }

        Quiz quiz = new Quiz();

        quiz.id = questionCursor.getInt(0);
        quiz.placeId = questionCursor.getString(1);
        quiz.placeName = questionCursor.getString(2);
        quiz.questionText = questionCursor.getString(3);
        quiz.correctAnswerId = questionCursor.getInt(4);
        quiz.correctComment = questionCursor.getString(5);
        quiz.infoUrl = questionCursor.getString(6);
        quiz.answers = new ArrayList<>();

        questionCursor.close();

        Cursor answerCursor = db.rawQuery(
                "SELECT _id, answer_text FROM answers WHERE question_id = ? ORDER BY _id",
                new String[]{String.valueOf(quiz.id)}
        );

        while (answerCursor.moveToNext()) {
            Answer answer = new Answer();
            answer.id = answerCursor.getInt(0);
            answer.text = answerCursor.getString(1);
            quiz.answers.add(answer);
        }

        answerCursor.close();

        return quiz;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS students");
        db.execSQL("DROP TABLE IF EXISTS subjects");
        db.execSQL("DROP TABLE IF EXISTS grades");
        db.execSQL("DROP TABLE IF EXISTS answers");
        db.execSQL("DROP TABLE IF EXISTS questions");
        onCreate(db);
    }

    public static class Quiz {
        public int id;
        public String placeId;
        public String placeName;
        public String questionText;
        public int correctAnswerId;
        public String correctComment;
        public String infoUrl;
        public ArrayList<Answer> answers;
    }

    public static class Answer {
        public int id;
        public String text;
    }
}