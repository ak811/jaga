package com.qkedy.jaga.implementation;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

@SuppressLint("ViewConstructor")
public class AndroidFastRenderView extends View {
    private AndroidGame game;

    private int fps;
    private static long fpsStartTime;
    private long startTime;
    public static Canvas canvas;

    public AndroidFastRenderView(AndroidGame game) {
        super(game);
        this.game = game;
        fpsStartTime = System.currentTimeMillis();

        startTime = System.nanoTime();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        AndroidFastRenderView.canvas = canvas;

//        deltaTime is based on the Hundredth of a second (0.01)
        float deltaTime = (System.nanoTime() - startTime) / 10_000_000f;
        startTime = System.nanoTime();
        if (deltaTime > 3.15)
            deltaTime = 3.15f;

//        Log.e("deltaRime", deltaTime + "");
//        int deltaTime = 1;

        game.getCurrentScreen().update(deltaTime);
        game.getCurrentScreen().paint(deltaTime);

//        calculateFPS();

        invalidate();
    }

    public void resume() {

    }

    private void calculateFPS() {
        fps++;

        if (System.nanoTime() - fpsStartTime > 1_000_000_000) {
            Log.w("", "fps: " + fps);
            fps = 0;
            fpsStartTime = System.nanoTime();
        }
    }

    public void pause() {

    }

    public static Canvas getCanvas() {
        return canvas;
    }
}