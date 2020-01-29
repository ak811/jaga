package com.qkedy.jaga.utils;

import android.graphics.Paint;
import android.graphics.Typeface;

import com.qkedy.jaga.implementation.AndroidGame;

public class Assets {

    public static Paint paint;

    public static void loadMusic() {

    }

    public static void loadSound() {

    }

    public static void loadPaint(AndroidGame androidGame) {
        paint = new Paint();
        try {
            paint = new Paint();
            String path = "fonts/berlin.ttf";
            Typeface plain = Typeface.createFromAsset(androidGame.getAssets(), path);
            Typeface bold = Typeface.create(plain, Typeface.BOLD);
            paint.setAntiAlias(true);
            paint.setTypeface(bold);
            paint.setTextAlign(Paint.Align.CENTER);
        } catch (Exception e) {
            System.out.println("Font Exception");
        }
    }

}
