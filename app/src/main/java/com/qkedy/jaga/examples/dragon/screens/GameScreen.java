package com.qkedy.jaga.examples.dragon.screens;

import android.graphics.Color;
import android.graphics.Paint;

import com.qkedy.jaga.examples.dragon.models.Background;
import com.qkedy.jaga.examples.dragon.models.Dragon;
import com.qkedy.jaga.examples.dragon.models.Enemy;
import com.qkedy.jaga.examples.dragon.models.EnemyFire;
import com.qkedy.jaga.examples.dragon.models.Fireball;
import com.qkedy.jaga.examples.dragon.models.Tile;
import com.qkedy.jaga.examples.dragon.utils.Assets;
import com.qkedy.jaga.examples.dragon.utils.Constants;
import com.qkedy.jaga.interfaces.Game;
import com.qkedy.jaga.interfaces.Graphics;
import com.qkedy.jaga.interfaces.Image;
import com.qkedy.jaga.interfaces.Input;
import com.qkedy.jaga.interfaces.Input.TouchEvent;
import com.qkedy.jaga.interfaces.Screen;
import com.qkedy.jaga.models.Animation;
import com.qkedy.jaga.models.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.qkedy.jaga.examples.dragon.utils.Utils.inBounds;

public class GameScreen extends Screen {

    // Constants are Here
    public enum GameState {
        Ready, Running, Paused, GameOver, Win
    }

    public static GameState state = GameState.Ready;

    private static Background background;
    public static Dragon dragon;
    public static ArrayList<Enemy> enemyList = new ArrayList<>();

    private Image currentSprite;

    private Animation dragonAnim_r, enemyAnim, dragonRunAnim_r;

    private Animation dragonAnim_l, dragonRunAnim_l;

    public static ArrayList<Tile> tileArray = new ArrayList<Tile>();

    int livesLeft = 1;
    Paint textStyle, textStyle2, textStyle3;

    private Scanner scanner;

    public GameScreen(Game game, int lvl) {
        super(game);

        // Initialize game objects here. All objects here will be used in the game.
        // If you add new assets or characters to the game, be sure to include them in this list
        background = new Background(0, 0);
        dragon = new Dragon();

        dragonAnim_r = new Animation();
        dragonAnim_r.addFrame(Assets.dragon_r, 150);
        dragonAnim_r.addFrame(Assets.dragon2_r, 25);
        dragonAnim_r.addFrame(Assets.dragon3_r, 25);
        dragonAnim_r.addFrame(Assets.dragon2_r, 25);

        dragonAnim_l = new Animation();
        dragonAnim_l.addFrame(Assets.dragon_l, 150);
        dragonAnim_l.addFrame(Assets.dragon2_l, 25);
        dragonAnim_l.addFrame(Assets.dragon3_l, 25);
        dragonAnim_l.addFrame(Assets.dragon2_l, 25);

        dragonRunAnim_r = new Animation();
        dragonRunAnim_r.addFrame(Assets.dragonRun_r, 25);
        dragonRunAnim_r.addFrame(Assets.dragonRun2_r, 25);

        dragonRunAnim_l = new Animation();
        dragonRunAnim_l.addFrame(Assets.dragonRun_l, 25);
        dragonRunAnim_l.addFrame(Assets.dragonRun2_l, 25);

        enemyAnim = new Animation();
        enemyAnim.addFrame(Assets.enemy, 100);
        enemyAnim.addFrame(Assets.enemy2, 100);

        currentSprite = dragonAnim_r.getImage();

        loadMap(lvl);

        // Defining a textStyle object. This lines of code are used in order to project the background.
        textStyle = new Paint();
        textStyle.setTextSize(30);
        textStyle.setTextAlign(Paint.Align.CENTER);
        textStyle.setAntiAlias(true);
        textStyle.setColor(Color.WHITE);

        textStyle2 = new Paint();
        textStyle2.setTextSize(100);
        textStyle2.setTextAlign(Paint.Align.CENTER);
        textStyle2.setAntiAlias(true);
        textStyle2.setColor(Color.WHITE);

        textStyle3 = new Paint();
        textStyle3.setTextSize(35);
        textStyle3.setTextAlign(Paint.Align.CENTER);
        textStyle3.setAntiAlias(true);
        textStyle3.setColor(Color.WHITE);
    }

    // This section of code defines a method that allows us to load the map_1 that is needed.

