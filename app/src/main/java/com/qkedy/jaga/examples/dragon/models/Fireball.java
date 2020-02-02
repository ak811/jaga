package com.qkedy.jaga.examples.dragon.models;

import android.graphics.Rect;

import com.qkedy.jaga.examples.dragon.screens.GameScreen;

import java.util.ArrayList;

public class Fireball {

    // Constants are Here
    private int x, y, speedX;
    private boolean visible;

    private Rect fireball;
    private ArrayList<Enemy> enemy = GameScreen.getEnemy();
    private Dragon dragon = GameScreen.getDragon();

    public Fireball(int startX, int startY) {
        x = startX;
        y = startY;
        if (dragon.isRoted())
            speedX = -25;
        else
            speedX = 25;

        visible = true;
        fireball = new Rect(0, 0, 0, 0);
    }

    public void update(float deltaTime) {
        x += speedX;
        fireball.set(x, y, x + 10, y + 5);
        if (x > 1920) {
            visible = false;
            fireball = null;
        }
        if (x < 1920) {
            checkCollision();
        }
    }

    private void checkCollision() {
        for (int e = 0; e < enemy.size(); e++) {
            if (Rect.intersects(fireball, enemy.get(e).enemy)) {
                visible = false;
                if (enemy.get(e).health > 1) {
                    enemy.get(e).health -= 1;
                } else {
                    enemy.get(e).die();
                    enemy.remove(e);
                }
            }
        }
        for (int t=0; t<GameScreen.getTile().size();t++)
        if ((Rect.intersects(fireball, GameScreen.getTile().get(t).getTileRect())) && (GameScreen.getTile().get(t).getTileType() != 0)) {
            GameScreen.dragon.getFireballs().remove(this);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeedX() {
        return speedX;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Rect getFireball() {
        return fireball;
    }

    public void setFireball(Rect fireball) {
        this.fireball = fireball;
    }
}