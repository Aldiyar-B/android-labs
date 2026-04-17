package com.example.lab09;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnInternet, btnPhone, btnEmail, btnLinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnInternet = findViewById(R.id.btnInternet);
        btnPhone = findViewById(R.id.btnPhone);
        btnEmail = findViewById(R.id.btnEmail);
        btnLinks = findViewById(R.id.btnLinks);

        btnInternet.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, InternetActivity.class)));

        btnPhone.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, PhoneActivity.class)));

        btnEmail.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, EmailActivity.class)));

        btnLinks.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, LinksActivity.class)));
    }
}