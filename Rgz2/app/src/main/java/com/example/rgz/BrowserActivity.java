package com.example.rgz;

import android.os.Bundle;
import android.text.Html;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

// ЛР9: встроенный браузер на WebView с оффлайн-заглушкой.
public class BrowserActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_URL = "url";
    public static final String EXTRA_FALLBACK_TEXT = "fallback_text";

    private WebView webView;
    private String pageTitle;
    private String fallbackText;
    private String url;
    private boolean fallbackShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        pageTitle = getIntent().getStringExtra(EXTRA_TITLE);
        fallbackText = getIntent().getStringExtra(EXTRA_FALLBACK_TEXT);
        url = getIntent().getStringExtra(EXTRA_URL);
        if (url == null || url.trim().isEmpty()) { finish(); return; }

        ((TextView) findViewById(R.id.web_title)).setText(pageTitle == null ? "Сайт" : pageTitle);
        webView = findViewById(R.id.info_web_view);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (request.isForMainFrame() && !fallbackShown) showOffline();
            }
        });
        webView.loadUrl(url);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override public void handleOnBackPressed() {
                if (webView != null && webView.canGoBack()) webView.goBack();
                else finish();
            }
        });
    }

    private void showOffline() {
        fallbackShown = true;
        String t = Html.escapeHtml(pageTitle == null ? "Сайт" : pageTitle);
        String txt = Html.escapeHtml(fallbackText == null ? "Сайт недоступен. Проверьте интернет." : fallbackText);
        String u = Html.escapeHtml(url);
        String html = "<!doctype html><html><head><meta charset='utf-8'>"
                + "<style>body{font-family:sans-serif;padding:20px;line-height:1.5;}"
                + ".notice{background:#fff3cd;padding:12px;border-radius:8px;margin-bottom:16px;}"
                + ".url{font-size:13px;color:#666;word-break:break-all;margin-top:18px;}</style></head><body>"
                + "<div class='notice'>Сайт не открылся. Вероятно, в эмуляторе нет интернета.</div>"
                + "<h1>" + t + "</h1><p>" + txt + "</p><p class='url'>Источник: " + u + "</p></body></html>";
        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
    }
}
