package com.qkedy.jaga.examples.dragon.screens;

import android.graphics.PorterDuff;

import com.qkedy.jaga.examples.dragon.utils.Assets;
import com.qkedy.jaga.interfaces.Game;
import com.qkedy.jaga.interfaces.Graphics;
import com.qkedy.jaga.interfaces.Input;
import com.qkedy.jaga.interfaces.Screen;

import java.util.List;

// the first line extends the screen variable and then begins the game process
public class MainMenuScreen extends Screen {
    public MainMenuScreen(Game game) {
        super(game);
    }


    // this block of code handles the events associated with the user touching the screen.
    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents.get(i);
            if (event.getType() == Input.TouchEvent.TOUCH_UP) {
                if (inBounds(event, 800, 810, 360, 200)) {
                    game.setScreen(new LevelSelectionScreen(game));
                }
            }
        }
    }

    // if the touch input is in the bounds of the designated area then it will be registered.
    private boolean inBounds(Input.TouchEvent event, int x, int y, int width,
                             int height) {
        if (event.getPoint().getX() > x && event.getPoint().getX() < x + width - 1
                && event.getPoint().getY() > y
                && event.getPoint().getY() < y + height - 1)
            return true;
        else
            return false;
    }

    @Override
    public void paint(float deltaTime) {
        Graphics g = game.getGraphics();
        g.drawImage(Assets.menu, 0, 0, 255, false, null, PorterDuff.Mode.ADD);
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
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
