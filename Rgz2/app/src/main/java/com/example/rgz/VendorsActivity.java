package com.example.rgz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

// ЛР10: карта вендоров с маркерами и викториной.
public class VendorsActivity extends AppCompatActivity {

    public static final String EXTRA_PLACE_ID = "place_id";
    public static final String PREFS_NAME = "quiz_progress";

    private VendorMapView mapView;
    private TextView progress;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendors);

        mapView = findViewById(R.id.vendorMap);
        progress = findViewById(R.id.vendorProgress);
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        mapView.setListener(placeId -> {
            Intent intent = new Intent(this, QuizActivity.class);
            intent.putExtra(EXTRA_PLACE_ID, placeId);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        String[] ids = {"github", "openai", "anthropic", "jetbrains"};
        int done = 0;
        for (String id : ids) {
            boolean answered = prefs.getBoolean(id, false);
            mapView.setAnswered(id, answered);
            if (answered) done++;
        }
        progress.setText("Угадано вендоров: " + done + " из " + mapView.total() +
                "\nНажмите на маркер, чтобы ответить на вопрос");
    }
}
