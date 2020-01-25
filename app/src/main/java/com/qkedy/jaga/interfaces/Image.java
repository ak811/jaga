package com.qkedy.jaga.interfaces;

import android.graphics.Bitmap;

import static android.graphics.Bitmap.Config;

public interface Image {

    /**
     * @return the bitmap of the image
     */
    Bitmap getBitmap();

    /**
     * @return the scaled width of the image
     */
    int getWidth();

    /**
     * @return the scaled height of the image
     */
    int getHeight();

    /**
     * @return the format of the image
     * some supported formats: ALPHA_8, RGB_565
     * @deprecated formats: ARGB_4444, ARGB_8888, RGBA_F16, HARDWARE
     */
    Config getFormat();

    /**
     * @apiNote recycle the bitmap
     */
    void dispose();
}
