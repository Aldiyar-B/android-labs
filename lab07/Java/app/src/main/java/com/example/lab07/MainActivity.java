package com.example.lab07;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button button;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        View.OnClickListener viewClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        };

        button.setOnClickListener(viewClickListener);
        textView.setOnClickListener(viewClickListener);
        imageView.setOnClickListener(viewClickListener);
    }

    // обычное меню сверху
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // обработка обычного меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_lang1) {
            textView.setText("Вы выбрали Java!");
            return true;
        } else if (id == R.id.action_lang2) {
            textView.setText("Вы выбрали Kotlin!");
            return true;
        } else if (id == R.id.action_lang3) {
            textView.setText("Вы выбрали Python!");
            return true;
        } else if (id == R.id.action_settings) {
            textView.setText("Вы выбрали Settings!");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // PopupMenu
    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.popupmenu);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.menu1) {
                    Toast.makeText(getApplicationContext(),
                            "Вы выбрали PopupMenu 1",
                            Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.menu2) {
                    Toast.makeText(getApplicationContext(),
                            "Вы выбрали PopupMenu 2",
                            Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.menu3) {
                    Toast.makeText(getApplicationContext(),
                            "Вы выбрали PopupMenu 3",
                            Toast.LENGTH_SHORT).show();
                    return true;
                }

                return false;
            }
        });

        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                Toast.makeText(getApplicationContext(),
                        "onDismiss",
                        Toast.LENGTH_SHORT).show();
            }
        });

        popupMenu.show();
    }
}