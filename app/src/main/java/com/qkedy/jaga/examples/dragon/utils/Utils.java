package com.qkedy.jaga.examples.dragon.utils;

import com.qkedy.jaga.interfaces.Input;

public class Utils {
    public static boolean inBounds(Input.TouchEvent event, int x, int y, int width, int height) {
        return event.getPoint().getX() > x &&
                event.getPoint().getX() < x + width - 1 && event.getPoint().getY() > y
                && event.getPoint().getY() < y + height - 1;
    }
}
