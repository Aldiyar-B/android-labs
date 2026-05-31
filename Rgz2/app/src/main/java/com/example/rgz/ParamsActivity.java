package com.example.rgz;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

// Подбор. ЛР3 (цветной выбор этапа), ЛР5 (Toast), ЛР6 (FAB+Snackbar),
// ЛР7 (меню профилей), ЛР1-2 (счётчик "топ N").
public class ParamsActivity extends AppCompatActivity {

    private String stage = null;
    private String profile = Scorer.PROFILE_NOVICE;
    private int topN = 3;
    private Button[] stageButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_params);

        Toolbar toolbar = findViewById(R.id.paramsToolbar);
        setSupportActionBar(toolbar);
        updateProfileTitle();

        stageButtons = new Button[]{
                findViewById(R.id.stageFront),
                findViewById(R.id.stageBack),
                findViewById(R.id.stageTest),
                findViewById(R.id.stageRefactor)
        };
        String[] stageNames = {"Вёрстка", "Бэкенд", "Тестирование", "Рефакторинг"};
        for (int i = 0; i < stageButtons.length; i++) {
            final String name = stageNames[i];
            stageButtons[i].setOnClickListener(v -> selectStage(name));
        }

        TextView topLabel = findViewById(R.id.topLabel);
        topLabel.setText("Показать топ: " + topN);
        findViewById(R.id.btnTopPlus).setOnClickListener(v -> {
            if (topN < 8) topN++;
            topLabel.setText("Показать топ: " + topN);
        });
        findViewById(R.id.btnTopMinus).setOnClickListener(v -> {
            if (topN > 1) topN--;
            topLabel.setText("Показать топ: " + topN);
        });

        TextView budgetLabel = findViewById(R.id.budgetLabel);
        SeekBar budget = findViewById(R.id.budgetSeek);
        budgetLabel.setText("Бюджет: до " + budget.getProgress() + " $/мес");
        budget.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar sb, int p, boolean u) {
                budgetLabel.setText("Бюджет: до " + p + " $/мес");
            }
            public void onStartTrackingTouch(SeekBar sb) {}
            public void onStopTrackingTouch(SeekBar sb) {}
        });

        FloatingActionButton fab = findViewById(R.id.fabSelect);
        fab.setOnClickListener(view -> {
            if (stage == null) {
                Toast.makeText(this, "Выберите этап разработки", Toast.LENGTH_SHORT).show();
                return;
            }
            Switch privacy = findViewById(R.id.switchPrivacy);
            Switch ide = findViewById(R.id.switchIde);

            Snackbar.make(view, "Подбираем инструменты...", Snackbar.LENGTH_SHORT).show();

            Intent intent = new Intent(this, ResultsActivity.class);
            intent.putExtra("profile", profile);
            intent.putExtra("budget", budget.getProgress());
            intent.putExtra("privacy", privacy.isChecked());
            intent.putExtra("ide", ide.isChecked());
            intent.putExtra("topN", topN);
            intent.putExtra("stage", stage);
            startActivity(intent);
        });
    }

    private void selectStage(String name) {
        stage = name;
        for (Button b : stageButtons) {
            if (b.getText().toString().equals(name)) {
                b.setBackgroundColor(Color.parseColor("#1976D2"));
                b.setTextColor(Color.WHITE);
            } else {
                b.setBackgroundColor(Color.parseColor("#E0E0E0"));
                b.setTextColor(Color.BLACK);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profiles, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.profile_novice) { profile = Scorer.PROFILE_NOVICE; updateProfileTitle(); return true; }
        if (id == R.id.profile_team)   { profile = Scorer.PROFILE_TEAM; updateProfileTitle(); return true; }
        if (id == R.id.profile_expert) { profile = Scorer.PROFILE_EXPERT; updateProfileTitle(); return true; }
        return super.onOptionsItemSelected(item);
    }

    private void updateProfileTitle() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Профиль: " + Scorer.profileTitle(profile));
        }
    }
}
