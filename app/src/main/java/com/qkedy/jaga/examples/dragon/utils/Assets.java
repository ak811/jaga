package com.qkedy.jaga.examples.dragon.utils;

import com.qkedy.jaga.examples.dragon.screens.MainActivity;
import com.qkedy.jaga.interfaces.Image;
import com.qkedy.jaga.interfaces.Music;
import com.qkedy.jaga.interfaces.Sound;

public class Assets {

    // Constants are Here
    public static Image menu, splash, background, button;

    public static Image dragon_r, dragon2_r, dragon3_r, dragonJump_r, dragonDown_r,
            dragonRun_r, dragonRun2_r;

    public static Image dragon_l, dragon2_l, dragon3_l, dragonJump_l, dragonDown_l,
            dragonRun_l, dragonRun2_l;

    public static Image enemy, enemy2;

    public static Image tileDirt, tileGrassTop, tileGrassBot, tileGrassLeft,
            tileGrassRight;

    public static Image winFlag, fireball_r, fireball_l, heart;

    public static Sound jump, shoot;

    public static Music music;

    public static void load(MainActivity mainActivity) {
        music = mainActivity.getAudio().createMusic("game_music.mp3");
        music.setLooping(true);
        music.setVolume(0.85f);
//        music.play();
    }

}
