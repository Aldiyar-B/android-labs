package com.example.labs10;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.HashMap;
import java.util.Map;

public class QuizActivity extends AppCompatActivity {
    private static final Map<String, QuizQuestion> QUESTIONS = createQuestions();

    private QuizQuestion question;
    private Button[] answerButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        String placeId = getIntent().getStringExtra(MainActivity.EXTRA_PLACE_ID);
        question = QUESTIONS.get(placeId);
        if (question == null) {
            finish();
            return;
        }

        answerButtons = new Button[]{
                findViewById(R.id.btn_a),
                findViewById(R.id.btn_b),
                findViewById(R.id.btn_c),
                findViewById(R.id.btn_d)
        };

        bindQuestion();
    }

    public void answerA(View view) {
        handleAnswer(0);
    }

    public void answerB(View view) {
        handleAnswer(1);
    }

    public void answerC(View view) {
        handleAnswer(2);
    }

    public void answerD(View view) {
        handleAnswer(3);
    }

    private void bindQuestion() {
        TextView placeTitle = findViewById(R.id.place_title);
        TextView questionText = findViewById(R.id.question_text);

        placeTitle.setText(question.title);
        questionText.setText(question.text);
        for (int i = 0; i < answerButtons.length; i++) {
            answerButtons[i].setText(question.answers[i]);
        }
    }

    private void handleAnswer(int selectedIndex) {
        boolean correct = selectedIndex == question.correctIndex;
        for (int i = 0; i < answerButtons.length; i++) {
            answerButtons[i].setEnabled(false);
            int color = i == question.correctIndex ? R.color.answer_correct : R.color.answer_wrong;
            answerButtons[i].setBackgroundTintList(ContextCompat.getColorStateList(this, color));
            answerButtons[i].setTextColor(ContextCompat.getColor(this, R.color.white));
        }

        if (correct) {
            SharedPreferences prefs = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
            prefs.edit().putBoolean(question.id, true).apply();
        }

        new AlertDialog.Builder(this)
                .setTitle(correct ? "Верно" : "Неверно")
                .setMessage(correct ? question.correctComment : question.wrongComment)
                .setNegativeButton("Подробнее", (dialog, which) -> openInfoPage())
                .setPositiveButton("К карте", (dialog, which) -> finish())
                .show();
    }

    private void openInfoPage() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(WebViewActivity.EXTRA_TITLE, question.title);
        intent.putExtra(WebViewActivity.EXTRA_URL, question.infoUrl);
        intent.putExtra(WebViewActivity.EXTRA_FALLBACK_TEXT, question.correctComment);
        startActivity(intent);
    }

    private static Map<String, QuizQuestion> createQuestions() {
        Map<String, QuizQuestion> questions = new HashMap<>();
        questions.put("moscow", new QuizQuestion(
                "moscow",
                "Москва",
                "Какая достопримечательность находится у маркера на Красной площади?",
                new String[]{"Эйфелева башня", "Собор Василия Блаженного", "Статуя Христа", "Оперный театр"},
                1,
                "Правильно: рядом с Красной площадью находится Собор Василия Блаженного.",
                "Подумайте о самом узнаваемом храме рядом с Красной площадью.",
                "https://ru.wikipedia.org/wiki/Собор_Василия_Блаженного"
        ));
        questions.put("paris", new QuizQuestion(
                "paris",
                "Париж",
                "Какая река протекает через центр Парижа?",
                new String[]{"Сена", "Темза", "Нева", "Москва-река"},
                0,
                "Верно: исторический центр Парижа расположен на берегах Сены.",
                "Подсказка: эта река делит Париж на правый и левый берег.",
                "https://ru.wikipedia.org/wiki/Сена"
        ));
        questions.put("rio", new QuizQuestion(
                "rio",
                "Рио-де-Жанейро",
                "Какая знаменитая статуя является символом Рио-де-Жанейро?",
                new String[]{"Колосс Родосский", "Родина-мать", "Христос-Искупитель", "Статуя Свободы"},
                2,
                "Точно: статуя Христа-Искупителя стоит на горе Корковаду.",
                "Подсказка: статуя расположена на горе Корковаду.",
                "https://ru.wikipedia.org/wiki/Статуя_Христа-Искупителя"
        ));
        questions.put("sydney", new QuizQuestion(
                "sydney",
                "Сидней",
                "Как называется известное здание с парусообразной крышей в Сиднее?",
                new String[]{"Лувр", "Сиднейский оперный театр", "Биг-Бен", "Колизей"},
                1,
                "Да: Сиднейский оперный театр стал одним из символов Австралии.",
                "Подсказка: это концертный комплекс на берегу гавани.",
                "https://ru.wikipedia.org/wiki/Сиднейский_оперный_театр"
        ));
        return questions;
    }

    private static class QuizQuestion {
        final String id;
        final String title;
        final String text;
        final String[] answers;
        final int correctIndex;
        final String correctComment;
        final String wrongComment;
        final String infoUrl;

        QuizQuestion(
                String id,
                String title,
                String text,
                String[] answers,
                int correctIndex,
                String correctComment,
                String wrongComment,
                String infoUrl
        ) {
            this.id = id;
            this.title = title;
            this.text = text;
            this.answers = answers;
            this.correctIndex = correctIndex;
            this.correctComment = correctComment;
            this.wrongComment = wrongComment;
            this.infoUrl = infoUrl;
        }
    }
}
