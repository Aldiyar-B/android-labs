package com.example.lab09;

import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class BrowserActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        webView = findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        webView.setWebViewClient(new MyWebViewClient());

        Uri data = getIntent().getData();
        if (data != null) {
            webView.loadUrl(data.toString());
        } else {
            webView.loadUrl("https://nstu.ru");
        }

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

    private static class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(@NonNull WebView view,
                                                @NonNull WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }

        @Override
        @SuppressWarnings("deprecation")
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(@NonNull WebView view,
                                    @NonNull WebResourceRequest request,
                                    @NonNull WebResourceError error) {
            Toast.makeText(view.getContext(),
                    "Ошибка загрузки страницы",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        @SuppressWarnings("deprecation")
        public void onReceivedError(WebView view,
                                    int errorCode,
                                    String description,
                                    String failingUrl) {
            Toast.makeText(view.getContext(),
                    "Ошибка загрузки страницы: " + description,
                    Toast.LENGTH_SHORT).show();
        }
    }
}