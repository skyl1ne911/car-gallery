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
import android.graphics.PathMeasure;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewParent;

import androidx.appcompat.widget.AppCompatImageView;

import com.myapp.autogallery.R;

public class CollageImageView extends AppCompatImageView {
    private final static int VERTICAL = 0;
    private final static int HORIZONTAL = 1;

    private final static int LINE_TOP = 0;
    private final static int LINE_BOTTOM = 1;

    private final static int RESOURCE_LEFT_SIDE = 0;
    private final static int RESOURCE_TOP_SIDE = 1;
    private final static int RESOURCE_RIGHT_SIDE = 2;
    private final static int RESOURCE_BOTTOM_SIDE = 3;

    private Bitmap bitmap;
    private Paint paint;
    private Path line, secondImage;
    private Matrix matrix;
    private Bitmap dec;

    private int lineStart, parentSide;
    private int orientation;
    private int resource;
    private int translateX, translateY;
    private int lineSize, lineColor;
    private float rotateDegrees;

    private int bitmapWidth, heightBitmap;
    private float viewWidth, viewHeight;


    public CollageImageView(Context context, Bitmap bitmap) {
        super(context);
        this.bitmap = bitmap;
    }

    public CollageImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttrs(context, attrs);
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }

    public void setAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.CollageImageView);
            int type = attr.getType(R.styleable.CollageImageView_color);
            int startPosition = RESOURCE_RIGHT_SIDE;

            resource = attr.getResourceId(R.styleable.CollageImageView_resource, 0);
            orientation = attr.getInt(R.styleable.CollageImageView_orientation, 0);
            translateX = attr.getDimensionPixelOffset(R.styleable.CollageImageView_translateX, 0);
            translateY = attr.getDimensionPixelOffset(R.styleable.CollageImageView_translateY, 0);
            rotateDegrees = attr.getFloat(R.styleable.CollageImageView_rotate, 0);
            lineSize = attr.getDimensionPixelOffset(R.styleable.CollageImageView_lineSize, 0);
            lineStart = attr.getInt(R.styleable.CollageImageView_lineStart, 0);

            if (orientation == HORIZONTAL) startPosition = RESOURCE_TOP_SIDE;
            parentSide = attr.getInt(R.styleable.CollageImageView_from_parent, startPosition);

            if (type == TypedValue.TYPE_REFERENCE)
                lineColor = attr.getResourceId(R.styleable.CollageImageView_color, Color.WHITE);
            else
                lineColor = attr.getColor(R.styleable.CollageImageView_color, Color.WHITE);

            attr.recycle();
        }
    }

    public void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        line = new Path();

        paint.setStrokeWidth(lineSize);
        paint.setColor(lineColor);

        Log.d("viewSizeX", " " + viewWidth);
        Log.d("viewSizeY", " " + viewHeight);
    }

    protected void drawBorderLine(Path path, int corner) {
        if (orientation == VERTICAL) {
            path.moveTo(0, 0);
            path.lineTo(lineSize, 0);
            path.lineTo(corner + lineSize, viewHeight);
            path.lineTo(corner, viewHeight);
        }
        else {
            path.moveTo(0, viewHeight);
            path.lineTo(viewWidth, viewHeight + corner);
            path.lineTo(viewWidth, viewHeight + corner - lineSize);
            path.lineTo(0, viewHeight - lineSize);
        }

        Matrix mirrorMatrix = new Matrix();
        if (corner < 0) {
            mirrorMatrix.setScale(-1, -1);
            mirrorMatrix.postTranslate(lineSize, viewHeight);
        }
        else {
            mirrorMatrix.setScale(1, 1);
            mirrorMatrix.postTranslate(0, 0);
        }

//        path.transform(mirrorMatrix, path);
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
        ViewParent parent = getParent();
        int parentWidth = ((View) parent).getWidth();
        int parentHeight = ((View) parent).getHeight();

        float startX = 0;
        if (viewWidth == parentWidth && orientation == VERTICAL) startX = viewWidth / 2;

        line.moveTo(startX, 0);
        line.lineTo(startX + lineSize, 0);
        line.lineTo(startX + lineSize, viewHeight);
        line.lineTo(startX, viewHeight);


        Log.d("viewParentSizeXY", parentWidth + " " + parentHeight);

        float degrees = 20;
        float sin, cos;

        if (degrees > 90) degrees = 90;
        else if (degrees < -90) degrees = -90;

        if (degrees == 90) {

        } else {
        }
            sin = (float) Math.sin(Math.toRadians(90 - degrees));
            cos = (float) Math.cos(Math.toRadians(90 - degrees));

        Matrix matrix = new Matrix();

//        matrix.setRotate(degrees, startX, 0);
//        if (lineStart == LINE_TOP) matrix.preScale(1, 1 + tg);
//        else {
//            matrix.preScale(1, -1 - tg);
//            matrix.postTranslate(0, viewHeight + lineSize);
//        }

        float result = viewHeight / sin;

        
        if (lineStart == LINE_TOP) {
            matrix.setScale(1, result / viewHeight, startX, 0);
            matrix.postRotate(degrees, startX, 0);
        }
        else {
            matrix.setScale(1, result / viewHeight, startX, viewHeight);
            matrix.postRotate(degrees, startX, viewHeight);
        }

        if (degrees == 90) matrix.postTranslate(0, -lineSize);
        else if (degrees == -90) matrix.postTranslate(0, lineSize);

        Path transformedLine = new Path();
        line.transform(matrix, transformedLine);

        PathMeasure measure = new PathMeasure(transformedLine, false);
        float[] endPoint = new float[2];
        measure.getPosTan(measure.getLength(), endPoint, null);


        Path path = new Path();
        if (lineStart == LINE_TOP) {
            if (parentSide == RESOURCE_RIGHT_SIDE) {
                path.moveTo(startX, 0);
                path.rLineTo(-result * cos, viewHeight);
                path.rLineTo(viewWidth, 0);
                path.lineTo(viewWidth, 0);
            }
            else if (parentSide == RESOURCE_LEFT_SIDE) {
                path.moveTo(startX, 0);
                path.rLineTo(-result * cos, viewHeight);
                path.rLineTo(-viewWidth, 0);
                path.lineTo(-viewWidth, 0);
            }
        }
        else if (lineStart == LINE_BOTTOM) {
            if (parentSide == RESOURCE_RIGHT_SIDE) {
                path.moveTo(startX + result * cos, 0);
                path.rLineTo(-result * cos, viewHeight);
                path.rLineTo(viewWidth, 0);
                path.lineTo(viewWidth, 0);
            }
            else if (parentSide == RESOURCE_LEFT_SIDE) {
                path.moveTo(startX + result * cos, 0);
                path.rLineTo(-result * cos, viewHeight);
                path.rLineTo(-viewWidth, 0);
                path.lineTo(-viewWidth, 0);
            }
        }

        path.close();
        transformedLine.close();

        canvas.drawPath(transformedLine, paint);
        canvas.clipPath(path);
        super.onDraw(canvas);
        canvas.drawPath(transformedLine, paint);


        Log.d("sin", String.valueOf(sin));
        Log.d("cos", String.valueOf(cos));
        Log.d("degrees", String.valueOf(rotateDegrees));
        Log.d("points", "P1: " + endPoint[0] + ", P2: " + endPoint[1]);
        Log.d("measureLength", String.valueOf(measure.getLength()));

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

        Drawable drawable = getDrawable();
        bitmap = ((BitmapDrawable) drawable).getBitmap();

        if (setViewSize())
            setMeasuredDimension(Math.min(parentWidth, bitmapWidth), Math.min(parentHeight, heightBitmap));
        else
            setMeasuredDimension(parentWidth, parentHeight);

        Log.d("parentX", String.valueOf(parentWidth));
        Log.d("parentY", String.valueOf(parentHeight));
    }

    protected boolean setViewSize() {
        if (resource == 0) return false;

        dec = BitmapFactory.decodeResource(getResources(), resource);
        bitmapWidth = dec.getWidth();
        heightBitmap = dec.getHeight();
        return true;
    }
}

