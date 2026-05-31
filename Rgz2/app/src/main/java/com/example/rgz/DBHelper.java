package com.example.rgz;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

// ЛР11: база данных ИИ-инструментов + данные для викторины по вендорам.
public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "ai_tools.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE tools (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, vendor TEXT, category TEXT, price INTEGER, " +
                "quality INTEGER, privacy INTEGER, ease INTEGER, ide INTEGER, " +
                "site_url TEXT, risks TEXT)");

        db.execSQL("CREATE TABLE questions (" +
                "_id INTEGER PRIMARY KEY, place_id TEXT, place_name TEXT, " +
                "question_text TEXT, correct_answer_id INTEGER, correct_comment TEXT, info_url TEXT)");

        db.execSQL("CREATE TABLE answers (" +
                "_id INTEGER PRIMARY KEY, question_id INTEGER, answer_text TEXT)");

        insertTools(db);
        insertQuiz(db);
    }

    private void insertTools(SQLiteDatabase db) {
        addTool(db, "GitHub Copilot", "GitHub/Microsoft", "Ассистент IDE", 10, 5, 2, 5, 1,
                "https://github.com/features/copilot",
                "Код отправляется в облако; возможны небезопасные подсказки.");
        addTool(db, "Cursor", "Anysphere", "ИИ-редактор кода", 20, 5, 3, 4, 1,
                "https://www.cursor.com",
                "Облачная обработка; платная подписка для полного функционала.");
        addTool(db, "Tabnine", "Tabnine", "Ассистент IDE", 12, 3, 5, 4, 1,
                "https://www.tabnine.com",
                "Качество ниже топовых моделей; зато есть локальный режим.");
        addTool(db, "ChatGPT", "OpenAI", "Диалоговая модель", 20, 4, 2, 5, 0,
                "https://chat.openai.com",
                "Нет интеграции с IDE; данные уходят в облако.");
        addTool(db, "Claude", "Anthropic", "Диалоговая модель", 20, 5, 3, 5, 0,
                "https://claude.ai",
                "Нет прямой интеграции с IDE; облачная обработка.");
        addTool(db, "Gemini Code Assist", "Google", "Ассистент IDE", 19, 4, 3, 4, 1,
                "https://cloud.google.com/products/gemini/code-assist",
                "Привязка к экосистеме Google Cloud.");
        addTool(db, "Amazon Q Developer", "Amazon", "Корпоративный ассистент", 19, 4, 4, 3, 1,
                "https://aws.amazon.com/q/developer/",
                "Сложнее в освоении; ориентирован на экосистему AWS.");
        addTool(db, "Windsurf", "Codeium", "ИИ-редактор кода", 15, 4, 3, 4, 1,
                "https://codeium.com/windsurf",
                "Молодой продукт; функционал быстро меняется.");
    }

    private void addTool(SQLiteDatabase db, String name, String vendor, String category,
                         int price, int quality, int privacy, int ease, int ide,
                         String url, String risks) {
        db.execSQL("INSERT INTO tools (name, vendor, category, price, quality, privacy, ease, ide, site_url, risks) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?)",
                new Object[]{name, vendor, category, price, quality, privacy, ease, ide, url, risks});
    }

    private void insertQuiz(SQLiteDatabase db) {
        db.execSQL("INSERT INTO questions VALUES (1,'openai','OpenAI'," +
                "'Какой инструмент разработан компанией OpenAI?',102," +
                "'ChatGPT — диалоговая модель компании OpenAI.','https://chat.openai.com')");
        db.execSQL("INSERT INTO answers VALUES (101,1,'GitHub Copilot'),(102,1,'ChatGPT'),(103,1,'Tabnine'),(104,1,'Cursor')");

        db.execSQL("INSERT INTO questions VALUES (2,'anthropic','Anthropic'," +
                "'Какую диалоговую модель создала компания Anthropic?',202," +
                "'Claude — семейство моделей компании Anthropic.','https://claude.ai')");
        db.execSQL("INSERT INTO answers VALUES (201,2,'Gemini'),(202,2,'Claude'),(203,2,'ChatGPT'),(204,2,'Llama')");

        db.execSQL("INSERT INTO questions VALUES (3,'github','GitHub'," +
                "'Какой ИИ-ассистент встроен в среды разработки от GitHub?',302," +
                "'GitHub Copilot — ассистент, встроенный в IDE.','https://github.com/features/copilot')");
        db.execSQL("INSERT INTO answers VALUES (301,3,'Windsurf'),(302,3,'GitHub Copilot'),(303,3,'Tabnine'),(304,3,'Claude')");

        db.execSQL("INSERT INTO questions VALUES (4,'jetbrains','JetBrains'," +
                "'Какой ИИ-редактор кода известен как самостоятельная среда со встроенной моделью?',402," +
                "'Cursor — отдельный ИИ-редактор кода со встроенной моделью.','https://www.cursor.com')");
        db.execSQL("INSERT INTO answers VALUES (401,4,'ChatGPT'),(402,4,'Cursor'),(403,4,'Gemini'),(404,4,'Amazon Q')");
    }

    public List<Tool> getAllTools() {
        List<Tool> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT _id,name,vendor,category,price,quality,privacy,ease,ide,site_url,risks FROM tools", null);
        while (c.moveToNext()) {
            Tool t = new Tool();
            t.id = c.getInt(0); t.name = c.getString(1); t.vendor = c.getString(2);
            t.category = c.getString(3); t.price = c.getInt(4); t.quality = c.getInt(5);
            t.privacy = c.getInt(6); t.ease = c.getInt(7); t.ide = c.getInt(8);
            t.siteUrl = c.getString(9); t.risks = c.getString(10);
            list.add(t);
        }
        c.close();
        return list;
    }

    public Tool getToolById(int id) {
        Tool t = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT _id,name,vendor,category,price,quality,privacy,ease,ide,site_url,risks FROM tools WHERE _id=?",
                new String[]{String.valueOf(id)});
        if (c.moveToFirst()) {
            t = new Tool();
            t.id = c.getInt(0); t.name = c.getString(1); t.vendor = c.getString(2);
            t.category = c.getString(3); t.price = c.getInt(4); t.quality = c.getInt(5);
            t.privacy = c.getInt(6); t.ease = c.getInt(7); t.ide = c.getInt(8);
            t.siteUrl = c.getString(9); t.risks = c.getString(10);
        }
        c.close();
        return t;
    }

    public Quiz getQuizByPlace(String placeId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor q = db.rawQuery("SELECT _id,place_id,place_name,question_text,correct_answer_id,correct_comment,info_url " +
                "FROM questions WHERE place_id=?", new String[]{placeId});
        if (!q.moveToFirst()) { q.close(); return null; }

        Quiz quiz = new Quiz();
        quiz.id = q.getInt(0); quiz.placeId = q.getString(1); quiz.placeName = q.getString(2);
        quiz.questionText = q.getString(3); quiz.correctAnswerId = q.getInt(4);
        quiz.correctComment = q.getString(5); quiz.infoUrl = q.getString(6);
        quiz.answers = new ArrayList<>();
        q.close();

        Cursor a = db.rawQuery("SELECT _id,answer_text FROM answers WHERE question_id=? ORDER BY _id",
                new String[]{String.valueOf(quiz.id)});
        while (a.moveToNext()) {
            Answer ans = new Answer();
            ans.id = a.getInt(0); ans.text = a.getString(1);
            quiz.answers.add(ans);
        }
        a.close();
        return quiz;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tools");
        db.execSQL("DROP TABLE IF EXISTS questions");
        db.execSQL("DROP TABLE IF EXISTS answers");
        onCreate(db);
    }

    public static class Quiz {
        public int id; public String placeId; public String placeName;
        public String questionText; public int correctAnswerId;
        public String correctComment; public String infoUrl;
        public ArrayList<Answer> answers;
    }

    public static class Answer {
        public int id; public String text;
    }
}
