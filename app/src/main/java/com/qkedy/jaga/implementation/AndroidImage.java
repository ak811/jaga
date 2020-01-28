package com.qkedy.jaga.implementation;

import android.graphics.Bitmap;

import com.qkedy.jaga.interfaces.Image;

import static android.graphics.Bitmap.Config;

public class AndroidImage implements Image {

    private Bitmap bitmap;
    private Config format;

    public AndroidImage(Bitmap bitmap, Config format) {
        this.bitmap = bitmap;
        this.format = format;
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public int getWidth() {
        return bitmap.getWidth();
    }

    @Override
    public int getHeight() {
        return bitmap.getHeight();
    }

    @Override
    public Config getFormat() {
        return format;
    }

    /**
     * @apiNote Free the native object associated with this bitmap, and clear the reference to the pixel data.
     */
    @Override
    public void dispose() {
        bitmap.recycle();
    }
}