    private void loadMap(int lvl) {
        ArrayList lines = new ArrayList();
        int width = 0;
        int height = 0;

        switch (lvl) {
            case 1:
                scanner = new Scanner(DragonGame.map_1);
                break;
            case 2:
                scanner = new Scanner(DragonGame.map_2);
                break;
        }

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            // no more lines to read
            if (line == null) {
                break;
            }
            if (!line.startsWith("!")) {
                lines.add(line);
                width = Math.max(width, line.length());
            }
        }
        height = lines.size();
        for (int j = 0; j < height; j++) {
            String line = (String) lines.get(j);
            for (int i = 0; i < width; i++) {
                if (i < line.length()) {
                    char ch = line.charAt(i);
                    if (Character.getNumericValue(ch) == 1) {
                        enemyList.add(new Enemy(i * 40, j * 40));
                    } else {
                        Tile t = new Tile(i, j, Character.getNumericValue(ch));
                        tileArray.add(t);
                    }
                }
            }
        }

    }

    @Override
    public void update(float deltaTime) {
        List touchEvents = game.getInput().getTouchEvents();

        // We have four separate update methods in this example.
        // Depending on the state of the game, we call different update methods.
        // Refer to Unit 3's code. We did a similar thing without separating the
        // update methods.
        if (state == GameState.Ready)
            updateReady(touchEvents);
        if (state == GameState.Running)
            updateRunning(touchEvents, deltaTime);
        if (state == GameState.Paused)
            updatePaused(touchEvents);
        if (state == GameState.GameOver)
            updateGameOver(touchEvents);
        if (state == GameState.Win)
            updateWin(touchEvents);
    }

    private void updateReady(List touchEvents) {

        // This example starts with a "Ready" screen.
        // When the user touches the screen, the game begins.
        // state now becomes GameState.Running.
        // Now the updateRunning() method will be called!
        if (touchEvents.size() > 0)
            state = GameState.Running;
    }

    private void updateRunning(List touchEvents, float deltaTime) {

        // This is identical to the update() method from our Unit 2/3 game.
        // 1. All touch input is handled here:
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            Input.TouchEvent event = (TouchEvent) touchEvents.get(i);
            if (event.getType() == TouchEvent.TOUCH_DOWN) {
                // This group of code is used to define that are that you want to register the users input.
                if (inBounds(event, 0, 450, 100, 100)) {
                    dragon.jump();
                    currentSprite = dragonAnim_r.getImage();
                    dragon.setDucked(false);
                } else if (inBounds(event, 0, 550, 100, 100)) {
                    if (dragon.isReadyToFire()) {
                        dragon.shoot();
                    }
                } else if (inBounds(event, 0, 650, 100, 100)) {
                    if (dragon.isRoted())
                        currentSprite = Assets.dragonDown_l;
                    else
                        currentSprite = Assets.dragonDown_r;
                    dragon.duck();
                }

                if (inBounds(event, 0, 0, 50, 50)) {
                    pause();
                }
                if (event.getPoint().getX() > 960) {
                    // Move right.
                    dragon.moveRight();
                    dragon.setMovingRight(true);
                }
                if (event.getPoint().getX() < 960 && event.getPoint().getX() > 100) {
                    // Move Left.
                    dragon.moveLeft();
                    dragon.setMovingLeft(true);
                }
            }

            if (event.getType() == TouchEvent.TOUCH_UP) {

                if (inBounds(event, 0, 650, 100, 100)) {
                    currentSprite = dragonAnim_r.getImage();
                    dragon.setDucked(false);
                }
                if (event.getPoint().getX() > 960) {
                    // Move right.
                    dragon.stopRight();
                }
                if (event.getPoint().getX() < 960 && event.getPoint().getX() > 100) {
                    // Move left.
                    dragon.stopLeft();
                }
            }

        }


        // 2. Check miscellaneous events like death:
        if (livesLeft == 0 || dragon.health == 0 || dragon.getCenterY() > 1090) {
            state = GameState.GameOver;
        }

        // 3. Call individual update() methods here.
        // This is where all the game updates happen.
        dragon.update(deltaTime);
        if (dragon.isJumped()) {
            if (dragon.isRoted())
                currentSprite = Assets.dragonJump_l;
            else
                currentSprite = Assets.dragonJump_r;
        } else if (dragon.isMovingRight())
            currentSprite = dragonRunAnim_r.getImage();
        else if (dragon.isMovingLeft())
            currentSprite = dragonRunAnim_l.getImage();
        else if (!dragon.isJumped() && !dragon.isDucked() && !dragon.isRoted())
            currentSprite = dragonAnim_r.getImage();
        else if (!dragon.isJumped() && !dragon.isDucked() && dragon.isRoted())
            currentSprite = dragonAnim_l.getImage();

        // Fires.
        ArrayList fireball = dragon.getFireballs();
        for (int i = 0; i < fireball.size(); i++) {
            Fireball f = (Fireball) fireball.get(i);
            if (f.isVisible()) {
                f.update(deltaTime);
            } else {
                fireball.remove(i);
            }
        }

        for (int e = 0; e < enemyList.size(); e++) {
            ArrayList fire = enemyList.get(e).getFire();
            for (int i = 0; i < fire.size(); i++) {
                EnemyFire ef = (EnemyFire) fire.get(i);
                if (ef.isVisible()) {
                    ef.update(deltaTime);
                } else {
                    fire.remove(i);
                }
            }
        }

        updateTiles(deltaTime);
        for (int e = 0; e < enemyList.size(); e++) {
            enemyList.get(e).update(deltaTime);
        }
        background.update(deltaTime);
        animate(deltaTime);

    }

    private void updatePaused(List touchEvents) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = (TouchEvent) touchEvents.get(i);
            if (event.getType() == TouchEvent.TOUCH_UP) {
                if (inBounds(event, 760, 460, 440, 150)) {
                    resume();
                }
                if (inBounds(event, 760, 620, 440, 150)) {
                    nullify();
                    goToMenu();
                }
            }
        }
    }

    private void updateGameOver(List touchEvents) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = (TouchEvent) touchEvents.get(i);
            if (event.getType() == TouchEvent.TOUCH_DOWN) {
                if (inBounds(event, 0, 0, 1920, 1080)) {
                    nullify();
                    goToMenu();
                    return;
                }
            }
        }
    }

    private void updateWin(List touchEvents) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = (TouchEvent) touchEvents.get(i);
            if (event.getType() == TouchEvent.TOUCH_DOWN) {
                if (inBounds(event, 0, 0, 1920, 1080)) {
                    nullify();
                    goToMenu();
                    return;
                }
            }
        }
    }

    private void updateTiles(float deltaTime) {
        for (int i = 0; i < tileArray.size(); i++) {
            Tile t = (Tile) tileArray.get(i);
            t.update(deltaTime);
        }
    }

    @Override
    public void paint(float deltaTime) {
        Graphics g = game.getGraphics();

        g.drawImage(Assets.background, background.getBgX(), background.getBgY(),
                Constants.MAX_ALPHA, false, null, null);
        paintTiles(g);

        ArrayList fireball = dragon.getFireballs();
        for (int i = 0; i < fireball.size(); i++) {
            Fireball p = (Fireball) fireball.get(i);
            if (((Fireball) fireball.get(i)).getSpeedX() < 0)
                g.drawImage(Assets.fireball_l, p.getX(), p.getY(),
                        Constants.MAX_ALPHA, false, null, null);
            else
                g.drawImage(Assets.fireball_r, p.getX(), p.getY(),
                        Constants.MAX_ALPHA, false, null, null);
        }
        // First draw the game elements.

        for (int e = 0; e < enemyList.size(); e++) {
            ArrayList fire = enemyList.get(e).getFire();
            for (int i = 0; i < fire.size(); i++) {
                EnemyFire p = (EnemyFire) fire.get(i);
                g.drawRect(p.getX(), p.getY(), 20, 10, Color.GREEN, Constants.MAX_ALPHA);
            }
        }

        g.drawRect(Dragon.body, Color.GREEN, Constants.MAX_ALPHA);
        g.drawRect(Dragon.bodyLeft, Color.RED, Constants.MAX_ALPHA);
        g.drawRect(Dragon.bodyRight, Color.BLUE, Constants.MAX_ALPHA);
        g.drawRect(Dragon.footLeft, Color.BLACK, Constants.MAX_ALPHA);
        g.drawRect(Dragon.footRight, Color.WHITE, Constants.MAX_ALPHA);
        g.drawRect(Dragon.verticalBodyLine, Color.YELLOW, Constants.MAX_ALPHA);

        g.drawImage(currentSprite, dragon.getCenterX() - 61,
                dragon.getCenterY() - 63, Constants.MAX_ALPHA, false, null, null);

        for (int e = 0; e < enemyList.size(); e++) {
            g.drawImage(enemyAnim.getImage(), enemyList.get(e).getCenterX() - 48,
                    enemyList.get(e).getCenterY() - 48, Constants.MAX_ALPHA,
                    false, null, null);
        }

        // Secondly, draw the UI above the game elements.
        if (state == GameState.Ready)
            drawReadyUI();
        if (state == GameState.Running)
            drawRunningUI();
        if (state == GameState.Paused)
            drawPausedUI();
        if (state == GameState.GameOver)
            drawGameOverUI();
        if (state == GameState.Win)
            drawGameWin();
    }

    private void paintTiles(Graphics g) {
        for (int i = 0; i < tileArray.size(); i++) {
            Tile t = tileArray.get(i);
            if (t.type != 0) {
                g.drawImage(t.getTileImage(), t.getTileX(),
                        t.getTileY(), Constants.MAX_ALPHA, false, null, null);
            }
        }
    }

    public void animate(float deltaTime) {
        dragonAnim_r.update(10, deltaTime);
        dragonRunAnim_r.update(10, deltaTime);
        dragonAnim_l.update(10, deltaTime);
        dragonRunAnim_l.update(10, deltaTime);
        enemyAnim.update(50, deltaTime);
    }

    private void nullify() {

        // Set all variables to null. You will be recreating them in the
        // constructor.
        textStyle = null;
        background = null;
        dragon = null;
        enemyList.clear();
        currentSprite = null;
        dragonAnim_r = null;
        dragonAnim_l = null;
        dragonRunAnim_r = null;
        dragonRunAnim_l = null;
        enemyAnim = null;
        tileArray.clear();
        state = GameState.Ready;

        // Call garbage collector to clean up memory.
        System.gc();
    }

    private void drawReadyUI() {
        Graphics g = game.getGraphics();
        g.drawARGB(155, 0, 0, 0);
        g.drawString("Tap to Start.", new Point(980, 580), textStyle2, Constants.MAX_ALPHA);
    }

    private void drawRunningUI() {
        Graphics g = game.getGraphics();
//        g.drawImage(Assets.button, 0, 450, 0, 0, 100, 100);
//        g.drawImage(Assets.button, 0, 550, 0, 100, 100, 100);
//        g.drawImage(Assets.button, 0, 650, 0, 200, 100, 100);
//        g.drawImage(Assets.button, 0, 0, 0, 300, 50, 50);
//        g.drawString("Health Left: ", 250, 50, textStyle3);
        if (livesLeft != 0 && dragon.health != 0) {
            for (int i = 1; i <= dragon.health; i++) {
                g.drawImage(Assets.heart, 330 + (i * 45),
                        22, Constants.MAX_ALPHA, false, null, null);
            }
        }
    }

    private void drawPausedUI() {
        Graphics g = game.getGraphics();
        // Darken the entire screen so you can display the Paused screen.
        g.drawARGB(125, 0, 0, 0);
        g.drawString("Resume", new Point(980, 540), textStyle2, Constants.MAX_ALPHA);
        g.drawString("Menu", new Point(980, 720), textStyle2, Constants.MAX_ALPHA);
    }

    private void drawGameOverUI() {
        Graphics g = game.getGraphics();
        g.drawRect(0, 0, 1920, 1090, Color.BLACK, Constants.MAX_ALPHA);
        g.drawString("GAME OVER.", new Point(980, 540), textStyle, Constants.MAX_ALPHA);
        g.drawString("Tap to return.", new Point(980, 640), textStyle, Constants.MAX_ALPHA);
    }

    private void drawGameWin() {
        Graphics g = game.getGraphics();
        g.drawARGB(125, 100, 60, 80);
        g.drawString("YOU WIN.", new Point(980, 540), textStyle2, Constants.MAX_ALPHA);
        g.drawString("Tap to return.", new Point(980, 640), textStyle, Constants.MAX_ALPHA);
    }

    @Override
    public void pause() {
        if (state == GameState.Running)
            state = GameState.Paused;
    }

    @Override
    public void resume() {
        if (state == GameState.Paused)
            state = GameState.Running;
    }

    @Override
    public void stop() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void backButton() {
        pause();
    }

    private void goToMenu() {
        // TODO Auto-generated method stub
        game.setScreen(new MainMenuScreen(game));
    }

    public static Background getBackground() {
        // TODO Auto-generated method stub
        return background;
    }

    public static Dragon getDragon() {
        // TODO Auto-generated method stub
        return dragon;
    }

    public static ArrayList<Enemy> getEnemy() {
        // TODO Auto-generated method stub
        return enemyList;
    }

    public static ArrayList<Tile> getTile() {
        // TODO Auto-generated method stub
        return tileArray;
    }

}