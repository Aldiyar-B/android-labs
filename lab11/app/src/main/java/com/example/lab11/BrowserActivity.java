package com.example.lab11;

import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class BrowserActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_URL = "url";
    public static final String EXTRA_FALLBACK_TEXT = "fallback_text";

    private WebView webView;
    private String pageTitle;
    private String fallbackText;
    private String infoUrl;
    private boolean fallbackShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        pageTitle = getIntent().getStringExtra(EXTRA_TITLE);
        fallbackText = getIntent().getStringExtra(EXTRA_FALLBACK_TEXT);
        infoUrl = getIntent().getStringExtra(EXTRA_URL);

        if (infoUrl == null || infoUrl.trim().isEmpty()) {
            finish();
            return;
        }

        TextView webTitle = findViewById(R.id.web_title);
        webView = findViewById(R.id.info_web_view);

        webTitle.setText(pageTitle == null ? "Справка" : "Справка: " + pageTitle);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(
                    WebView view,
                    WebResourceRequest request,
                    WebResourceError error
            ) {
                super.onReceivedError(view, request, error);

                if (request.isForMainFrame() && !fallbackShown) {
                    showOfflineReference();
                }
            }
        });

        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl(encodeUrl(infoUrl));

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (webView != null && webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
            }
        });
    }

    private String encodeUrl(String url) {
        Uri uri = Uri.parse(url);
        String path = uri.getPath();

        if (path == null) {
            return url;
        }

        return uri.buildUpon()
                .encodedPath(Uri.encode(path, "/"))
                .build()
                .toString();
    }

    private void showOfflineReference() {
        fallbackShown = true;

        String safeTitle = Html.escapeHtml(pageTitle == null ? "Справка" : pageTitle);
        String safeText = Html.escapeHtml(fallbackText == null
                ? "Информация временно недоступна. Проверьте интернет в эмуляторе."
                : fallbackText);
        String safeUrl = Html.escapeHtml(infoUrl);

        String html = "<!doctype html><html><head><meta charset='utf-8'>"
                + "<style>"
                + "body{font-family:sans-serif;padding:20px;line-height:1.5;color:#1f2933;}"
                + "h1{font-size:24px;margin-bottom:12px;}"
                + ".notice{background:#fff3cd;padding:12px;border-radius:8px;margin-bottom:16px;}"
                + ".url{font-size:13px;color:#52616b;word-break:break-all;margin-top:18px;}"
                + "</style></head><body>"
                + "<div class='notice'>Сайт не открылся. Вероятно, в эмуляторе нет интернета.</div>"
                + "<h1>" + safeTitle + "</h1>"
                + "<p>" + safeText + "</p>"
                + "<p class='url'>Источник: " + safeUrl + "</p>"
                + "</body></html>";

        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
    }
}