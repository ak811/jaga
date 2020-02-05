package com.qkedy.jaga.models;

import androidx.annotation.NonNull;

import com.qkedy.jaga.examples.dragon.utils.Constants;
import com.qkedy.jaga.interfaces.Input;

import static java.lang.Math.abs;

public class Background {

    private final int BACKGROUND_MOVE_OFFSET = 10;
    private final float SCROLL_SPEED = 5;
    private final float ACCELERATION_OFFSET = .05f;
    private final float PAGE_CHANGING_SPEED = 25;

    private float bgX;
    private float bgY;
    private boolean isSelected;
    private int pageNumber;
    private int widthMax;
    private int heightMax;
    private long touchedTime;
    private float BackgroundStartX;
    private float BackgroundStartY;
    private float accelerationX;
    private float accelerationY;

    private int maxPages;
    private boolean goToNextPage;
    private boolean goToPreviousPage;
    private boolean backToCurrentPage;
    private float[] pagesStartPoint;

    public Background(float x, float y, int width, int height) {
        this.bgX = x;
        this.bgY = y;
        this.isSelected = false;
        this.widthMax = width;
        this.heightMax = height;
        this.accelerationX = 0;
        this.accelerationY = 0;
        this.pageNumber = 0;
        this.maxPages = width / Constants.SCREEN_WIDTH;
        this.pagesStartPoint = new float[maxPages];
        for (int i = 0; i < pagesStartPoint.length; i++) {
            pagesStartPoint[i] = i * Constants.SCREEN_WIDTH;
        }
    }

    public void moveHandler(Input.TouchEvent event) {
        float deltaX = (BackgroundStartX - event.getPoint().getX());
        float deltaY = (BackgroundStartY - event.getPoint().getY());
        long deltaT = System.currentTimeMillis() - touchedTime;
        if (widthMax > Constants.SCREEN_WIDTH) {
            if (bgX >= -(widthMax - Constants.SCREEN_WIDTH) && bgX <= 0) {
                if ((bgX - (deltaX) <= 0
                        && (bgX - (deltaX) >= -(widthMax - Constants.SCREEN_WIDTH)))) {
                    bgX -= (deltaX);
                    accelerationX = deltaX / deltaT;
                    BackgroundStartX = event.getPoint().getX();
                } else {
                    if (bgX >= -((widthMax - Constants.SCREEN_WIDTH) / 2f)) {
                        bgX = 0;
                    } else {
                        bgX = -(widthMax - Constants.SCREEN_WIDTH);
                    }
                }
            }
        }
        if (heightMax > Constants.SCREEN_HEIGHT) {
            if (bgY >= -(heightMax - Constants.SCREEN_HEIGHT) && bgY <= 0) {
                if ((bgY - (deltaY) <= 0
                        && (bgY - (deltaY) >= -(heightMax - Constants.SCREEN_HEIGHT)))) {
                    bgY = bgY - (deltaY);
                    accelerationY = deltaY / deltaT;
                    BackgroundStartY = event.getPoint().getY();
                } else {
                    if (bgY >= -((heightMax - Constants.SCREEN_HEIGHT) / 2f)) {
                        bgY = 0;
                    } else {
                        bgY = -(heightMax - Constants.SCREEN_HEIGHT);
                    }
                }
            }
        }
        touchedTime = System.currentTimeMillis();
    }

    public void scrollerHandler(float deltaTime) {
        if (accelerationX != 0) {
            if (accelerationX > 0) {
                if (bgX - abs(SCROLL_SPEED * accelerationX) >= -(widthMax - Constants.SCREEN_WIDTH)) {
                    bgX -= abs(SCROLL_SPEED * accelerationX);
                    if (accelerationX - ACCELERATION_OFFSET > 0) {
                        accelerationX -= ACCELERATION_OFFSET * deltaTime;
                    } else {
                        accelerationX = 0;
                    }
                } else {
                    bgX = -(widthMax - Constants.SCREEN_WIDTH);
                    accelerationX = 0;
                }
            } else if (accelerationX < 0) {
                if ((bgX + abs(SCROLL_SPEED * accelerationX) <= 0)) {
                    bgX += abs(SCROLL_SPEED * accelerationX);
                    if (accelerationX + ACCELERATION_OFFSET < 0) {
                        accelerationX += ACCELERATION_OFFSET * deltaTime;
                    } else {
                        accelerationX = 0;
                    }
                } else {
                    bgX = 0;
                    accelerationX = 0;
                }
            }
        }

        if (accelerationY != 0) {
            if (accelerationY > 0) {
                if (bgY - abs(SCROLL_SPEED * accelerationY) >= -(heightMax - Constants.SCREEN_HEIGHT)) {
                    bgY -= abs(SCROLL_SPEED * accelerationY);
                    if (accelerationY - ACCELERATION_OFFSET > 0) {
                        accelerationY -= ACCELERATION_OFFSET * deltaTime;
                    } else {
                        accelerationY = 0;
                    }
                } else {
                    bgY = -(heightMax - Constants.SCREEN_HEIGHT);
                    accelerationY = 0;
                }
            } else if (accelerationY < 0) {
                if ((bgY + abs(SCROLL_SPEED * accelerationY) <= 0)) {
                    bgY += abs(SCROLL_SPEED * accelerationY);
                    if (accelerationY + ACCELERATION_OFFSET < 0) {
                        accelerationY += ACCELERATION_OFFSET * deltaTime;
                    } else {
                        accelerationY = 0;
                    }
                } else {
                    bgY = 0;
                    accelerationY = 0;
                }
            }
        }
    }

