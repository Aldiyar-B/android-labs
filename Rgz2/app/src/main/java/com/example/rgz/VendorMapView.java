package com.example.rgz;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

// ЛР8/ЛР10: стилизованная карта на Canvas с маркерами вендоров и обработкой нажатий.
public class VendorMapView extends View {

    public interface OnMarkerClickListener {
        void onMarkerClick(String placeId);
    }

    public static class Marker {
        String id; String name; float fx; float fy; boolean answered;
        Marker(String id, String name, float fx, float fy) {
            this.id = id; this.name = name; this.fx = fx; this.fy = fy;
        }
    }

    private final List<Marker> markers = new ArrayList<>();
    private final Paint sea = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint land = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pin = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint text = new Paint(Paint.ANTI_ALIAS_FLAG);
    private OnMarkerClickListener listener;

    public VendorMapView(Context c, AttributeSet a) {
        super(c, a);
        sea.setColor(Color.parseColor("#BBDEFB"));
        land.setColor(Color.parseColor("#A5D6A7"));
        text.setColor(Color.parseColor("#212121"));
        text.setTextSize(32f);

        markers.add(new Marker("github", "GitHub", 0.15f, 0.45f));
        markers.add(new Marker("openai", "OpenAI", 0.20f, 0.38f));
        markers.add(new Marker("anthropic", "Anthropic", 0.25f, 0.55f));
        markers.add(new Marker("jetbrains", "JetBrains", 0.55f, 0.36f));
    }

    public void setListener(OnMarkerClickListener l) { this.listener = l; }

    public void setAnswered(String id, boolean answered) {
        for (Marker m : markers) if (m.id.equals(id)) m.answered = answered;
        invalidate();
    }

    public int total() { return markers.size(); }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();

        canvas.drawRect(0, 0, w, h, sea);
        // условные "континенты"
        canvas.drawRoundRect(w * 0.05f, h * 0.25f, w * 0.40f, h * 0.75f, 40, 40, land); // Америка
        canvas.drawRoundRect(w * 0.48f, h * 0.20f, w * 0.70f, h * 0.55f, 40, 40, land); // Европа
        canvas.drawRoundRect(w * 0.72f, h * 0.30f, w * 0.95f, h * 0.70f, 40, 40, land); // Азия

        for (Marker m : markers) {
            float x = m.fx * w;
            float y = m.fy * h;
            pin.setColor(m.answered ? Color.parseColor("#2E7D32") : Color.parseColor("#D32F2F"));
            canvas.drawCircle(x, y, 22, pin);
            canvas.drawText(m.name, x + 28, y + 10, text);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int w = getWidth();
            int h = getHeight();
            for (Marker m : markers) {
                float x = m.fx * w;
                float y = m.fy * h;
                float dx = event.getX() - x;
                float dy = event.getY() - y;
                if (dx * dx + dy * dy <= 60 * 60) {
                    if (listener != null) listener.onMarkerClick(m.id);
                    performClick();
                    return true;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() { return super.performClick(); }
}
