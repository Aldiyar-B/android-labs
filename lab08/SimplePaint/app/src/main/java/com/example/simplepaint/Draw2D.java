package com.example.simplepaint;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class Draw2D extends View {

    private Paint mPaint = new Paint();
    private Rect mRect = new Rect();
    private Bitmap mBitmap;

    public Draw2D(Context context) {
        super(context);

        Resources res = this.getResources();
        Bitmap original = BitmapFactory.decodeResource(res, R.drawable.dog);

        mBitmap = Bitmap.createScaledBitmap(original, 200, 200, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        // фон
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);
        canvas.drawPaint(mPaint);

        // солнце
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.YELLOW);
        canvas.drawCircle(width - 60, 60, 40, mPaint);

        // лужайка
        mPaint.setColor(Color.GREEN);
        canvas.drawRect(0, height - 120, width, height, mPaint);

        // текст
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(40);
        canvas.drawText("Собака на лужайке", 30, height - 140, mPaint);

        // наклонный текст
        int x = width - 250;
        int y = 220;
        mPaint.setColor(Color.GRAY);
        mPaint.setTextSize(32);
        String beam = "Лучик солнца!";

        canvas.save();
        canvas.rotate(-45, x + mRect.exactCenterX(), y + mRect.exactCenterY());
        canvas.drawText(beam, x, y, mPaint);
        canvas.restore();

        // картинка собаки
        canvas.drawBitmap(
                mBitmap,
                width - mBitmap.getWidth(),
                height - mBitmap.getHeight() - 120,
                mPaint
        );
    }
}