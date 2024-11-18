package com.myapp.autogallery.items;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

import com.myapp.autogallery.R;

public class TrapezoidImage extends AppCompatImageView {
    private Bitmap bitmap;
    private Paint paint;
    private Path line, secondImage;
    private Matrix matrix;
    private Bitmap dec;

    private int resource;
    private int translateX, translateY;
    private int lineSize, lineColor;
    private float rotate;

    private int bitmapWidth, heightBitmap;
    private float viewWidth, viewHeight;

    public TrapezoidImage(Context context, Bitmap bitmap) {
        super(context);
        this.bitmap = bitmap;
    }

    public TrapezoidImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttrs(context, attrs);
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }

    public void setAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.TrapezoidImage);

            resource = attr.getResourceId(R.styleable.TrapezoidImage_resource, 0);

            translateY = attr.getDimensionPixelOffset(R.styleable.TrapezoidImage_translateY, 0);
            translateX = attr.getDimensionPixelOffset(R.styleable.TrapezoidImage_translateX, 0);
            rotate = attr.getFloat(R.styleable.TrapezoidImage_rotate, 0);

            lineSize = attr.getDimensionPixelOffset(R.styleable.TrapezoidImage_lineSize, 0);

            int type = attr.getType(R.styleable.TrapezoidImage_color);
            if (type == TypedValue.TYPE_REFERENCE)
                lineColor = attr.getResourceId(R.styleable.TrapezoidImage_color, Color.WHITE);
            else
                lineColor = attr.getColor(R.styleable.TrapezoidImage_color, Color.WHITE);

            attr.recycle();
        }
    }

    public void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        secondImage = new Path();
        line = new Path();

        float radians = (float) Math.toRadians(90 + rotate);
        float hypotenuse = (float) (viewHeight / Math.sin(radians));
        float corner = (float) (hypotenuse * Math.cos(radians));

        drawBorderLine(line, corner);
        drawMask(secondImage, corner);

        paint.setColor(lineColor);
        line.close();
        secondImage.close();

        Log.d("line", hypotenuse + " " + corner);
        Log.d("radians", String.valueOf(radians));
    }

    protected void drawBorderLine(Path path, float corner) {
        if (corner < 0) {
            path.moveTo(-corner, 0);
            path.lineTo(-corner + lineSize, 0);
            path.lineTo(lineSize, viewHeight);
            path.lineTo(0, viewHeight);
        }
        else {
            path.moveTo(0, 0);
            path.lineTo(lineSize, 0);
            path.lineTo(corner, viewHeight);
            path.lineTo(corner - lineSize, viewHeight);
        }
    }

    protected void drawMask(Path path, float corner) {
        float start = lineSize;
        float end = corner;

        if (corner < 0) {
            start = -corner + lineSize;
            end = lineSize;
        }

        path.moveTo(start, 0);
        path.lineTo(viewWidth, 0);
        path.lineTo(viewWidth, viewHeight);
        path.lineTo(end, viewHeight);
    }

    protected void setScaleType() {
        if (resource != 0) {
            matrix = new Matrix();

            float scaleX = viewWidth / bitmapWidth;
            float scaleY = viewHeight / heightBitmap;
            float scale = Math.min(scaleY, scaleX);

            float dx = (viewWidth - bitmapWidth * scale) / 2;
            float dy = (viewHeight - heightBitmap * scale) / 2;

            matrix.reset();
            matrix.postScale(scale, scale);
            matrix.postTranslate(translateX + dx, translateY + dy);

            Log.d("sizeX", bitmapWidth + " " + viewWidth);
            Log.d("sizeY", heightBitmap + " " + (viewHeight + translateY));
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.save();
        canvas.drawPath(line, paint);
        canvas.clipPath(secondImage);
        super.onDraw(canvas);
        canvas.restore();

        if (resource != 0) canvas.drawBitmap(dec, matrix, null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
        init();
        setScaleType();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);

        if (setViewSize())
            setMeasuredDimension(Math.min(parentWidth, bitmapWidth), Math.min(parentHeight, heightBitmap));
        else
            setMeasuredDimension(parentWidth, parentHeight);

        Log.d("measure", String.valueOf(parentWidth));
    }

    protected boolean setViewSize() {
        if (resource == 0) return false;

        dec = BitmapFactory.decodeResource(getResources(), resource);
        bitmapWidth = dec.getWidth();
        heightBitmap = dec.getHeight();
        return true;
    }
}

