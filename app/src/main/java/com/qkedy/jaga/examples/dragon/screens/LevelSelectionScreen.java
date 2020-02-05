package com.qkedy.jaga.examples.dragon.screens;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.qkedy.jaga.interfaces.Game;
import com.qkedy.jaga.interfaces.Graphics;
import com.qkedy.jaga.interfaces.Input;
import com.qkedy.jaga.interfaces.Screen;
import com.qkedy.jaga.models.Point;

import java.util.List;

import static com.qkedy.jaga.examples.dragon.utils.Utils.inBounds;

public class LevelSelectionScreen extends Screen {

    private final String TAG = LevelSelectionScreen.class.getSimpleName();

    private Paint textStyle2;

    LevelSelectionScreen(Game game) {
        super(game);
        textStyle2 = new Paint();
        textStyle2.setTextSize(100);
        textStyle2.setTextAlign(Paint.Align.CENTER);
        textStyle2.setAntiAlias(true);
        textStyle2.setColor(Color.WHITE);
    }

    @Override
    public void update(float deltaTime) {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents.get(i);
            if (event.getType() == Input.TouchEvent.TOUCH_UP) {
                if (inBounds(event, 760, 360, 440, 150)) {
                    game.setScreen(new GameScreen(game, 1));
                } else if (inBounds(event, 760, 560, 440, 150)) {
                    game.setScreen(new GameScreen(game, 2));
                }
            }
        }
    }

    @Override
    public void paint(float deltaTime) {
        Graphics g = game.getGraphics();
        g.drawRect(new Point(0, 0), 1920, 1090, Color.GRAY, 255);
        g.drawString("lvl 1", new Point(980, 440), textStyle2, 255);
        g.drawString("lvl 2", 980, 640, textStyle2, 255);
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
        game.setScreen(new MainMenuScreen(game));
    }
}
