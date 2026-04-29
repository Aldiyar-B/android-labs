package com.example.lab09;

import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class BrowserActivity extends AppCompatActivity {

    private WebView webView;
    private EditText urlEditText;
    private Button btnBack, btnGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        webView = findViewById(R.id.webView);
        urlEditText = findViewById(R.id.urlEditText);
        btnBack = findViewById(R.id.btnBack);
        btnGo = findViewById(R.id.btnGo);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        String url = getIntent().getStringExtra("url");

        if (url != null && !url.isEmpty()) {
            loadPage(url);
        } else {
            urlEditText.setText("local://webview");
            webView.loadDataWithBaseURL(
                    null,
                    "<html><body><h1>Собственный браузер</h1>" +
                            "<p>Страница открыта внутри WebView</p>" +
                            "</body></html>",
                    "text/html",
                    "UTF-8",
                    null
            );
        }

        btnGo.setOnClickListener(v -> {
            String newUrl = urlEditText.getText().toString().trim();

            if (newUrl.isEmpty()) {
                Toast.makeText(this, "Введите адрес сайта", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newUrl.startsWith("http://") && !newUrl.startsWith("https://")) {
                newUrl = "https://" + newUrl;
            }

            loadPage(newUrl);
        });

        btnBack.setOnClickListener(v -> {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                finish();
            }
        });

        urlEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                btnGo.performClick();
                return true;
            }
            return false;
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
            }
        });
    }

    private void loadPage(String url) {
        urlEditText.setText(url);
        webView.loadUrl(url);
    }
}