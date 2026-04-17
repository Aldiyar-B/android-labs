package com.example.simplepaint;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Draw2D draw2D = new Draw2D(this);
        setContentView(draw2D);
    }
}