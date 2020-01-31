package com.qkedy.jaga.examples.dragon;

import android.graphics.Bitmap;
import android.graphics.ImageFormat;

import com.qkedy.jaga.interfaces.Game;
import com.qkedy.jaga.interfaces.Graphics;
import com.qkedy.jaga.interfaces.Screen;

public class SplashLoadingScreen extends Screen {

    public SplashLoadingScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        Assets.splash = g.newImage("splash.png", Bitmap.Config.ARGB_8888);
        game.setScreen(new LoadingScreen(game));
    }

    @Override
    public void paint(float deltaTime) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void backButton() {

    }

}