    public void pageHandler(float deltaTime) {
        if (goToNextPage) {
            if (bgX - (PAGE_CHANGING_SPEED * deltaTime) > -pagesStartPoint[pageNumber + 1]) {
                bgX -= (PAGE_CHANGING_SPEED * deltaTime);
            } else {
                bgX = -pagesStartPoint[pageNumber + 1];
                pageNumber++;
                goToNextPage = false;
            }
        } else if (goToPreviousPage) {
            if (bgX + PAGE_CHANGING_SPEED * deltaTime < -pagesStartPoint[pageNumber - 1]) {
                bgX += (PAGE_CHANGING_SPEED * deltaTime);
            } else {
                bgX = -pagesStartPoint[pageNumber - 1];
                pageNumber--;
                goToPreviousPage = false;
            }
        } else if (backToCurrentPage) {
            if ((pagesStartPoint[pageNumber] + bgX) < 0) {
                if (bgX + (PAGE_CHANGING_SPEED * deltaTime) < -pagesStartPoint[pageNumber]) {
                    bgX += (PAGE_CHANGING_SPEED * deltaTime);
                } else {
                    bgX = -pagesStartPoint[pageNumber];
                    backToCurrentPage = false;
                }
            } else {
                if (bgX - (PAGE_CHANGING_SPEED * deltaTime) > -pagesStartPoint[pageNumber]) {
                    bgX -= (PAGE_CHANGING_SPEED * deltaTime);
                } else {
                    bgX = -pagesStartPoint[pageNumber];
                    backToCurrentPage = false;
                }
            }
        }
    }

    public boolean changePage(float deltaTime, int goalPage) {
        boolean isPageChanged = false;
        if (goalPage < pageNumber) {
            if (bgX + (PAGE_CHANGING_SPEED * deltaTime) < -pagesStartPoint[goalPage]) {
                bgX += (PAGE_CHANGING_SPEED * deltaTime);
            } else {
                bgX = -pagesStartPoint[goalPage];
                pageNumber = goalPage;
                isPageChanged = true;
            }
        } else if (goalPage > pageNumber) {
            if (bgX - (PAGE_CHANGING_SPEED * deltaTime) > -pagesStartPoint[goalPage]) {
                bgX -= (PAGE_CHANGING_SPEED * deltaTime);
            } else {
                bgX = -pagesStartPoint[goalPage];
                pageNumber = goalPage;
                isPageChanged = true;
            }
        } else {
            bgX = -pagesStartPoint[goalPage];
            pageNumber = goalPage;
            isPageChanged = true;
        }
        return isPageChanged;
    }

    public void pageCheck() {
        goToNextPage = false;
        goToPreviousPage = false;
        backToCurrentPage = false;
        if (pagesStartPoint[pageNumber] - abs(bgX) < -Constants.SCREEN_WIDTH / 2f || accelerationX > .2f
                && pageNumber < maxPages - 1) {
            goToNextPage = true;
        } else if (pagesStartPoint[pageNumber] - abs(bgX) > Constants.SCREEN_WIDTH / 2f || accelerationX < -.2f
                && pageNumber > 0) {
            goToPreviousPage = true;
        } else {
            backToCurrentPage = true;
        }
    }

    public float getBgX() {
        return bgX;
    }

    public float getBgY() {
        return bgY;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getMaxPages() {
        return maxPages;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected, @NonNull Input.TouchEvent event) {
        if (selected) {
            this.touchedTime = System.currentTimeMillis();
            this.BackgroundStartX = event.getPoint().getX();
            this.BackgroundStartY = event.getPoint().getY();
        }
        this.isSelected = selected;
    }

    public void setAccelerationsToZero() {
        accelerationX = 0;
        accelerationY = 0;
    }

}
