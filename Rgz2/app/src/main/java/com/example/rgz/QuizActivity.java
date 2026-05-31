package com.example.rgz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

// ЛР10: викторина по вендору (данные из SQLite).
public class QuizActivity extends AppCompatActivity {

    private DBHelper.Quiz quiz;
    private Button[] buttons;
    private String placeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        placeId = getIntent().getStringExtra(MapsActivity.EXTRA_PLACE_ID);
        if (placeId == null) { finish(); return; }

        quiz = new DBHelper(this).getQuizByPlace(placeId);
        if (quiz == null || quiz.answers == null || quiz.answers.size() < 4) { finish(); return; }

        ((TextView) findViewById(R.id.place_title)).setText(quiz.placeName);
        ((TextView) findViewById(R.id.question_text)).setText(quiz.questionText);

        buttons = new Button[]{
                findViewById(R.id.btn_a),
                findViewById(R.id.btn_b),
                findViewById(R.id.btn_c),
                findViewById(R.id.btn_d)
        };
        for (int i = 0; i < buttons.length; i++) {
            final int index = i;
            buttons[i].setText(quiz.answers.get(i).text);
            buttons[i].setOnClickListener(v -> handleAnswer(index));
        }
    }

    private void handleAnswer(int selectedIndex) {
        boolean correct = quiz.answers.get(selectedIndex).id == quiz.correctAnswerId;

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setEnabled(false);
            boolean isCorrect = quiz.answers.get(i).id == quiz.correctAnswerId;
            buttons[i].setBackgroundTintList(ColorStateList.valueOf(
                    isCorrect ? Color.parseColor("#2E7D32") : Color.parseColor("#D32F2F")));
            buttons[i].setTextColor(Color.WHITE);
        }

        if (correct) {
            SharedPreferences prefs = getSharedPreferences(MapsActivity.PREFS_NAME, MODE_PRIVATE);
            prefs.edit().putBoolean(placeId, true).apply();
        }

        new AlertDialog.Builder(this)
                .setTitle(correct ? "Верно" : "Неверно")
                .setMessage(correct ? quiz.correctComment
                        : "Ответ неправильный. Можно открыть справку и попробовать снова.")
                .setNegativeButton("Подробнее", (d, w) -> openInfo())
                .setPositiveButton("К карте", (d, w) -> finish())
                .show();
    }

    private void openInfo() {
        Intent intent = new Intent(this, BrowserActivity.class);
        intent.putExtra(BrowserActivity.EXTRA_TITLE, quiz.placeName);
        intent.putExtra(BrowserActivity.EXTRA_URL, quiz.infoUrl);
        intent.putExtra(BrowserActivity.EXTRA_FALLBACK_TEXT, quiz.correctComment);
        startActivity(intent);
    }
}
