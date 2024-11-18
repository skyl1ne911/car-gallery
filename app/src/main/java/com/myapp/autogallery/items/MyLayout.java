package com.myapp.autogallery.items;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.myapp.autogallery.R;

public class MyLayout extends ViewGroup {

    private int count, orientation;
    private int offsetRight, offsetLeft, offsetTop, offsetBottom;
    private int offsetRightLeft, offsetTopBottom;

    private Paint paint;
    private Path line;


    public MyLayout(Context context) {
        super(context);
        init(context, null);
    }

    public MyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray type = context.obtainStyledAttributes(attrs, R.styleable.MyLayout);
            count = type.getInt(R.styleable.MyLayout_count, 0);
            orientation = type.getInt(R.styleable.MyLayout_orientation, 0);

            offsetRight = type.getDimensionPixelOffset(R.styleable.MyLayout_offset_right, 0);
            offsetLeft = -type.getDimensionPixelOffset(R.styleable.MyLayout_offset_left, 0);
            offsetTop = type.getDimensionPixelOffset(R.styleable.MyLayout_offset_top, 0);
            offsetBottom = -type.getDimensionPixelOffset(R.styleable.MyLayout_offset_bottom, 0);

            offsetRightLeft = offsetRight + offsetLeft;
            offsetTopBottom = offsetTop + offsetBottom;

            type.recycle();
        }

    }

    public void draw(int count, int corner) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        line = new Path();

        if (orientation == 1) {
            if (count == 2) {
                line.moveTo(0, getHeight() / 2 - 5);
                line.lineTo(getWidth(), getHeight() / 2 - 5);
                line.lineTo(getWidth(), getHeight() / 2 + 5);
                line.lineTo(0, getHeight() / 2 + 5);
            }
        }
        else {
            if (count == 2) {
                line.moveTo(getWidth() / 2 - 5, 0);
                line.lineTo(getWidth() / 2 + 5, 0);
                line.lineTo(getWidth() / 2 + 5, getHeight());
                line.lineTo(getWidth() / 2 - 5, getHeight());

            }
        }

        if (offsetRight != 0) line.offset(offsetRight, 0);
        if (offsetLeft != 0) line.offset(offsetLeft, 0);
        if (offsetTop != 0) line.offset(0, offsetTop);
        if (offsetBottom != 0) line.offset(0, offsetBottom);

        line.close();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (line != null) {
            canvas.save();
            canvas.drawPath(line, paint);
            canvas.restore();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int l = 0;
        int r = right / 2 + offsetRightLeft - 5;
        int t = 0;

        if (count != 0) draw(count, 0);



        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            if (child.getVisibility() != GONE) {
                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();



                child.layout(l, t, r,
                        height + offsetTopBottom);

                l += width + offsetRightLeft + 5;
                r = getWidth();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0;
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            if (child.getVisibility() != GONE) {
                child.measure(MeasureSpec.makeMeasureSpec(widthMeasureSpec / count,
                                MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(heightMeasureSpec, MeasureSpec.EXACTLY));


            }
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }
}
