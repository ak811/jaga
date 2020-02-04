package com.qkedy.jaga.interfaces;


import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;

import androidx.annotation.Nullable;

import com.qkedy.jaga.models.Background;
import com.qkedy.jaga.models.Line;
import com.qkedy.jaga.models.Point;

import static android.graphics.Bitmap.Config;

public interface Graphics {

    /**
     * @param fileName the name of file
     * @param config   the image format
     * @return an Image specified with the file and config
     * @apiNote builds new AndroidImage
     */
    Image newImage(String fileName, Config config);

    Image cutNewImage(String path, int width, int height, int x, int y);

    void clearScreen(int color);

    void drawPath(Path path, int border, int color, int alpha, boolean isDashed);

    void drawRect(Rect rect, int color, int alpha);

    void drawRect(float x, float y, float width, float height, int color, int alpha);

    void drawRect(Point coordinate, float width, float height, int color, int alpha);

    void drawRect(Rect rect, Background background, int color, int alpha);

    void drawRoundRect(float x, float y, float width, float height, int radius, int color, int alpha);

    void drawRoundRect(Point coordinate, float width, float height, int radius, int color, int alpha);

    void drawHollowRoundRect(float x, float y, float width, float height, int radius, int border, int color, int alpha);

    void drawHollowRoundRect(Point coordinate, float width, float height, int radius, int border, int color, int alpha);

    void drawCircle(float x, float y, int radius, int color, int alpha);

    void drawCircle(Point coordinate, int radius, int color, int alpha);

    void drawHollowCircle(float x, float y, int radius, int border, int color, int alpha);

    void drawHollowCircle(Point coordinate, int radius, int border, int color, int alpha);

    void drawLine(float x1, float y1, float x2, float y2, int border, int color, int alpha);

    void drawLine(Point p1, Point p2, int border, int color, int alpha);

    void drawLine(Line line, float alpha, int color, float offsetX, float offsetY);

    void drawShadowLine(Line line, float alpha, int color, float offsetX, float offsetY);

    void drawARGB(int a, int r, int g, int b);

    void drawString(String text, float x, float y, Paint paint, int alpha);

    void drawString(String text, Point coordinate, Paint paint, int alpha);

    void drawShadowString(String text, float x, float y, Paint paint, int shadowColor, int alpha);

    void drawShadowString(String text, Point coordinate, Paint paint, int shadowColor, int alpha);

    void drawImage(Image image, float x, float y);

    void drawImage(Image image, float x, float y, int alpha);

    void drawImage(Image image, float x, float y, int alpha, boolean isPushed,
                   @Nullable String color, @Nullable PorterDuff.Mode cMode);

    void drawImage(Image image, Point coordinate, int alpha, boolean isPushed,
                   @Nullable String color, @Nullable PorterDuff.Mode cMode);

    void drawScaledImage(Image image, float x, float y, int width, int height,
                         int alpha, boolean isPushed, @Nullable String color, @Nullable PorterDuff.Mode cMode);

    void drawScaledImage(Image image, Point coordinate, int width, int height,
                         int alpha, boolean isPushed, @Nullable String color, @Nullable PorterDuff.Mode cMode);

    void drawRotateImage(Image image, float x, float y, float degree, int alpha,
                         boolean isPushed, @Nullable String color, @Nullable PorterDuff.Mode cMode);

    void drawRotateImage(Image image, Point coordinate, float degree, int alpha,
                         boolean isPushed, @Nullable String color, @Nullable PorterDuff.Mode cMode);

    void drawScaledRotatedImage(Image image, float x, float y, int width, int height,
                                float degree, int alpha, boolean isPushed,
                                @Nullable String color, @Nullable PorterDuff.Mode cMode);

    void drawScaledRotatedImage(Image image, Point coordinate, int width, int height,
                                float degree, int alpha, boolean isPushed,
                                @Nullable String color, @Nullable PorterDuff.Mode cMode);

    void drawCroppedImage(Image image, float x, float y, int srcX, int srcY, int srcWidth,
                          int srcHeight, int alpha, boolean isPushed,
                          @Nullable String color, @Nullable PorterDuff.Mode cMode);

    void drawCroppedImage(Image image, Point coordinate, int srcX, int srcY, int srcWidth,
                          int srcHeight, int alpha, boolean isPushed,
                          @Nullable String color, @Nullable PorterDuff.Mode cMode);

    void drawScaledCroppedImage(Image image, float x, float y, int width, int height, int srcX, int srcY,
                                int srcWidth, int srcHeight, int alpha, boolean isPushed,
                                @Nullable String color, @Nullable PorterDuff.Mode cMode);

    void drawScaledCroppedImage(Image image, Point coordinate, int width, int height, int srcX, int srcY,
                                int srcWidth, int srcHeight, int alpha, boolean isPushed,
                                @Nullable String color, @Nullable PorterDuff.Mode cMode);

    void drawRotateCroppedImage(Image image, float x, float y, int srcX, int srcY, int srcWidth, int srcHeight,
                                float degree, int alpha, boolean isPushed,
                                @Nullable String color, @Nullable PorterDuff.Mode cMode);

    void drawRotateCroppedImage(Image image, Point coordinate, int srcX, int srcY, int srcWidth, int srcHeight,
                                float degree, int alpha, boolean isPushed,
                                @Nullable String color, @Nullable PorterDuff.Mode cMode);

    void drawScaledRotateCroppedImage(Image image, float x, float y, int width, int height, int srcX, int srcY,
                                      int srcWidth, int srcHeight, float degree, int alpha, boolean isPushed,
                                      @Nullable String color, @Nullable PorterDuff.Mode cMode);

    void drawScaledRotateCroppedImage(Image image, Point coordinate, int width, int height, int srcX, int srcY,
                                      int srcWidth, int srcHeight, float degree, int alpha, boolean isPushed,
                                      @Nullable String color, @Nullable PorterDuff.Mode cMode);

    void drawScaledImageWithShadow(Image image, float desX, float desY, float width, float height, int shadowOffsetX, int shadowOffsetY);

    int getWidth();

    int getHeight();
}
