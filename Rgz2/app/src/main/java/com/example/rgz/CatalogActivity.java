package com.example.rgz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// ЛР11: каталог инструментов из базы SQLite.
public class CatalogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        ListView list = findViewById(R.id.toolsList);
        Button btnMap = findViewById(R.id.btnVendorsMap);

        List<Tool> tools = new DBHelper(this).getAllTools();
        ArrayList<HashMap<String, String>> data = new ArrayList<>();
        for (Tool t : tools) {
            HashMap<String, String> item = new HashMap<>();
            item.put("title", t.name + "  (" + t.vendor + ")");
            item.put("description", t.category + " · " + t.price + " $/мес · качество " + t.quality + "/5");
            data.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.listview_item,
                new String[]{"title", "description"},
                new int[]{R.id.textTitle, R.id.textDescription});
        list.setAdapter(adapter);

        list.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, ToolDetailActivity.class);
            intent.putExtra(ToolDetailActivity.EXTRA_TOOL_ID, tools.get(position).id);
            startActivity(intent);
        });

        btnMap.setOnClickListener(v -> startActivity(new Intent(this, MapsActivity.class)));
    }
}
