package com.example.rgz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

// Результаты подбора: ранжированный список + диаграмма (ЛР8) + сортировка (ЛР7 PopupMenu).
public class ResultsActivity extends AppCompatActivity {

    private List<Tool> ranked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Scorer scorer = new Scorer();
        scorer.profile = getIntent().getStringExtra("profile");
        scorer.maxBudget = getIntent().getIntExtra("budget", 30);
        scorer.requirePrivacy = getIntent().getBooleanExtra("privacy", false);
        scorer.requireIde = getIntent().getBooleanExtra("ide", false);
        int topN = getIntent().getIntExtra("topN", 3);
        String stage = getIntent().getStringExtra("stage");

        ((TextView) findViewById(R.id.resultsHeader)).setText(
                "Этап: " + stage + " · профиль: " + Scorer.profileTitle(scorer.profile));

        ranked = scorer.rank(new DBHelper(this).getAllTools());
        if (ranked.size() > topN) ranked = new ArrayList<>(ranked.subList(0, topN));

        ((ScoreChartView) findViewById(R.id.scoreChart)).setData(ranked);
        showList();

        ((Button) findViewById(R.id.btnSort)).setOnClickListener(this::showSortMenu);
    }

    private void showList() {
        ListView list = findViewById(R.id.resultsList);
        ArrayList<HashMap<String, String>> data = new ArrayList<>();
        for (Tool t : ranked) {
            HashMap<String, String> item = new HashMap<>();
            item.put("title", t.name + " — балл " + t.score);
            item.put("description", t.vendor + " · " + t.category + " · " + t.price + " $/мес");
            data.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.listview_item,
                new String[]{"title", "description"},
                new int[]{R.id.textTitle, R.id.textDescription});
        list.setAdapter(adapter);

        list.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, ToolDetailActivity.class);
            intent.putExtra(ToolDetailActivity.EXTRA_TOOL_ID, ranked.get(position).id);
            startActivity(intent);
        });
    }

    private void showSortMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.inflate(R.menu.menu_sort);
        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.sort_score) {
                Collections.sort(ranked, (a, b) -> Double.compare(b.score, a.score));
            } else if (id == R.id.sort_price) {
                Collections.sort(ranked, (a, b) -> Integer.compare(a.price, b.price));
            } else if (id == R.id.sort_quality) {
                Collections.sort(ranked, (a, b) -> Integer.compare(b.quality, a.quality));
            }
            showList();
            return true;
        });
        popup.show();
    }
}
