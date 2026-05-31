package com.example.rgz;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

// ЛР8: график баллов соответствия на Canvas + анимация роста столбцов.
public class ScoreChartView extends View {

    private final Paint barPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private String[] labels = new String[0];
    private double[] values = new double[0];
    private float progress = 0f;

    public ScoreChartView(Context c, AttributeSet a) {
        super(c, a);
        textPaint.setColor(Color.parseColor("#212121"));
        textPaint.setTextSize(34f);
        barPaint.setColor(Color.parseColor("#1976D2"));
    }

    public void setData(List<Tool> tools) {
        int n = Math.min(tools.size(), 5);
        labels = new String[n];
        values = new double[n];
        for (int i = 0; i < n; i++) {
            labels[i] = tools.get(i).name;
            values[i] = tools.get(i).score;
        }
        animateBars();
    }

    private void animateBars() {
        progress = 0f;
        post(new Runnable() {
            @Override public void run() {
                progress += 0.05f;
                if (progress > 1f) progress = 1f;
                invalidate();
                if (progress < 1f) postDelayed(this, 16);
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (values.length == 0) return;
        int w = getWidth();
        int h = getHeight();
        int n = values.length;
        float barH = (h - 20f) / n;
        for (int i = 0; i < n; i++) {
            float top = i * barH + 6;
            float full = (float) (values[i] / 100.0) * (w - 260);
            float len = full * progress;
            canvas.drawRect(220, top, 220 + len, top + barH - 16, barPaint);
            canvas.drawText(labels[i], 6, top + barH / 2f, textPaint);
            canvas.drawText(String.valueOf((int) values[i]), 226 + len, top + barH / 2f + 8, textPaint);
        }
    }
}
