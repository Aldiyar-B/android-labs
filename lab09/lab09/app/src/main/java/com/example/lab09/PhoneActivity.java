package com.example.lab09;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PhoneActivity extends AppCompatActivity {

    Button btnPhone1, btnPhone2, btnPhone3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        btnPhone1 = findViewById(R.id.btnPhone1);
        btnPhone2 = findViewById(R.id.btnPhone2);
        btnPhone3 = findViewById(R.id.btnPhone3);

        btnPhone1.setOnClickListener(v -> dialNumber("+79991111111"));
        btnPhone2.setOnClickListener(v -> dialNumber("+79992222222"));
        btnPhone3.setOnClickListener(v -> dialNumber("+79993333333"));
    }

    private void dialNumber(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
        startActivity(intent);
    }
}