package com.qkedy.jaga.examples.dragon.models;

import android.graphics.Rect;

import com.qkedy.jaga.examples.dragon.utils.Assets;
import com.qkedy.jaga.models.Background;

import java.util.ArrayList;

public class Dragon {

    // Constants are Here
    final int JUMPSPEED = -30;
    final int MOVESPEED = 30;

    public int health = 25;

    private int centerX = 310;
    private int centerY = 800;
    private boolean jumped = false;
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private boolean ducked = false;
    private boolean readyToFire = true;
    private boolean roted = false;

    private int speedX = 0;
    private int speedY = 0;

    public static Rect verticalBodyLine = new Rect(0, 0, 0, 0);
    public static Rect body = new Rect(0, 0, 0, 0);
    public static Rect bodyLeft = new Rect(0, 0, 0, 0);
    public static Rect bodyRight = new Rect(0, 0, 0, 0);
    public static Rect footLeft = new Rect(0, 0, 0, 0);
    public static Rect footRight = new Rect(0, 0, 0, 0);

//    private Background background = GameScreen.getBackground();

//    private ArrayList<Fireball> fireballs = new ArrayList<Fireball>();

    public void update(float deltaTime) {

        // Moves Character or Scrolls Background accordingly.
        if (speedX == 0) {
//            background.setSpeedX(0);
        } else if (centerX <= 800 && centerX >= 300) {
            centerX += speedX;
        } else if (speedX < 0 && centerX > 800) {
            centerX += speedX;
        } else if (speedX > 0 && centerX < 300) {
            centerX += speedX;
        } else if (speedX > 0 && centerX > 800) {
//            background.setSpeedX((int) (-MOVESPEED / 5));
        } else if (speedX < 0 && centerX < 300) {
//            background.setSpeedX((int) (MOVESPEED / 5));
        }

        // Updates Y Position
        centerY += speedY;

        // Handles Jumping
        speedY += 2;
        if (speedY > 4) {
            jumped = true;
        }

        // Dragon Body Parts
        verticalBodyLine.set(centerX - 10, centerY - 60, centerX + 30, centerY + 130);
        body.set(centerX - 55, centerY - 60, centerX + 125, centerY + 130);
        bodyLeft.set(centerX - 55, verticalBodyLine.top + 60, centerX - 25, verticalBodyLine.bottom - 60);
        bodyRight.set(centerX + 80, verticalBodyLine.top + 60, centerX + 110, verticalBodyLine.bottom - 60);
        footLeft.set(centerX - 10, verticalBodyLine.bottom - 50, centerX + 10, verticalBodyLine.bottom - 10);
        footRight.set(centerX + 45, verticalBodyLine.bottom - 50, centerX + 65, verticalBodyLine.bottom - 10);
    }

    public void moveRight() {
        roted = false;
        if (ducked == false) {
            speedX = MOVESPEED;
        }
    }

    public void moveLeft() {
        roted = true;
        if (ducked == false) {
            speedX = -MOVESPEED;
        }
    }

    public void stopRight() {
        setMovingRight(false);
        roted = false;
        stop();
    }

    public void stopLeft() {
        setMovingLeft(false);
        roted = true;
        stop();
    }

    private void stop() {
        if (isMovingRight() == false && isMovingLeft() == false) {
            speedX = 0;
        }
        if (isMovingRight() == false && isMovingLeft() == true) {
            moveLeft();
            roted = true;
        }
        if (isMovingRight() == true && isMovingLeft() == false) {
            moveRight();
            roted = false;
        }
    }

    public void jump() {
        if (jumped == false) {
            speedY = JUMPSPEED;
            jumped = true;
            Assets.jump.play(1f);
        }
    }

    public void duck() {
        setDucked(true);
        setSpeedX(0);
    }

    public void shoot() {
        if (isRoted()) {
            if (readyToFire && !isDucked()) {
//                Fireball p = new Fireball(centerX - 80, centerY - 40);
//                fireballs.add(p);
                Assets.shoot.play(0.85f);
            } else {
//                Fireball p = new Fireball(centerX - 80, centerY + 60);
//                fireballs.add(p);
                Assets.shoot.play(0.85f);
            }
        } else {
            if (readyToFire && !isDucked()) {
//                Fireball p = new Fireball(centerX + 80, centerY - 40);
//                fireballs.add(p);
                Assets.shoot.play(0.85f);
            } else {
//                Fireball p = new Fireball(centerX + 80, centerY + 60);
//                fireballs.add(p);
                Assets.shoot.play(0.85f);
            }
        }
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public boolean isJumped() {
        return jumped;
    }

    public int getSpeedX() {
        return speedX;
    }

    public int getSpeedY() {
        return speedY;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public void setJumped(boolean jumped) {
        this.jumped = jumped;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }

    public boolean isDucked() {
        return ducked;
    }

    public void setDucked(boolean ducked) {
        this.ducked = ducked;
    }

    public boolean isMovingRight() {
        return movingRight;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    public boolean isMovingLeft() {
        return movingLeft;
    }

    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }

//    public ArrayList<Fireball> getFireballs() {
//        return fireballs;
//    }

    public boolean isReadyToFire() {
        return readyToFire;
    }

    public boolean isRoted() {
        return roted;
    }

}