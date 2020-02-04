package com.qkedy.jaga.examples.dragon.screens;

import android.util.Log;

import com.qkedy.jaga.R;
import com.qkedy.jaga.examples.dragon.utils.Assets;
import com.qkedy.jaga.implementation.AndroidGame;
import com.qkedy.jaga.interfaces.Screen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class MainActivity extends AndroidGame {

    private final String TAG = MainActivity.class.getSimpleName();

    public static String map_1;
    public static String map_2;
    boolean firstTimeCreate = true;

    @Override
    public Screen getInitScreen() {
        if (firstTimeCreate) {
            Assets.load(this);
            firstTimeCreate = false;
        }
        InputStream lvl_1 = getResources().openRawResource(R.raw.map1);
        map_1 = convertStreamToString(lvl_1);
        Log.w(TAG, "hello");
        InputStream lvl_2 = getResources().openRawResource(R.raw.map2);
        map_2 = convertStreamToString(lvl_2);
        return new SplashLoadingScreen(this);
    }

    @Override
    public void onBackPressed() {
        getCurrentScreen().backButton();
    }

    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null)
                sb.append(line).append("\n");
        } catch (IOException e) {
            Log.w("LOG", Objects.requireNonNull(e.getMessage()));
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.w("LOG", Objects.requireNonNull(e.getMessage()));
            }
        }
        return sb.toString();
    }

    @Override
    public void onResume() {
        super.onResume();
        Assets.music.play();
    }

    @Override
    public void onPause() {
        super.onPause();
        Assets.music.pause();
    }

}