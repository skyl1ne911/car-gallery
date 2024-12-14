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

import androidx.appcompat.widget.AppCompatImageView;

import com.myapp.autogallery.R;

public class CollageImageView extends AppCompatImageView {
    public final static int LEFT = 0;
    public final static int TOP = 1;
    public final static int RIGHT = 2;
    public final static int BOTTOM = 3;
    public final static int CENTER = 4;

    private Path transformedShape;
    private Path transformedLine;

    private Bitmap bitmap;
    private Paint paint;
    private Path secondImage;
    private Matrix matrix;
    private Bitmap dec;

    private int lineStart, parentSide;
    private int resource;
    private int translateX, translateY;
    private int lineSize, lineColor;
    private float rotateDegrees;

    private int bitmapWidth, bitmapHeight;
    private float viewWidth, viewHeight;

    private float[] lineCoords;

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
            lineStart = attr.getInt(R.styleable.CollageImageView_lineStart, 1);
            parentSide = attr.getInt(R.styleable.CollageImageView_from_line, 0);

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
//        rotateDegrees = getCorrectDegrees();

        if (viewWidth == parentWidth && (lineStart == TOP || lineStart == BOTTOM)) {
            if (lineStart == BOTTOM) startY += viewHeight;
            startX += viewWidth / 2;
        }
        else if (viewHeight == parentHeight && (lineStart == LEFT || lineStart == RIGHT)) {
            if (lineStart == RIGHT) startX += viewWidth;
            startY += viewHeight / 2;
        }

        transformedLine = drawLine(startX, startY);
        setRotateLine(transformedLine, rotateDegrees);
        transformedShape = drawShape(startX, startY);
//        setMatrixShape(transformedShape, rotateDegrees);

        paint.setStrokeWidth(lineSize);
        paint.setColor(lineColor);

        Log.d("degrees", String.valueOf(rotateDegrees));
        Log.d("startX | startY", String.format("X: %.2f Y: %d", startX, 0));
    }


    protected void setScaleType() {
        if (resource != 0) {
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
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawPath(transformedLine, paint);
        canvas.clipPath(transformedShape);
        super.onDraw(canvas);
        canvas.drawPath(transformedLine, paint);

//        transformedLine.close();
//        transformedShape.close();

        Log.d("CollageImageView", "onDraw");
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

    public Path drawLine(float x, float y) {
        Path line = new Path();
        Matrix matrix = new Matrix();

        float lineScaleY = (viewWidth + translateX) / (viewHeight + translateY) * 2;
        float centerY = viewHeight / 2;
        initializeLine(line);

        if (lineStart == RIGHT || lineStart == LEFT) {
            matrix.setRotate(-90);
            matrix.postTranslate(0, y);
        }
        else matrix.setTranslate(x, 0);

        matrix.preScale(1, lineScaleY, 0, centerY);
        line.transform(matrix);

        PathMeasure pathMeasure = new PathMeasure(line, false);
        pathMeasure.getPosTan(pathMeasure.getLength(), lineCoords, null);

        Log.d("coords", lineCoords[0] + " " + lineCoords[1]);
        return line;
    }

    public Path drawShape(float x, float y) {
        Path shape = new Path();
        Matrix matrix = new Matrix();
        float[] shapeCoords = {1, 1, 1, 1};

        initializeShape(shape);

        switch (parentSide) {
            case LEFT:
                shapeCoords[0] = x / viewWidth;
                shapeCoords[2] = 0;
                break;
            case RIGHT:
                shapeCoords[0] = 1 - x / viewWidth;
                shapeCoords[2] = viewWidth;
                break;
            case TOP:
                shapeCoords[1] = y / viewHeight;
                shapeCoords[3] = 0;
                break;
            case BOTTOM:
                shapeCoords[1] = 1 - y / viewHeight;
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
        float px = (lineStart == LEFT) ? 0 : viewWidth;
        float py = (lineStart == TOP) ? 0 : viewHeight;

        if (lineStart == TOP || lineStart == BOTTOM) {
            matrix.postRotate(degrees, lineCoords[0], viewHeight / 2);
        }
        else {
            matrix.postRotate(degrees, viewWidth / 2, lineCoords[1]);
        }

//        if (lineStart == TOP || lineStart == BOTTOM)
//            matrix.postRotate(degrees, lineCoords[0], py);
//        else
//            matrix.postRotate(degrees, px, lineCoords[1]);

        path.transform(matrix);
    }

    public void setMatrixShape(Path path, float degrees) {
        Matrix matrix = new Matrix();
        float px = lineCoords[0];
        float py = lineCoords[1];
        float radians = (float) Math.toRadians(degrees);
        float kx = (float) Math.tan(radians);
        if (degrees % 90 == 0) kx = 0;
        float scaleX = (float) Math.sqrt(viewWidth / viewHeight * 2 + kx * kx);
//
//        if (parentSide == RIGHT || parentSide == BOTTOM) scaleX = -scaleX;
//        if (lineStart == TOP || lineStart == BOTTOM) {
//            if (lineStart == TOP) py = 0;
//            matrix.setScale(scaleX, 1, px, py);
//            matrix.postSkew(-kx, 0, px, py);
//        }
//        else {
//            if (lineStart == LEFT) px = 0;
//            matrix.setScale(1, scaleX, px, py);
////            matrix.preTranslate(0, translateY);
//            matrix.postSkew(0, kx, px, py);
//        }

        if (parentSide == LEFT) {
            if (degrees % 90 == 0) {
                if (degrees < 0) {
                    for (float i = degrees; i < 0; i += 90)
                        matrix.preRotate(-90, lineCoords[0], viewHeight / 2);
                }
                else {
                    for (float i = 0; i < degrees; i += 90)
                        matrix.preRotate(90, lineCoords[0], viewHeight / 2);
                }
            }
            else if (degrees > 90 || degrees < -90) {
                matrix.preScale(-scaleX, scaleX, lineCoords[0], 0);
            }
            matrix.preScale(-scaleX, scaleX, lineCoords[0], viewHeight / 2);
            matrix.postSkew(-kx, 0, lineCoords[0], viewHeight / 2);
        }

        path.transform(matrix);
        Log.d("kx", String.valueOf(kx));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
        if (oldw != w || oldh != h) {
            setLineStart(lineStart);
//            setScaleType();
            init();
        }
        Log.d("viewSizeX", " " + viewWidth);
        Log.d("viewSizeY", " " + viewHeight);
        Log.d("CollageImageView", "onSizeChanged");
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


        Log.d("parentX", String.valueOf(parentWidth));
        Log.d("parentY", String.valueOf(parentHeight));
        Log.d("bitmapSize", String.format("w: %d  h: %d", bitmapWidth, bitmapHeight));
        Log.d("CollageImageView", "onMeasure");
    }

    protected boolean setViewSize() {
        if (resource == 0) return false;

        dec = BitmapFactory.decodeResource(getResources(), resource);
        bitmapWidth = dec.getWidth();
        bitmapHeight = dec.getHeight();
        return true;
    }

    public void setLineStart(int lineStart) {
        if (lineStart == TOP || lineStart == BOTTOM) translateY = 0;
        else translateX = 0;
    }

    private float getCorrectDegrees() {
        float degrees = rotateDegrees;
        if (degrees >= 90) degrees -= 90;
        else if (degrees <= -90) degrees += 90;
        return degrees;
    }

}

