package com.qkedy.jaga.interfaces;

import com.qkedy.jaga.models.Point;

public interface Drawable {

    void updateAnimation(float deltaTime);

    boolean onMoveAnimation(Point endPoint, float speed, float deltaTime);

    void onTouch(Input.TouchEvent event);

    void onDraw(Graphics g);
}
