package com.qkedy.jaga.models;

import com.qkedy.jaga.interfaces.Image;

import java.util.ArrayList;

public class Animation {

    private ArrayList<AnimFrame> frames;
    private int currentFrame;
    private long animTime;
    private long totalDuration;

    private boolean animFinished;

    public Animation() {
        frames = new ArrayList<>();
        totalDuration = 0;
        animFinished = false;

        synchronized (this) {
            animTime = 0;
            currentFrame = 0;
        }
    }

    /**
     * synchronized method waits for the methods that use the same resources.
     * (avoiding multiThreading)
     */
    public synchronized void addFrame(Image image, long duration) {
        totalDuration += duration;
        frames.add(new AnimFrame(image, totalDuration));
    }

    public synchronized void update(long elapsedTime, float deltaTime) {
        if (frames.size() > 1) {
            animTime += elapsedTime;
            if (animTime >= totalDuration) {
                animTime = animTime % totalDuration;
                currentFrame = 0;
                animFinished = true;
            }

            while (animTime > getFrame(currentFrame).endTime)
                currentFrame++;
        }
    }

    public synchronized Image getImage() {
        if (frames.size() == 0) {
            return null;
        } else {
            return getFrame(currentFrame).image;
        }
    }

    private AnimFrame getFrame(int i) {
        return frames.get(i);
    }

    public synchronized boolean isAnimFinished() {
        if (animFinished) {
            animFinished = false;
            return true;
        } else
            return false;
    }

    public synchronized int getCurrentFrameNumber() {
        return currentFrame;
    }

    public synchronized void setCurrentFrameNumber(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public synchronized void resetAnimation() {
        animTime = 0;
        currentFrame = 0;
    }

    public synchronized long getAnimTime() {
        return animTime;
    }

    public synchronized void setAnimTime(long animTime) {
        this.animTime = animTime;
    }

    private class AnimFrame {

        private Image image;
        private long endTime;

        AnimFrame(Image image, long endTime) {
            this.image = image;
            this.endTime = endTime;
        }
    }
}
