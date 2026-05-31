package com.example.rgz;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

// ЛР12: фрагмент с характеристиками инструмента.
public class CharacteristicsFragment extends Fragment {

    public CharacteristicsFragment() { super(R.layout.fragment_characteristics); }

    public static CharacteristicsFragment newInstance(Tool t) {
        CharacteristicsFragment f = new CharacteristicsFragment();
        Bundle b = new Bundle();
        b.putInt("quality", t.quality);
        b.putInt("privacy", t.privacy);
        b.putInt("ease", t.ease);
        b.putInt("ide", t.ide);
        b.putInt("price", t.price);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle s) {
        super.onViewCreated(view, s);
        Bundle b = getArguments();
        ((TextView) view.findViewById(R.id.charText)).setText("Характеристики:\n\n" +
                "Качество результата: " + b.getInt("quality") + "/5\n" +
                "Приватность данных: " + b.getInt("privacy") + "/5\n" +
                "Простота освоения: " + b.getInt("ease") + "/5\n" +
                "Интеграция с IDE: " + (b.getInt("ide") == 1 ? "есть" : "нет") + "\n" +
                "Стоимость: " + b.getInt("price") + " $/мес");
    }
}
