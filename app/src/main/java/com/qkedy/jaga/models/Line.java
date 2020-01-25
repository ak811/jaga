package com.qkedy.jaga.models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.qkedy.jaga.utils.Constants;

import java.util.Objects;

public class Line {
    private Point src, des;
    private float strokeWidth;
    private int color;
    private float slope;
    private double length;
    private boolean visible;

    private final int MAX_SLOPE_VALUE_CONSIDERED_AS_INFINITY_SLOPE = 100;

    public Line(Point src, Point des) {
        this.src = src;
        this.des = des;
    }

    public Line(Point src, Point des, float strokeWidth, int color) {
        this(src, des);
        this.strokeWidth = strokeWidth;
        this.color = color;
        this.visible = true;
    }

    public boolean doesPointIsInTheLineEquation(Point touchPoint) {

        float lineTouchOffset = 40;

        // if(src.getX() - des.getX() == 0)
        if (String.valueOf(Math.abs(getSlope())).equals(Constants.LINE_MAX_SLOPE)) {
            if (des.getY() >= src.getY()) {
                if ((touchPoint.getY() <= des.getY() - lineTouchOffset / 2)
                        && (touchPoint.getY() >= src.getY() - lineTouchOffset / 2)) {
                    return (touchPoint.getX() >= src.getX() - lineTouchOffset / 2)
                            && (touchPoint.getX() <= src.getX() + lineTouchOffset / 2);
                }
            } else {
                if ((touchPoint.getY() >= des.getY() - lineTouchOffset / 2)
                        && (touchPoint.getY() <= src.getY() + lineTouchOffset / 2)) {
                    return (touchPoint.getX() >= src.getX() - lineTouchOffset / 2)
                            && (touchPoint.getX() <= src.getX() + lineTouchOffset / 2);
                }
            }
        }
        // if(src.getY() - des.getY() == 0)
        else if (Math.abs(getSlope()) == 0) {
            if (des.getX() >= src.getX()) {
                if (touchPoint.getX() <= des.getX() && touchPoint.getX() >= src.getX()) {
                    return (touchPoint.getY() >= src.getY() - lineTouchOffset / 2)
                            && (touchPoint.getY() <= src.getY() + lineTouchOffset / 2);
                }
            } else {
                if (touchPoint.getX() <= src.getX() && touchPoint.getX() >= des.getX()) {
                    return (touchPoint.getY() >= src.getY() - lineTouchOffset / 2)
                            && (touchPoint.getY() <= src.getY() + lineTouchOffset / 2);
                }
            }
        } else {
            float leftLineEquation_y = getSlope() * (touchPoint.getX() - src.getX()) + src.getY() - lineTouchOffset / 2;
            float rightLineEquation_y = leftLineEquation_y + lineTouchOffset;

            if (touchPoint.getY() >= leftLineEquation_y && touchPoint.getY() <= rightLineEquation_y) {
                if (src.getX() <= des.getX() && src.getY() <= des.getY()) {
                    return touchPoint.getX() >= src.getX() && touchPoint.getX() <= des.getX()
                            && touchPoint.getY() >= src.getY() && touchPoint.getY() <= des.getY();
                } else if (src.getX() <= des.getX() && src.getY() >= des.getY()) {
                    return touchPoint.getX() >= src.getX() && touchPoint.getX() <= des.getX()
                            && touchPoint.getY() <= src.getY() && touchPoint.getY() >= des.getY();
                } else if (src.getX() >= des.getX() && src.getY() <= des.getY()) {
                    return touchPoint.getX() <= src.getX() && touchPoint.getX() >= des.getX()
                            && touchPoint.getY() >= src.getY() && touchPoint.getY() <= des.getY();
                } else if (src.getX() >= des.getX() && src.getY() >= des.getY()) {
                    return touchPoint.getX() <= src.getX() && touchPoint.getX() >= des.getX()
                            && touchPoint.getY() <= src.getY() && touchPoint.getY() >= des.getY();
                }
            }
        }

        return false;
    }

    public boolean isLine() {
        return !src.equals(des);
    }

    public float getSlope() {
        slope = (des.getY() - src.getY()) / (des.getX() - src.getX());

        if (Math.abs(slope) > MAX_SLOPE_VALUE_CONSIDERED_AS_INFINITY_SLOPE)
            slope = Float.valueOf("Infinity");

        if (String.valueOf(Math.abs(slope)).equals("Infinity") || Math.abs(slope) == 0)
            slope = Math.abs(slope);

        return slope;
    }

    public void setXLength(double xLength) {
        this.length = xLength;
    }

    public double getLength() {
        length = Math.sqrt(Math.pow(des.getX() - src.getX(), 2) + Math.pow(des.getY() - src.getY(), 2));
        return this.length;
    }

    public void setSlope(float slope) {
        this.slope = slope;
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Point getSrc() {
        return src;
    }

    public void setSrc(Point src) {
        this.src = src;
    }

    public Point getDes() {
        return des;
    }

    public void setDes(Point des) {
        this.des = des;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public String toString() {
        return
                ""
                        + "\nsrc: " + src.getX() + " " + src.getY()
                        + "\ndes: " + des.getX() + " " + des.getY()
                        + "\nslope: " + getSlope()
                        + "\n///////////////////////////////"
                ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return src.equals(line.src) &&
                des.equals(line.des);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(src, des);
    }
}