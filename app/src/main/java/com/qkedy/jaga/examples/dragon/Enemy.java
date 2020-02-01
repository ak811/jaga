package com.qkedy.jaga.examples.dragon;

import android.graphics.Rect;

import com.qkedy.jaga.models.Background;

import java.util.ArrayList;

public class Enemy {

    // Constants are Here
    private int power, centerX, speedX, centerY;
    private int fire_timer = 30;
    private Background bg = GameScreen.getBackground();
    private Dragon dragon = GameScreen.getDragon();

    public Rect enemy = new Rect(0, 0, 0, 0);
    public int health = 5;

    private int movementSpeed;

    private ArrayList<EnemyFire> fire = new ArrayList<EnemyFire>();

    // Create Enemy
    public Enemy(int centerX, int centerY) {
        setCenterX(centerX);
        setCenterY(centerY);
    }

    // Behavioral Methods
    public void update(float deltaTime) {
        follow();
        centerX += speedX;
        speedX = bg.getSpeedX() * 5 + movementSpeed;
        enemy.set(centerX - 55, centerY - 20, centerX + 125, centerY + 130);

        fire_timer--;

        if (Rect.intersects(enemy, Dragon.body)) {
            checkCollision();
        }

        for (int t=0; t<GameScreen.getTile().size();t++)
            if ((Rect.intersects(enemy, GameScreen.getTile().get(t).getTileRect())) && (GameScreen.getTile().get(t).getTileType() != 0)) {
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
        fire.add(p);
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

    public ArrayList<EnemyFire> getFire() {
        return fire;
    }

}