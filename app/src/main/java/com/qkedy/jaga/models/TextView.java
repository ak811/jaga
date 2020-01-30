package com.qkedy.jaga.models;

import com.qkedy.jaga.interfaces.Drawable;
import com.qkedy.jaga.interfaces.Graphics;
import com.qkedy.jaga.interfaces.Input;

public class TextView implements Drawable {

    private String text;

    public TextView(String text) {
        this.text = text;
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
}
