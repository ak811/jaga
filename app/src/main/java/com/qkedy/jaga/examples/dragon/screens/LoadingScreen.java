package com.qkedy.jaga.examples.dragon.screens;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff;
import android.util.Log;

import com.qkedy.jaga.examples.dragon.utils.Assets;
import com.qkedy.jaga.interfaces.Game;
import com.qkedy.jaga.interfaces.Graphics;
import com.qkedy.jaga.interfaces.Screen;

public class LoadingScreen extends Screen {

    private final String TAG = LoadingScreen.class.getSimpleName();

    public LoadingScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        Log.w(TAG, "update");

        Graphics g = game.getGraphics();

        Assets.menu = g.newImage("menu.png", Config.ARGB_8888);
        Assets.background = g.newImage("background.png", Bitmap.Config.ARGB_8888);

        Assets.dragon_r = g.newImage("dragon_r.png", Config.ARGB_8888);
        Assets.dragon2_r = g.newImage("dragon2_r.png", Config.ARGB_8888);
        Assets.dragon3_r = g.newImage("dragon3_r.png", Config.ARGB_8888);
        Assets.dragonJump_r = g.newImage("jump_r.png", Config.ARGB_8888);
        Assets.dragonDown_r = g.newImage("down_r.png", Config.ARGB_8888);
        Assets.dragonRun_r = g.newImage("dragonrun_r.png", Config.ARGB_8888);
        Assets.dragonRun2_r = g.newImage("dragonrun2_r.png", Config.ARGB_8888);

        Assets.dragon_l = g.newImage("dragon_l.png", Config.ARGB_8888);
        Assets.dragon2_l = g.newImage("dragon2_l.png", Config.ARGB_8888);
        Assets.dragon3_l = g.newImage("dragon3_l.png", Config.ARGB_8888);
        Assets.dragonJump_l = g.newImage("jump_l.png", Config.ARGB_8888);
        Assets.dragonDown_l = g.newImage("down_l.png", Config.ARGB_8888);
        Assets.dragonRun_l = g.newImage("dragonrun_l.png", Config.ARGB_8888);
        Assets.dragonRun2_l = g.newImage("dragonrun2_l.png", Config.ARGB_8888);

        Assets.enemy = g.newImage("enemy.png", Config.ARGB_8888);
        Assets.enemy2 = g.newImage("enemy2.png", Config.ARGB_8888);

        Assets.tileDirt = g.newImage("tiledirt.png", Config.ARGB_8888);
        Assets.tileGrassTop = g.newImage("tilegrasstop.png", Config.ARGB_8888);
        Assets.tileGrassBot = g.newImage("tilegrassbot.png", Config.ARGB_8888);
        Assets.tileGrassLeft = g.newImage("tilegrassleft.png", Config.ARGB_8888);
        Assets.tileGrassRight = g.newImage("tilegrassright.png", Config.ARGB_8888);

        Assets.button = g.newImage("button.jpg", Config.ARGB_8888);
        Assets.winFlag = g.newImage("win_flag.png", Config.ARGB_8888);
        Assets.fireball_r = g.newImage("fireball_r.png", Config.ARGB_8888);
        Assets.fireball_l = g.newImage("fireball_l.png", Config.ARGB_8888);
        Assets.heart = g.newImage("heart.png", Bitmap.Config.ARGB_8888);

        Assets.jump = game.getAudio().createSound("jump_s.wav");
        Assets.shoot = game.getAudio().createSound("fireball_s.wav");

        game.setScreen(new MainMenuScreen(game));
    }

    @Override
    public void paint(float deltaTime) {
        Graphics g = game.getGraphics();
        g.drawImage(Assets.splash, 0, 0, 255, false, null, PorterDuff.Mode.ADD);
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