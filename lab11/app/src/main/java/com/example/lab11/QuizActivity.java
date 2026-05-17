package com.example.lab11;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class QuizActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private DBHelper.Quiz quiz;
    private Button[] answerButtons;
    private String placeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        placeId = getIntent().getStringExtra(MapsActivity.EXTRA_PLACE_ID);

        if (placeId == null) {
            finish();
            return;
        }

        dbHelper = new DBHelper(this);
        quiz = dbHelper.getQuizByPlace(placeId);

        if (quiz == null || quiz.answers == null || quiz.answers.size() < 4) {
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

        placeTitle.setText(quiz.placeName);
        questionText.setText(quiz.questionText);

        for (int i = 0; i < answerButtons.length; i++) {
            answerButtons[i].setText(quiz.answers.get(i).text);
        }
    }

    private void handleAnswer(int selectedIndex) {
        int selectedAnswerId = quiz.answers.get(selectedIndex).id;
        boolean correct = selectedAnswerId == quiz.correctAnswerId;

        for (int i = 0; i < answerButtons.length; i++) {
            answerButtons[i].setEnabled(false);

            int currentAnswerId = quiz.answers.get(i).id;

            int color;
            if (currentAnswerId == quiz.correctAnswerId) {
                color = R.color.answer_correct;
            } else {
                color = R.color.answer_wrong;
            }

            answerButtons[i].setBackgroundTintList(
                    ContextCompat.getColorStateList(this, color)
            );
            answerButtons[i].setTextColor(
                    ContextCompat.getColor(this, R.color.white)
            );
        }

        if (correct) {
            SharedPreferences prefs = getSharedPreferences(
                    MapsActivity.PREFS_NAME,
                    MODE_PRIVATE
            );

            prefs.edit().putBoolean(placeId, true).apply();
        }

        new AlertDialog.Builder(this)
                .setTitle(correct ? "Верно" : "Неверно")
                .setMessage(correct
                        ? quiz.correctComment
                        : "Ответ выбран неправильно. Можно открыть справку и попробовать позже.")
                .setNegativeButton("Подробнее", (dialog, which) -> openInfoPage())
                .setPositiveButton("К карте", (dialog, which) -> finish())
                .show();
    }

    private void openInfoPage() {
        Intent intent = new Intent(this, BrowserActivity.class);
        intent.putExtra(BrowserActivity.EXTRA_TITLE, quiz.placeName);
        intent.putExtra(BrowserActivity.EXTRA_URL, quiz.infoUrl);
        intent.putExtra(BrowserActivity.EXTRA_FALLBACK_TEXT, quiz.correctComment);
        startActivity(intent);
    }
}