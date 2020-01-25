package com.qkedy.jaga.models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Objects;

public class Point {

    private float x;
    private float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
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

    public void setPoint(Point point) {
        this.x = point.getX();
        this.y = point.getY();
    }

    @Override
    public String toString() {
        return "\nx: " + x
                + "\ny: " + y
                + "\n>>>>>>>>>>>>>>>>>>>>";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!Point.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Point otherPoint = (Point) obj;
        return this.x == otherPoint.x && this.y == otherPoint.y;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
