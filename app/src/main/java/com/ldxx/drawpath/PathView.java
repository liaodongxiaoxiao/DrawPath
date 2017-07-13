package com.ldxx.drawpath;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by wangzhuo-neu on 2017/7/13.
 */

public class PathView extends View {
    private static final String TAG = "PathView";
    private Paint paint;

    public PathView(Context context) {
        this(context, null);

    }

    public PathView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(4);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();

        //Rect r = new Rect();
        Log.e(TAG, "Rect x:" + width/2 + " y:" + height/2);
        canvas.drawCircle(width/2, height/2,10, paint);
    }
}
