package com.example.rgz;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

// ЛР12: фрагмент с рисками и ограничениями инструмента.
public class RisksFragment extends Fragment {

    public RisksFragment() { super(R.layout.fragment_risks); }

    public static RisksFragment newInstance(Tool t) {
        RisksFragment f = new RisksFragment();
        Bundle b = new Bundle();
        b.putString("risks", t.risks);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle s) {
        super.onViewCreated(view, s);
        ((TextView) view.findViewById(R.id.riskText)).setText(
                "Риски и ограничения:\n\n" + getArguments().getString("risks"));
    }
}
