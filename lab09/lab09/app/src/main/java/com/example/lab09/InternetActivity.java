package com.example.lab09;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class InternetActivity extends AppCompatActivity {

    private ImageButton btnCheckInternet;
    private FrameLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);

        btnCheckInternet = findViewById(R.id.btnCheckInternet);
        rootLayout = findViewById(R.id.rootLayout);

        btnCheckInternet.setOnClickListener(v -> checkInternet());
    }

    private void checkInternet() {
        ConnectivityManager conMan =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo nwi = conMan != null ? conMan.getActiveNetworkInfo() : null;

        if (nwi != null && nwi.isConnected()) {
            btnCheckInternet.setImageResource(R.drawable.door_open);
            Toast.makeText(this,
                    "Подключение есть\nДобро пожаловать в приложение!",
                    Toast.LENGTH_LONG).show();
        } else {
            btnCheckInternet.setImageResource(R.drawable.door_closed);
            Toast.makeText(this,
                    "Нет подключения\nРазрешите доступ и повторите попытку",
                    Toast.LENGTH_LONG).show();
        }
    }
}