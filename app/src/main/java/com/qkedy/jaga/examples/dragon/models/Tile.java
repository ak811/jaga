package com.qkedy.jaga.examples.dragon.models;

import android.graphics.Rect;

import com.qkedy.jaga.examples.dragon.screens.GameScreen;
import com.qkedy.jaga.examples.dragon.utils.Assets;
import com.qkedy.jaga.interfaces.Image;


public class Tile {

    // Constants are Here
    private int tileX, tileY, speedX;
    public int type;
    public Image tileImage;

    private Dragon dragon = GameScreen.getDragon();
    private Background background = GameScreen.getBackground();

    private Rect tile;

    public Tile(int x, int y, int typeInt) {
        tileX = x * 40;
        tileY = y * 40;
        type = typeInt;

        tile = new Rect();

        if (type == 5) {
            tileImage = Assets.tileDirt;
        } else if (type == 8) {
            tileImage = Assets.tileGrassTop;
        } else if (type == 4) {
            tileImage = Assets.tileGrassLeft;
        } else if (type == 6) {
            tileImage = Assets.tileGrassRight;
        } else if (type == 2) {
            tileImage = Assets.tileGrassBot;
        } else if (type == 9) {
            tileImage = Assets.winFlag;
        } else {
            type = 0;
        }
    }

    public void update(float deltaTime) {
        speedX = background.getSpeedX() * 5;
        tileX += speedX;
        tile.set(tileX, tileY, tileX + 50, tileY + 50);
        if (Rect.intersects(tile, Dragon.body) && type != 0) {
            checkVerticalCollision(Dragon.verticalBodyLine);
            checkSideCollision(Dragon.bodyLeft, Dragon.bodyRight, Dragon.footLeft,
                    Dragon.footRight);
        }
    }

    public void checkVerticalCollision(Rect drag) {
        if (Rect.intersects(drag, tile) && type == 8) {
            dragon.setJumped(false);
            dragon.setSpeedY(0);
            dragon.setCenterY(tileY - 130);
        }
        if (Rect.intersects(drag, tile) && type == 2) {
            dragon.setSpeedY(0);
            dragon.setCenterY(tileY + 120);
        }
        if (Rect.intersects(drag, tile) && type == 9) {
            GameScreen.state = GameScreen.GameState.Win;
        }
    }

    public void checkSideCollision(Rect rleft, Rect rright, Rect leftfoot, Rect rightfoot) {
        if (type != 0 && type != 9) {
            if (Rect.intersects(rleft, tile)) {
                dragon.setCenterX(tileX + 130);
                dragon.setSpeedX(0);
            } else if (Rect.intersects(leftfoot, tile)) {
                dragon.setCenterX(tileX + 50);
                dragon.setSpeedX(0);
            } else if (Rect.intersects(rright, tile)) {
                dragon.setCenterX(tileX - 130);
                dragon.setSpeedX(0);
            } else if (Rect.intersects(rightfoot, tile)) {
                dragon.setCenterX(tileX - 50);
                dragon.setSpeedX(0);
            }
        }
    }

    public int getTileX() {
        return tileX;
    }

    public void setTileX(int tileX) {
        this.tileX = tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public void setTileY(int tileY) {
        this.tileY = tileY;
    }

    public Image getTileImage() {
        return tileImage;
    }

    public void setTileImage(Image tileImage) {
        this.tileImage = tileImage;
    }

    public Rect getTileRect() {
        return tile;
    }

    public int getTileType() {
        return type;
    }

}