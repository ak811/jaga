package com.qkedy.jaga.examples.dragon.models;

import android.graphics.Rect;

import com.qkedy.jaga.examples.dragon.screens.GameScreen;

import java.util.ArrayList;

public class Enemy {

    private int power, centerX, speedX, centerY;
    private int fire_timer = 30;
    private Background bg = GameScreen.getBackground();
    private Dragon dragon = GameScreen.getDragon();

    private Rect rect = new Rect(0, 0, 0, 0);
    private int health = 5;

    private int movementSpeed;

    private ArrayList<EnemyFire> fires = new ArrayList<>();

    public Enemy(int centerX, int centerY) {
        setCenterX(centerX);
        setCenterY(centerY);
    }

    public void update(float deltaTime) {
        follow();

        centerX += speedX;
        speedX = bg.getSpeedX() * 5 + movementSpeed;
        rect.set(centerX - 55, centerY - 20, centerX + 125, centerY + 130);

        fire_timer--;

        if (Rect.intersects(rect, Dragon.body)) {
            checkCollision();
        }

        for (int t = 0; t < GameScreen.getTile().size(); t++)
            if ((Rect.intersects(rect, GameScreen.getTile().get(t).getTileRect()))
                    && (GameScreen.getTile().get(t).getTileType() != 0)) {
                centerY -= 5;
            }
    }

    private void checkCollision() {
        centerX = dragon.getCenterX() + 160;
    }

    public void follow() {
        if (centerX < -10 || centerX > 1920) {
            movementSpeed = 0;
        } else if (dragon.getCenterX() + 150 <= centerX) {
            movementSpeed = -10;
            if (fire_timer <= 0) {
                attack();
                fire_timer = 30;
            }
        } else {
            if (dragon.getCenterX() + 150 >= centerX) {
                movementSpeed = 10;
            } else {
                movementSpeed = 0;
            }
        }
    }

    public void die() {
    }

    public void attack() {
        EnemyFire p = new EnemyFire(centerX - 52, centerY + 52);
        fires.add(p);
    }

    public int getPower() {
        return power;
    }

    public int getSpeedX() {
        return speedX;
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public Background getBg() {
        return bg;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public void setBg(Background bg) {
        this.bg = bg;
    }

    public ArrayList<EnemyFire> getFires() {
        return fires;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }
}