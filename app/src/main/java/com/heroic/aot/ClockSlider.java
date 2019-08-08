package com.heroic.aot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class ClockSlider extends View {
    public static final int CLOCK_SLIDER = 1;
    public static final int VOLUME_SLIDER = 2;
    public static final int VIBRATE_PICKER = 3;

    public static final boolean ENABLE_VIBRATE = false;
    private static final int INSETS = 6;
    private static final int MINUTES_PER_HALF_DAY = 100;

    private int width;
    private int height;
    private int centerX;
    private int centerY;
    private int diameter;
    private RectF innerCircle;
    private int displayMode = CLOCK_SLIDER;

    private Calendar start = new GregorianCalendar();
    private int startAngle = 90;
    private Calendar end = new GregorianCalendar();

    /**
     * minutes to shush.
     */
    private int minutes = 0;

    private Bitmap bgBitmap;
    private Bitmap fgBitmap;
    private Path clipPath = new Path();

    private float luftRotation = 0.0f;
    private int totalNicks = 100;
    private int currentNick = 0;

    public ClockSlider(Context context, AttributeSet attrs) {
        super(context, attrs);

        bgBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.round);
        fgBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.round);
    }

    public ClockSlider(Context context) {
        super(context);

        bgBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.round);
        fgBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.round);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (getWidth() != width || getHeight() != height) {
            width = bgBitmap.getWidth();
            height = bgBitmap.getHeight();
            //          width = getWidth();
            //          height = getHeight();
            centerX = width / 2;
            centerY = height / 2;

            diameter = Math.min(width, height) - (2 * INSETS);
            int thickness = diameter / 15;

            int left = (width - diameter) / 2;
            int top = (height - diameter) / 2;
            int bottom = top + diameter;
            int right = left + diameter;
            //          outerCircle = new RectF(left, top, right, bottom);
            int innerDiameter = diameter - thickness * 2;
            //          innerCircle = new RectF(left + thickness, top + thickness, left
            //           + thickness + innerDiameter, top + thickness + innerDiameter);

            innerCircle = new RectF(0, 0, width, height);
            canvas.drawBitmap(bgBitmap, null, innerCircle, null);
        }

        if (displayMode == CLOCK_SLIDER) {
            drawClock(canvas);
        } else {
            throw new AssertionError();
        }
    }

    private void drawClock(Canvas canvas) {
        int sweepDegrees = (minutes / 2) - 1;

        canvas.drawBitmap(bgBitmap, null, innerCircle, null);
        // the colored "filled" part of the circle
        drawArc(canvas, startAngle, sweepDegrees);

    }

    private void drawArc(Canvas canvas, int startAngle, int sweepDegrees) {
        if (sweepDegrees <= 0) {
            return;
        }
        clipPath.reset();
        clipPath.moveTo(innerCircle.centerX(), innerCircle.centerY());
        clipPath.arcTo(innerCircle, startAngle + sweepDegrees, -sweepDegrees);
        //      clipPath.lineTo(getWidth() / 2, getHeight() / 2);
        canvas.clipPath(clipPath);

        canvas.drawBitmap(fgBitmap, null, innerCircle, null);

        invalidate();
    }
}
