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
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewParent;

import androidx.annotation.IntDef;
import androidx.appcompat.widget.AppCompatImageView;

import com.myapp.autogallery.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class CollageImageView extends AppCompatImageView {
    public final static int VERTICAL = 0;
    public final static int HORIZONTAL = 1;

    public final static int LEFT = 0;
    public final static int TOP = 1;
    public final static int RIGHT = 2;
    public final static int BOTTOM = 3;
    public final static int CENTER = 4;

    @IntDef({VERTICAL, HORIZONTAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Orientation {}
    private int lineOrientation;

    @IntDef({LEFT, TOP, RIGHT, BOTTOM, CENTER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Sides {}
    private int lineStart, byParent, lineRotateSide;

    private Path transformedShape;
    private Path transformedLine;

    private Bitmap bitmap;
    private Paint paint;
    private Path secondImage;
    private Matrix matrix;
    private Bitmap dec;

    private int resource;
    private int translateX, translateY;
    private int lineSize, lineColor;
    private float rotateDegrees;

    private int bitmapWidth, bitmapHeight;
    private int viewWidth, viewHeight;

    private float[] lineCoords;

    private final String CLASS_NAME = getClass().getName();

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
            lineCoords = new float[2];

            resource = attr.getResourceId(R.styleable.CollageImageView_resource, 0);
            translateX = attr.getDimensionPixelOffset(R.styleable.CollageImageView_translateX, 0);
            translateY = attr.getDimensionPixelOffset(R.styleable.CollageImageView_translateY, 0);
            rotateDegrees = attr.getFloat(R.styleable.CollageImageView_rotate, 0);
            lineSize = attr.getDimensionPixelOffset(R.styleable.CollageImageView_lineSize, 0);
            lineOrientation = attr.getInt(R.styleable.CollageImageView_lineOrientation, VERTICAL);
            byParent = attr.getInt(R.styleable.CollageImageView_from_line, LEFT);
            lineRotateSide = attr.getInt(R.styleable.CollageImageView_rotateSide, CENTER);
            lineStart = attr.getInt(R.styleable.CollageImageView_lineStart, -1);

            if (type == TypedValue.TYPE_REFERENCE)
                lineColor = attr.getResourceId(R.styleable.CollageImageView_color, Color.WHITE);
            else
                lineColor = attr.getColor(R.styleable.CollageImageView_color, Color.WHITE);

            attr.recycle();
        }
    }

    protected void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        transformedLine = new Path();
        transformedShape = new Path();
        ViewParent parent = getParent();
        int parentWidth = ((View) parent).getWidth();
        int parentHeight = ((View) parent).getHeight();

        float startX = translateX, startY = translateY;
        if (lineRotateSide != CENTER) rotateDegrees = getCorrectDegrees();

        if (lineStart != -1) {
            startX += getVerticalLineStart();
            startY += getHorizontalLineStart();
        }
        else {
            startX += getDefaultVerticalLineStart(parentWidth);
            startY += getDefaultHorizontalLineStart(parentHeight);
        }

        transformedLine = drawLine(startX, startY);
        setRotateLine(transformedLine, rotateDegrees);
        transformedShape = drawShape(startX, startY);
        setMatrixShape(transformedShape, rotateDegrees);

        paint.setStrokeWidth(lineSize);
        paint.setColor(lineColor);

        Log.d(CLASS_NAME + " init.degrees", String.valueOf(rotateDegrees));
        Log.d(CLASS_NAME + " init.[startX | startY]", String.format("X: %.2f Y: %d", startX, 0));
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawPath(transformedLine, paint);
        canvas.clipPath(transformedShape);
        super.onDraw(canvas);
        canvas.drawPath(transformedLine, paint);

//        transformedLine.close();
//        transformedShape.close();

        Log.d(CLASS_NAME, "onDraw");
        if (resource != 0) canvas.drawBitmap(dec, matrix, null);
    }

    private void initializeLine(Path line) {
        line.moveTo(0, 0);
        line.rLineTo(lineSize, 0);
        line.rLineTo(0, viewHeight);
        line.rLineTo(-lineSize, 0);
    }

    private void initializeShape(Path shape) {
        shape.moveTo(0, 0);
        shape.lineTo(viewWidth, 0);
        shape.lineTo(viewWidth, viewHeight);
        shape.lineTo(0, viewHeight);
    }

    public Path drawLine(float startX, float startY) {
        Path line = new Path();
        Matrix matrix = new Matrix();

        float lineScaleY = (float) (viewWidth + translateX) / (viewHeight + translateY) * 4;
        float centerY = (float) (viewHeight / 2 - lineSize / 2);
        initializeLine(line);


        matrix.setScale(1, lineScaleY, 0, centerY);
        if (lineOrientation == HORIZONTAL) matrix.postRotate(-90);

        matrix.postTranslate(startX, startY);
        line.transform(matrix);

        PathMeasure pathMeasure = new PathMeasure(line, false);
        pathMeasure.getPosTan(pathMeasure.getLength(), lineCoords, null);
        Log.d(CLASS_NAME + " drawLine.coords", lineCoords[0] + " " + lineCoords[1]);
        return line;
    }

    public Path drawShape(float startX, float startY) {
        Path shape = new Path();
        Matrix matrix = new Matrix();
        float[] shapeCoords = {1, 1, 1, 1};

        initializeShape(shape);

        switch (byParent) {
            case LEFT:
                shapeCoords[0] = startX / viewWidth;
                shapeCoords[2] = 0;
                break;
            case RIGHT:
                shapeCoords[0] = 1 - startX / viewWidth;
                shapeCoords[2] = viewWidth;
                break;
            case TOP:
                shapeCoords[1] = startY / viewHeight;
                shapeCoords[3] = 0;
                break;
            case BOTTOM:
                shapeCoords[1] = 1 - startY / viewHeight;
                shapeCoords[3] = viewHeight;
                break;
        }
        matrix.setScale(shapeCoords[0], shapeCoords[1], shapeCoords[2], shapeCoords[3]);
        shape.transform(matrix);
        return shape;
    }

    public void setRotateLine(Path path, float degrees) {
        if (degrees == 0) return;
        Matrix matrix = new Matrix();
        float[] pivot = getMatrixRotate();

        matrix.postRotate(degrees, pivot[0], pivot[1]);
        path.transform(matrix);
    }

    public void setMatrixShape(Path path, float degrees) {
        if (degrees == 0) return;
        Matrix matrix = new Matrix();
        float radians = (float) Math.toRadians(degrees);
        float kx = (degrees % 90 == 0) ? 0 : (float) Math.tan(radians);
        float scaleX = 1 + (float) Math.sqrt(viewWidth / viewHeight * 2 + kx * kx);
        float[] pivot = getMatrixRotate();

        matrix.setScale(scaleX, scaleX, pivot[0], pivot[1]);

        if (degrees % 90 == 0)
            matrix.postRotate(degrees, pivot[0], pivot[1]);

        float skewX = (lineOrientation == VERTICAL) ? -kx : 0;
        float skewY = (lineOrientation == VERTICAL) ? 0 : kx;
        matrix.postSkew(skewX, skewY, pivot[0], pivot[1]);

        path.transform(matrix);
        Log.d(CLASS_NAME + " setMatrixShape.kx", String.valueOf(kx));
        Log.d(CLASS_NAME + " setMatrixShape.scaleX", String.valueOf(scaleX));
    }

    protected float[] getMatrixRotate() {
        float[] start = new float[2];
        start[0] = lineCoords[0];
        start[1] = lineCoords[1];

        if (lineOrientation == VERTICAL)
            start[1] = getVerticalLineRotation();
        else
            start[0] = getHorizontalLineRotation();

        return start;
    }

    private float getDefaultVerticalLineStart(float pw) { // PARENT WIDTH
        if (viewWidth == pw && lineOrientation == VERTICAL) return viewWidth / 2;
        return 0;
    }

    private float getDefaultHorizontalLineStart(float ph) { // PARENT HEIGHT
        if (viewHeight == ph && lineOrientation == HORIZONTAL) return viewHeight / 2;
        return 0;
    }

    private float getVerticalLineStart() {
        if (lineStart == LEFT) return 0;
        if (lineStart == RIGHT) return viewWidth - lineSize;
        return viewWidth / 2; // CENTER
    }

    private float getHorizontalLineStart() {
        if (lineStart == TOP) return lineSize;
        if (lineStart == BOTTOM) return viewHeight;
        return viewHeight / 2 - (float) lineSize / 2; // CENTER
    }

    private float getVerticalLineRotation() {
        if (lineRotateSide == TOP) return 0;
        if (lineRotateSide == BOTTOM) return viewHeight;
        return viewHeight / 2; // CENTER
    }

    private float getHorizontalLineRotation() {
        if (lineRotateSide == LEFT) return 0;
        if (lineRotateSide == RIGHT) return viewWidth;
        return viewWidth / 2; // CENTER
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
        if (oldw != w || oldh != h) {
            setLineTranslate(lineOrientation);
//            setScaleType();
            init();
        }
        Log.d(CLASS_NAME + " onSizeChanged.viewWidth", " " + viewWidth);
        Log.d(CLASS_NAME + " onSizeChanged.viewHeight", " " + viewHeight);
        Log.d(CLASS_NAME, "onSizeChanged");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);

//        Drawable drawable = getDrawable();
//        bitmap = ((BitmapDrawable) drawable).getBitmap();

        if (setViewSize())
            setMeasuredDimension(Math.min(parentWidth, bitmapWidth), Math.min(parentHeight, bitmapHeight));
        else
            setMeasuredDimension(parentWidth, parentHeight);

        Log.d(CLASS_NAME + " onMeasure.parentX", String.valueOf(parentWidth));
        Log.d(CLASS_NAME + " onMeasure.parentY", String.valueOf(parentHeight));
        Log.d("bitmapSize", String.format("w: %d  h: %d", bitmapWidth, bitmapHeight));
        Log.d(CLASS_NAME, "onMeasure");
    }

    protected boolean setViewSize() {
        if (resource == 0) return false;

        dec = BitmapFactory.decodeResource(getResources(), resource);
        bitmapWidth = dec.getWidth();
        bitmapHeight = dec.getHeight();
        return true;
    }

    protected void setScaleType() {
        if (resource == 0) return;
        matrix = new Matrix();

        float scaleX = viewWidth / bitmapWidth;
        float scaleY = viewHeight / bitmapHeight;
        float scale = Math.min(scaleY, scaleX);

        float dx = (viewWidth - bitmapWidth * scale) / 2;
        float dy = (viewHeight - bitmapHeight * scale) / 2;

        matrix.reset();
        matrix.postScale(scale, scale);
        matrix.postTranslate(translateX + dx, translateY + dy);
    }

    public void setLineTranslate(int lineOrientation) {
        if (lineOrientation == VERTICAL) translateY = 0;
        else translateX = 0;
    }

    private float getCorrectDegrees() {
        float degrees = rotateDegrees;
        if (degrees >= 90) {
            for (float i = degrees; i >= 0; i -= 90) {
                degrees = i;
                Log.d(CLASS_NAME + " getCorrectDegrees", String.valueOf(degrees));
            }
        }
        else if (degrees <= -90) {
            for (float i = degrees; i <= 0; i += 90) {
                degrees = i;
                Log.d(CLASS_NAME + " getCorrectDegrees", String.valueOf(degrees));
            }
        }
        return degrees;
    }

    public void setTranslateX(int x) { translateX = x; }

    public void setTranslateY(int y) { translateY = y; }

    public void setRotateDegrees(float degrees) { rotateDegrees = degrees; }

    public void setLineSize(int size) { lineSize = size; }

    public void setLineStart(@Sides int side) { lineStart = side; }

    public void setRotateSide(@Sides int side) { byParent = side; }

    public void setLineOrientation(@Orientation int orientation) { lineOrientation = orientation; }

    public void setLineRotateSide(@Sides int turnSide) { lineRotateSide = turnSide; }

    public void setLineColor(int color) { lineColor = color; }
}

