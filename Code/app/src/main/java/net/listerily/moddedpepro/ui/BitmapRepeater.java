package net.listerily.moddedpepro.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class BitmapRepeater {
    private static Bitmap repeatW(int width, Bitmap src) {
        int count = (width + src.getWidth() - 1) / src.getWidth() + 1;
        Bitmap bitmap = Bitmap.createBitmap(width, src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        for (int idx = 0; idx < count; ++idx) {
            if (idx + 1 == count)
                canvas.drawBitmap(src, width, 0, null);
            else
                canvas.drawBitmap(src, idx * src.getWidth(), 0, null);
        }
        return bitmap;
    }

    private static Bitmap repeatH(int height, Bitmap src) {
        int count = (height + src.getHeight() - 1) / src.getHeight() + 1;
        Bitmap bitmap = Bitmap.createBitmap(src.getWidth(), height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        for (int idx = 0; idx < count; ++idx) {
            if (idx + 1 == count)
                canvas.drawBitmap(src, 0, height, null);
            else
                canvas.drawBitmap(src, 0, idx * src.getHeight(), null);
        }
        return bitmap;
    }

    public static Bitmap repeat(int width, int height, Bitmap src) {
        Bitmap ret = repeatW(width, src);
        ret = repeatH(height, ret);
        return ret;
    }
}