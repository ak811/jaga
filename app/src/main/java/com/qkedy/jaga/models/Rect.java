package com.qkedy.jaga.models;

import android.graphics.Paint;

import com.qkedy.jaga.interfaces.Drawable;
import com.qkedy.jaga.interfaces.Graphics;
import com.qkedy.jaga.interfaces.Input;

import static android.graphics.Paint.Style;

public class Rect implements Drawable {

    private float bottom;
    private float left;
    private float right;
    private float top;
    private int color;
    private int alpha;
    private int radius;
    private int border;
    private Style style;
    private Paint paint;
    private boolean isShadowOn;
    private int shadowColor;
    private float shadowOffsetX;
    private float shadowOffsetY;

    public Rect(float bottom, float left, float right, float top) {
        this.bottom = bottom;
        this.left = left;
        this.right = right;
        this.top = top;
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getBorder() {
        return border;
    }

    public void setBorder(int border) {
        this.border = border;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
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

    @Override
    public void updateAnimation(float deltaTime) {

    }

    @Override
    public boolean onMoveAnimation(Point endPoint, float speed, float deltaTime) {
        return false;
    }

    @Override
    public void onTouch(Input.TouchEvent event) {

    }

    @Override
    public void onDraw(Graphics g) {

    }
}
