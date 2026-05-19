package com.example.lab12;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CounterFragment extends Fragment {

    private int counter = 0;

    public CounterFragment() {
        super(R.layout.fragment_counter);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView counterText = view.findViewById(R.id.counterText);
        Button btnPlus = view.findViewById(R.id.btnPlus);
        Button btnMinus = view.findViewById(R.id.btnMinus);

        btnPlus.setOnClickListener(v -> {
            counter++;
            counterText.setText(String.valueOf(counter));
        });

        btnMinus.setOnClickListener(v -> {
            if (counter > 0) {
                counter--;
                counterText.setText(String.valueOf(counter));
            }
        });
    }
}