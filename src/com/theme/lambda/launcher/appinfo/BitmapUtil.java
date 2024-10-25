package com.theme.lambda.launcher.appinfo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtil {

    public static void drawableToFile(Drawable drawable, String filePath, Bitmap.CompressFormat format) {
        if (drawable == null)
            return;
        try {
            File file = new File(filePath);

            if (file.exists()) {
                return;
            }

            if (!file.exists())
                file.createNewFile();

            FileOutputStream out = null;
            out = new FileOutputStream(file);
            drawableToBitmap(drawable).compress(format, 100, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }
}
