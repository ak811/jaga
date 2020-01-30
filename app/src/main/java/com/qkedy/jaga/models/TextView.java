package com.qkedy.jaga.models;

import android.graphics.Paint;

import com.qkedy.jaga.interfaces.Drawable;
import com.qkedy.jaga.interfaces.Graphics;
import com.qkedy.jaga.interfaces.Input;

public class TextView implements Drawable {

    private String text;
    private int color;
    private Paint.Align align;

    public TextView(String text, int color, Paint.Align align) {
        this.text = text;
        this.color = color;
        this.align = align;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Paint.Align getAlign() {
        return align;
    }

    public void setAlign(Paint.Align align) {
        this.align = align;
    }
}
