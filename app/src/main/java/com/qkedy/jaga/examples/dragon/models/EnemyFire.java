package com.qkedy.jaga.examples.dragon.models;

import android.graphics.Rect;

public class EnemyFire {

    // Constants are Here
    private int x, y, speedX, speedY;
    private boolean visible;

    private Dragon dragon = GameScreen.getDragon();

    private Rect fire;

    public EnemyFire(int startX, int startY) {
        x = startX;
        y = startY;
        speedX = (dragon.getCenterX() - x) / 15;
        speedY = (dragon.getCenterY() - y) / 15;
        visible = true;
        fire = new Rect(0, 0, 0, 0);
    }

    public void update(float deltaTime) {
        x += speedX;
        y += speedY;
        fire.set(x, y, x + 10, y + 5);
        if (x < 0) {
            visible = false;
            fire = null;
        }
        if (x > 0) {
            checkCollision();
        }

    }

    private void checkCollision() {
        if (Rect.intersects(fire, Dragon.body) && !GameScreen.dragon.isDucked()) {
            visible = false;
            if (GameScreen.dragon.health > 0) {
                GameScreen.dragon.health -= 1;
            }
        }
        for (int t = 0; t < GameScreen.getTile().size(); t++) {
            if ((Rect.intersects(fire, GameScreen.getTile().get(t).getTileRect())) && (GameScreen.getTile().get(t).getTileType() != 0)) {
                for (int e = 0; e < GameScreen.enemyList.size(); e++)
                    GameScreen.enemyList.get(e).getFire().remove(this);
            }
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

}
