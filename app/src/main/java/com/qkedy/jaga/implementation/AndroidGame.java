package com.qkedy.jaga.implementation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.qkedy.jaga.interfaces.Audio;
import com.qkedy.jaga.interfaces.FileIO;
import com.qkedy.jaga.interfaces.Game;
import com.qkedy.jaga.interfaces.Graphics;
import com.qkedy.jaga.interfaces.Input;
import com.qkedy.jaga.interfaces.Screen;
import com.qkedy.jaga.utils.Constants;

import org.json.JSONException;

public abstract class AndroidGame extends Activity implements Game {

    public static AndroidFastRenderView renderView;
    private Graphics graphics;
    public static Audio audio;
    public static Input input;
    private FileIO fileIO;
    private Screen screen;
    private WakeLock wakeLock;

    protected Activity activity;

    public static float scaleX;
    public static float scaleY;

    public static AssetManager assetManager;

    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;

        Bitmap bitmap = null;

        makeActivityFullScreen();
        hideSoftButtons();

        int deviceScreenWidth = getWindowManager().getDefaultDisplay().getWidth();
        int deviceScreenHeight = getWindowManager().getDefaultDisplay().getHeight();

        int bitmapWidth = Constants.SCREEN_WIDTH;
        int bitmapHeight = Constants.SCREEN_HEIGHT;

        float touchScaleX = (float) bitmapWidth / deviceScreenWidth;
        float touchScaleY = (float) bitmapHeight / deviceScreenHeight;

        scaleX = deviceScreenWidth / (float) bitmapWidth;
        scaleY = deviceScreenHeight / (float) bitmapHeight;

        renderView = new AndroidFastRenderView(this);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        renderView.setLayerType(View.LAYER_TYPE_HARDWARE, paint);
        graphics = new AndroidGraphics(getAssets(), bitmap);
        fileIO = new AndroidFileIO(this);
        audio = new AndroidAudio(this);
        input = new AndroidInput(this, renderView, touchScaleX, touchScaleY);
        try {
            screen = getInitScreen();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setContentView(renderView);

        /** @apiNote for controlling power management,
         * including "wake locks," which let you keep the device on, while
         * you're RUNNING long tasks.
         */
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyGame");
        }
    }

    private void makeActivityFullScreen() {
        // flag for the "no title" feature, turning off the title at the top of the screen.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // full screen activity
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onResume() {
        super.onResume();
        //avoiding screen from sleep
        wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/);
        screen.resume();
        //start a new Thread for view rendering
        renderView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        wakeLock.release();
        renderView.pause();
        screen.pause();

        if (isFinishing())
            screen.dispose();
    }

    @Override
    protected void onStop() {
        super.onStop();

        screen.stop();
    }

    @Override
    public Input getInput() {
        return input;
    }

    @Override
    public FileIO getFileIO() {
        return fileIO;
    }

    @Override
    public Graphics getGraphics() {
        return graphics;
    }

    @Override
    public Audio getAudio() {
        return audio;
    }

    @Override
    public void setScreen(Screen screen) {
        if (screen == null)
            throw new IllegalArgumentException("Screen must not be null");

        this.screen.pause();
        this.screen.dispose();
        screen.resume();
        screen.update(0);
        this.screen = screen;
    }

    public Screen getCurrentScreen() {
        return screen;
    }

    public Activity getActivity() {
        return activity;
    }

    private void hideSoftButtons() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
}