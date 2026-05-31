package com.example.rgz;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

// Карточка инструмента. ЛР12 (два фрагмента), ЛР9 (WebView, e-mail, проверка интернета).
public class ToolDetailActivity extends AppCompatActivity {

    public static final String EXTRA_TOOL_ID = "tool_id";
    private Tool tool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_detail);

        int id = getIntent().getIntExtra(EXTRA_TOOL_ID, -1);
        tool = new DBHelper(this).getToolById(id);
        if (tool == null) { finish(); return; }

        ((TextView) findViewById(R.id.detailName)).setText(tool.name);
        ((TextView) findViewById(R.id.detailVendor)).setText(tool.vendor + " · " + tool.category);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.charContainer, CharacteristicsFragment.newInstance(tool))
                    .replace(R.id.riskContainer, RisksFragment.newInstance(tool))
                    .commit();
        }

        ((Button) findViewById(R.id.btnOpenSite)).setOnClickListener(v -> openSite());
        ((Button) findViewById(R.id.btnEmail)).setOnClickListener(v -> sendRecommendation());
    }

    private void openSite() {
        if (!isOnline()) {
            Toast.makeText(this, "Нет подключения к сети", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, BrowserActivity.class);
        intent.putExtra(BrowserActivity.EXTRA_TITLE, tool.name);
        intent.putExtra(BrowserActivity.EXTRA_URL, tool.siteUrl);
        intent.putExtra(BrowserActivity.EXTRA_FALLBACK_TEXT,
                tool.name + " — " + tool.category + " от " + tool.vendor + ".");
        startActivity(intent);
    }

    private void sendRecommendation() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Рекомендация: " + tool.name);
        intent.putExtra(Intent.EXTRA_TEXT,
                "Рекомендую инструмент " + tool.name + " (" + tool.vendor + ").\n" +
                "Категория: " + tool.category + "\n" +
                "Стоимость: " + tool.price + " $/мес\n" +
                "Сайт: " + tool.siteUrl);
        startActivity(Intent.createChooser(intent, "Отправить рекомендацию"));
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm != null ? cm.getActiveNetworkInfo() : null;
        return info != null && info.isConnected();
    }
}
