package com.myapp.autogallery.items;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageConverter {

    public static void convertPngToJpg(Context context, int drawableId, File jpgFile) {
        Bitmap pngBitmap = BitmapFactory.decodeResource(context.getResources(), drawableId);
        Bitmap jpgBitmap = Bitmap.createBitmap(pngBitmap.getWidth(), pngBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(jpgBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(jpgBitmap, 0,0, new Paint());

        try (FileOutputStream out = new FileOutputStream(jpgFile)) {
            jpgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            System.out.println("Конвертация завершена успешно!");
        } catch (IOException e) {
            System.err.println("Ошибка при конвертации изображения: " + e.getMessage());
        }

        pngBitmap.recycle();
        jpgBitmap.recycle();
    }
}
