package com.qkedy.jaga.models;

import android.graphics.Paint;

import com.qkedy.jaga.interfaces.Drawable;
import com.qkedy.jaga.interfaces.Graphics;
import com.qkedy.jaga.interfaces.Image;
import com.qkedy.jaga.interfaces.Input;

public class ImageButton implements Drawable {

    private Image image;
    private float x;
    private float y;
    private float desX;
    private float desY;
    private int degree;
    private int color;
    private int alpha;
    private Paint paint;
    private boolean isPushed;
    private boolean isEnable;
    private Animation animation;
    private boolean isShadowOn;
    private int shadowColor;
    private float shadowOffsetX;
    private float shadowOffsetY;
    private final int pushOffset = 5;

    public ImageButton(Image image, float x, float y) {
        this.image = image;
        this.x = x;
        this.y = y;
    }

    @Override
    public void updateAnimation(float deltaTime) {

    }

    @Override
    public boolean onMoveAnimation(Point endPoint, float speed, float deltaTime) {
        return true;
    }

    @Override
    public void onTouch(Input.TouchEvent event) {

    }

    @Override
    public void onDraw(Graphics g) {

    }

    //****** Getters && setters ******
    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getDesX() {
        return desX;
    }

    public void setDesX(float desX) {
        this.desX = desX;
    }

    public float getDesY() {
        return desY;
    }

    public void setDesY(float desY) {
        this.desY = desY;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public boolean isPushed() {
        return isPushed;
    }

    public void setPushed(boolean pushed) {
        isPushed = pushed;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public Animation getAnimation() {
        return animation;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public boolean isShadowOn() {
        return isShadowOn;
    }

    public void setShadowOn(boolean shadowOn) {
        isShadowOn = shadowOn;
    }

    public int getShadowColor() {
        return shadowColor;
    }

    public void setShadowColor(int shadowColor) {
        this.shadowColor = shadowColor;
    }

    public float getShadowOffsetX() {
        return shadowOffsetX;
    }

    public void setShadowOffsetX(float shadowOffsetX) {
        this.shadowOffsetX = shadowOffsetX;
    }

    public float getShadowOffsetY() {
        return shadowOffsetY;
    }

    public void setShadowOffsetY(float shadowOffsetY) {
        this.shadowOffsetY = shadowOffsetY;
    }
}