package com.example.abgineh;

import static android.R.color.black;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SheetView extends View {

    public List<Placement> placements =
            new ArrayList<>();

    public int sheetWidth = 1;
    public int sheetHeight = 1;
     private  int sheetIndex = 0;
     private int totalSheets = 1;
    private final Paint paintPiece;
    private final Paint paintBorder;
    private final Paint textPaint;

    public SheetView(
            Context context,
            AttributeSet attrs
    ) {
        super(context, attrs);

        paintPiece = new Paint();
        paintPiece.setStyle(Paint.Style.FILL);

        paintBorder = new Paint();
        paintBorder.setStyle(Paint.Style.STROKE);
        paintBorder.setStrokeWidth(3f);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(24f);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        float scaleX =
                (float) getWidth() / sheetWidth;

        float scaleY =
                (float) getHeight() / sheetHeight;

        @SuppressLint("DrawAllocation") Random random = new Random();
        Paint pagePaint=new Paint();
        pagePaint.setColor(black);
        pagePaint.setTextSize(40);
        pagePaint.setAntiAlias(true);

        String pageText = (sheetIndex + 1) + " از " + totalSheets;
        canvas.drawText(pageText,20,50,pagePaint);

        for (Placement p : placements) {

            paintPiece.setColor(
                    Color.rgb(
                            random.nextInt(200),
                            random.nextInt(200),
                            random.nextInt(200)
                    )
            );

            if (p.rotated) {
                paintBorder.setColor(Color.RED);
            } else {
                paintBorder.setColor(Color.BLACK);
            }


            float left = p.x * scaleX;
            float top = p.y * scaleY;

            float right =
                    (p.x + p.width) * scaleX;

            float bottom =
                    (p.y + p.height) * scaleY;

            canvas.drawRect(
                    left,
                    top,
                    right,
                    bottom,
                    paintPiece
            );

            canvas.drawRect(
                    left,
                    top,
                    right,
                    bottom,
                    paintBorder
            );

            canvas.drawText(
                    p.width + "x" + p.height,
                    left + 10,
                    top + 30,
                    textPaint
            );
        }
    }
    public void setPageInfo (int index ,int total)
    {
        this.sheetIndex = index;
        this.totalSheets = total;

        invalidate();
    }
}