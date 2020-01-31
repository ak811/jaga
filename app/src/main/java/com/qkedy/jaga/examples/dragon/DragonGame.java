package com.qkedy.jaga.examples.dragon;

import android.util.Log;

import com.qkedy.jaga.implementation.AndroidGame;
import com.qkedy.jaga.interfaces.Screen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DragonGame extends AndroidGame {

    // Constants are Here
    public static String map_1;
    public static String map_2;
    boolean firstTimeCreate = true;

    @Override
    public Screen getInitScreen() {
        if (firstTimeCreate) {
            Assets.load(this);
            firstTimeCreate = false;
        }
//        InputStream lvl_1 = getResources().openRawResource(R.raw.map1);
//        map_1 = convertStreamToString(lvl_1);
//        InputStream lvl_2 = getResources().openRawResource(R.raw.map2);
//        map_2 = convertStreamToString(lvl_2);
//        return new SplashLoadingScreen(this);
        return null;
    }

    //handles the actions designated to happen when the user presses the back key
    @Override
    public void onBackPressed() {
        getCurrentScreen().backButton();
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append((line + "\n"));
            }
        } catch (IOException e) {
            Log.w("LOG", e.getMessage());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.w("LOG", e.getMessage());
            }
        }
        return sb.toString();
    }

    // resumes the program after the user has re-opened it
    @Override
    public void onResume() {
        super.onResume();
        Assets.music.play();
    }

    // handles the actions needed for when the program is minimized
    @Override
    public void onPause() {
        super.onPause();
        Assets.music.pause();
    }

}