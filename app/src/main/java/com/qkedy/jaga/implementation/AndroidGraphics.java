package com.qkedy.jaga.implementation;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.os.Build;

import androidx.annotation.Nullable;

import com.qkedy.jaga.interfaces.Graphics;
import com.qkedy.jaga.interfaces.Image;
import com.qkedy.jaga.models.Background;
import com.qkedy.jaga.models.Line;
import com.qkedy.jaga.models.Point;

import java.io.IOException;
import java.io.InputStream;

import static com.qkedy.jaga.implementation.AndroidFastRenderView.canvas;
import static com.qkedy.jaga.implementation.AndroidGame.scaleX;
import static com.qkedy.jaga.implementation.AndroidGame.scaleY;
import static java.lang.Math.abs;

public class AndroidGraphics implements Graphics {
    private final int PUSH_DOWN_OFFSET = 8;

    private AssetManager assets;
    private Bitmap frameBuffer;
    private Paint paint;
    private Paint imagePaint;
    private Rect srcRect;
    private Rect dstRect;
    private Rect srcRectNew;
    private Rect dstRectNew;
    private Matrix scaleMatrix;
    private Path pathHelper;

    AndroidGraphics(AssetManager assets, Bitmap frameBuffer) {
        this.assets = assets;
        this.frameBuffer = frameBuffer;
        this.paint = new Paint();
        this.imagePaint = new Paint(Paint.FILTER_BITMAP_FLAG);
        imagePaint.setAntiAlias(false);
        imagePaint.setDither(false);
        srcRect = new Rect();
        dstRect = new Rect();
        srcRectNew = new Rect();
        dstRectNew = new Rect();
        scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY);
        pathHelper = new Path();
    }

    @Override
    public Image newImage(String fileName, Config config) {

        Options options = new Options();
        options.inPreferredConfig = config;
        options.inScaled = false;
        options.inMutable = true;

        Bitmap bitmap;
        try (InputStream in = assets.open(fileName)) {
            bitmap = BitmapFactory.decodeStream(in, null, options);
            if (bitmap == null)
                throw new RuntimeException("Couldn't load bitmap from asset '"
                        + fileName + "'");
            else
                bitmap.setHasAlpha(true);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load bitmap from asset '"
                    + fileName + "'");
        }

        return new AndroidImage(bitmap, config);
    }

    @Override
    public Image cutNewImage(String path, int width, int height, int x, int y) {
        Config config = Config.ARGB_8888;

        Options options = new Options();
        options.inPreferredConfig = config;
        options.inScaled = false;

        Bitmap bitmap;
        try (InputStream in = assets.open(path)) {
            bitmap = BitmapFactory.decodeStream(in, null, options);
            if (bitmap == null)
                throw new RuntimeException("Couldn't load bitmap from asset '"
                        + path + "'");
            else
                bitmap.setHasAlpha(true);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load bitmap from asset '"
                    + path + "'");
        }

        Bitmap temp = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(temp);

        srcRectNew.left = width * x;
        srcRectNew.top = height * y;
        srcRectNew.right = (width * x) + width;
        srcRectNew.bottom = (height * y) + height;

        dstRectNew.left = 0;
        dstRectNew.top = 0;
        dstRectNew.right = (width);
        dstRectNew.bottom = (height);

        canvas.drawBitmap(bitmap, srcRectNew, dstRectNew, null);

        return new AndroidImage(temp, config);
    }

    @Override
    public void clearScreen(int color) {
        canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8,
                (color & 0xff));
    }

    @Override
    public void drawPath(Path path, int border, int color, int alpha, boolean isDashed) {
        paint.setColor(color);
        paint.setStrokeWidth(border * scaleX);
        paint.setAlpha(alpha);
        paint.setStyle(Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        if (isDashed)
            paint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
        else
            paint.setPathEffect(null);
        pathHelper.set(path);
        pathHelper.transform(scaleMatrix);
        canvas.drawPath(pathHelper, paint);
    }

    @Override
    public void drawRect(float x, float y, float width, float height, int color, int alpha) {
        paint.setColor(color);
        paint.setAlpha(alpha);
        paint.setStyle(Style.FILL);
        paint.setAntiAlias(true);
        canvas.drawRect(x * scaleX, y * scaleY,
                (x + width) * scaleX, (y + height) * scaleY, paint);
    }

    @Override
    public void drawRect(Rect rect, int color, int alpha) {
        paint.setColor(color);
        paint.setAlpha(alpha);
        paint.setStyle(Style.FILL);
        paint.setAntiAlias(true);
        canvas.drawRect(rect.left * scaleX, rect.top * scaleY,
                rect.right * scaleX, rect.bottom * scaleY, paint);
    }

    @Override
    public void drawRect(Point coordinate, float width, float height, int color, int alpha) {
        paint.setColor(color);
        paint.setAlpha(alpha);
        paint.setStyle(Style.FILL);
        paint.setAntiAlias(true);
        canvas.drawRect(coordinate.getX() * scaleX, coordinate.getY() * scaleY,
                (coordinate.getX() + width) * scaleX, (coordinate.getY() + height) * scaleY, paint);
    }

    @Override
    public void drawRect(Rect rect, Background background, int color, int alpha) {
        paint.setColor(color);
        paint.setAlpha(alpha);
        paint.setStyle(Style.FILL);
        canvas.drawRect((rect.left + background.getBgX()) * scaleX,
                (rect.top + background.getBgY()) * scaleY,
                (rect.right + background.getBgX()) * scaleX,
                (rect.bottom + background.getBgY()) * scaleY, paint);
    }

    @Override
    public void drawRoundRect(float x, float y, float width, float height, int radius, int color, int alpha) {
        paint.setColor(color);
        paint.setAlpha(alpha);
        paint.setStyle(Style.FILL);
        paint.setAntiAlias(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            canvas.drawRoundRect(x * scaleX, y * scaleY,
                    (x + width) * scaleX, (y + height) * scaleY, radius, radius, paint);
        else
            canvas.drawRect(x * scaleX, y * scaleY,
                    (x + width) * scaleX, (y + height) * scaleY, paint);
    }

    @Override
    public void drawRoundRect(Point coordinate, float width, float height, int radius, int color, int alpha) {
        paint.setColor(color);
        paint.setAlpha(alpha);
        paint.setStyle(Style.FILL);
        paint.setAntiAlias(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            canvas.drawRoundRect(coordinate.getX() * scaleX, coordinate.getY() * scaleY,
                    (coordinate.getX() + width) * scaleX, (coordinate.getY() + height) * scaleY, radius, radius, paint);
        else
            canvas.drawRect(coordinate.getX() * scaleX, coordinate.getY() * scaleY,
                    (coordinate.getX() + width) * scaleX, (coordinate.getY() + height) * scaleY, paint);
    }

    @Override
    public void drawHollowRoundRect(float x, float y, float width, float height,
                                    int radius, int border, int color, int alpha) {
        paint.setColor(color);
        paint.setAlpha(alpha);
        paint.setStrokeWidth(border * scaleX);
        paint.setStyle(Style.STROKE);
        paint.setAntiAlias(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            canvas.drawRoundRect(x * scaleX, y * scaleY,
                    (x + width) * scaleX, (y + height) * scaleY, radius, radius, paint);
        else
            canvas.drawRect(x * scaleX, y * scaleY,
                    (x + width) * scaleX, (y + height) * scaleY, paint);
    }

    @Override
    public void drawHollowRoundRect(Point coordinate, float width, float height,
                                    int radius, int border, int color, int alpha) {
        paint.setColor(color);
        paint.setAlpha(alpha);
        paint.setStrokeWidth(border * scaleX);
        paint.setStyle(Style.STROKE);
        paint.setAntiAlias(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            canvas.drawRoundRect(coordinate.getX() * scaleX, coordinate.getY() * scaleY,
                    (coordinate.getX() + width) * scaleX, (coordinate.getY() + height) * scaleY, radius, radius, paint);
        else
            canvas.drawRect(coordinate.getX() * scaleX, coordinate.getY() * scaleY,
                    (coordinate.getX() + width) * scaleX, (coordinate.getY() + height) * scaleY, paint);
    }

    @Override
    public void drawCircle(float x, float y, int radius, int color, int alpha) {
        paint.setColor(color);
        paint.setAlpha(alpha);
        paint.setStyle(Style.FILL);
        paint.setAntiAlias(true);
        canvas.drawCircle(x * scaleX,
                y * scaleY, radius * scaleX, paint);
    }

    @Override
    public void drawCircle(Point coordinate, int radius, int color, int alpha) {
        paint.setColor(color);
        paint.setAlpha(alpha);
        paint.setStyle(Style.FILL);
        paint.setAntiAlias(true);
        canvas.drawCircle(coordinate.getX() * scaleX,
                coordinate.getY() * scaleY, radius * scaleX, paint);
    }

    @Override
    public void drawHollowCircle(float x, float y, int radius, int border, int color, int alpha) {
        paint.setColor(color);
        paint.setAlpha(alpha);
        paint.setStrokeWidth(border * scaleX);
        paint.setStyle(Style.STROKE);
        paint.setAntiAlias(true);
        canvas.drawCircle(x * scaleX,
                y * scaleY, radius * scaleX, paint);
    }

    @Override
    public void drawHollowCircle(Point coordinate, int radius, int border, int color, int alpha) {
        paint.setColor(color);
        paint.setAlpha(alpha);
        paint.setStrokeWidth(border * scaleX);
        paint.setStyle(Style.STROKE);
        paint.setAntiAlias(true);
        canvas.drawCircle(coordinate.getX() * scaleX,
                coordinate.getY() * scaleY, radius * scaleX, paint);
    }

    @Override
    public void drawLine(float x1, float y1, float x2, float y2, int border, int color, int alpha) {
        paint.setColor(color);
        paint.setAlpha(alpha);
        paint.setStrokeWidth(border * scaleX);
        canvas.drawLine(x1 * scaleX, y1 * scaleY,
                x2 * scaleX, y2 * scaleY, paint);
    }

    @Override
    public void drawLine(Point p1, Point p2, int border, int color, int alpha) {
        paint.setColor(color);
        paint.setAlpha(alpha);
        paint.setStrokeWidth(border * scaleX);
        canvas.drawLine(p1.getX() * scaleX, p1.getY() * scaleY,
                p2.getX() * scaleX, p2.getY() * scaleY, paint);
    }

    @Override
    public void drawLine(Line line, float alpha, int color, float offsetX, float offsetY) {

    }

    @Override
    public void drawShadowLine(Line line, float alpha, int color, float offsetX, float offsetY) {

    }

    @Override
    public void drawARGB(int a, int r, int g, int b) {
        paint.setStyle(Style.FILL);
        canvas.drawARGB(a, r, g, b);
    }

    @Override
    public void drawString(String text, float x, float y, Paint paint, int alpha) {
        paint.setAlpha(alpha);
        paint.setTextSize(paint.getTextSize() * scaleX);
        canvas.drawText(text, x * scaleX, y * scaleY, paint);
        paint.setTextSize(paint.getTextSize() / scaleX);
    }

    @Override
    public void drawString(String text, Point coordinate, Paint paint, int alpha) {
        paint.setAlpha(alpha);
        paint.setTextSize(paint.getTextSize() * scaleX);
        canvas.drawText(text, coordinate.getX() * scaleX, coordinate.getY() * scaleY, paint);
        paint.setTextSize(paint.getTextSize() / scaleX);
    }

    @Override
    public void drawShadowString(String text, float x, float y, Paint paint, int shadowColor, int alpha) {
        paint.setAlpha(alpha);
        paint.setTextSize(paint.getTextSize() * scaleX);
        paint.setShadowLayer(5, 0, 5, shadowColor);
        canvas.drawText(text, x * scaleX, y * scaleY, paint);
        paint.setTextSize(paint.getTextSize() / scaleX);
        paint.clearShadowLayer();
    }

    @Override
    public void drawShadowString(String text, Point coordinate, Paint paint, int shadowColor, int alpha) {
        paint.setAlpha(alpha);
        paint.setTextSize(paint.getTextSize() * scaleX);
        paint.setShadowLayer(5, 0, 5, shadowColor);
        canvas.drawText(text, coordinate.getX() * scaleX,
                coordinate.getY() * scaleY, paint);
        paint.setTextSize(paint.getTextSize() / scaleX);
        paint.clearShadowLayer();
    }

    @Override
    public void drawImage(Image image, float x, float y) {
        int desWidth = image.getWidth();
        int desHeight = image.getHeight();

        dstRect.left = (int) x;
        dstRect.top = (int) y;
        dstRect.right = (int) (x + desWidth);
        dstRect.bottom = (int) (y + desHeight);

        dstRect.left *= scaleX;
        dstRect.top *= scaleY;
        dstRect.right *= scaleX;
        dstRect.bottom *= scaleY;

        canvas.drawBitmap(image.getBitmap(), null, dstRect, imagePaint);
    }

    @Override
    public void drawImage(Image image, float x, float y, int alpha) {
        imagePaint.setAlpha(alpha);

        int desWidth = image.getWidth();
        int desHeight = image.getHeight();

        dstRect.left = (int) x - desWidth / 2;
        dstRect.top = (int) y - desHeight / 2;
        dstRect.right = (int) (x + desWidth / 2);
        dstRect.bottom = (int) (y + desHeight / 2);

        dstRect.left *= scaleX;
        dstRect.top *= scaleY;
        dstRect.right *= scaleX;
        dstRect.bottom *= scaleY;

        canvas.drawBitmap(image.getBitmap(), null, dstRect, imagePaint);
    }

    @Override
    public void drawImage(Image image, float x, float y, int alpha, boolean isPushed,
                          @Nullable String color, @Nullable PorterDuff.Mode cMode) {
        imagePaint.setAlpha(alpha);
        setColorFilter(color, cMode);

        int desWidth;
        int desHeight;
        if (isPushed) {
            float pushRatio = ((float) image.getWidth()) / ((float) image.getHeight());
            if (pushRatio >= 1) {
                desWidth = image.getWidth() - PUSH_DOWN_OFFSET;
                desHeight = image.getHeight() - ((int) (PUSH_DOWN_OFFSET / pushRatio));
            } else {
                desWidth = image.getWidth() - ((int) (PUSH_DOWN_OFFSET * pushRatio));
                desHeight = image.getHeight() - PUSH_DOWN_OFFSET;
            }
        } else {
            desWidth = image.getWidth();
            desHeight = image.getHeight();
        }

        dstRect.left = (int) x - desWidth / 2;
        dstRect.top = (int) y - desHeight / 2;
        dstRect.right = (int) (x + desWidth / 2);
        dstRect.bottom = (int) (y + desHeight / 2);

        dstRect.left *= scaleX;
        dstRect.top *= scaleY;
        dstRect.right *= scaleX;
        dstRect.bottom *= scaleY;

        canvas.drawBitmap(image.getBitmap(), null, dstRect, imagePaint);
    }

    @Override
    public void drawImage(Image image, Point coordinate, int alpha, boolean isPushed,
                          @Nullable String color, @Nullable PorterDuff.Mode cMode) {
        imagePaint.setAlpha(alpha);
        setColorFilter(color, cMode);

        int desWidth;
        int desHeight;
        if (isPushed) {
            float pushRatio = ((float) image.getWidth()) / ((float) image.getHeight());
            if (pushRatio >= 1) {
                desWidth = image.getWidth() - PUSH_DOWN_OFFSET;
                desHeight = image.getHeight() - ((int) (PUSH_DOWN_OFFSET / pushRatio));
            } else {
                desWidth = image.getWidth() - ((int) (PUSH_DOWN_OFFSET * pushRatio));
                desHeight = image.getHeight() - PUSH_DOWN_OFFSET;
            }
        } else {
            desWidth = image.getWidth();
            desHeight = image.getHeight();
        }

        dstRect.left = (int) (coordinate.getX() - desWidth / 2);
        dstRect.top = (int) (coordinate.getY() - desHeight / 2);
        dstRect.right = (int) (coordinate.getX() + desWidth / 2);
        dstRect.bottom = (int) (coordinate.getY() + desHeight / 2);

        dstRect.left *= scaleX;
        dstRect.top *= scaleY;
        dstRect.right *= scaleX;
        dstRect.bottom *= scaleY;

        canvas.drawBitmap(image.getBitmap(), null, dstRect, imagePaint);
    }

    @Override
    public void drawScaledImage(Image image, float x, float y, int width, int height,
                                int alpha, boolean isPushed, @Nullable String color, @Nullable PorterDuff.Mode cMode) {
        imagePaint.setAlpha(alpha);
        setColorFilter(color, cMode);

        int desWidth;
        int desHeight;
        if (isPushed) {
            float pushRatio = ((float) width) / ((float) height);
            if (pushRatio >= 1) {
                desWidth = abs(width) - PUSH_DOWN_OFFSET;
                desHeight = abs(height) - ((int) (PUSH_DOWN_OFFSET / pushRatio));
            } else {
                desWidth = abs(width) - ((int) (PUSH_DOWN_OFFSET * pushRatio));
                desHeight = abs(height) - PUSH_DOWN_OFFSET;
            }
        } else {
            desWidth = abs(width);
            desHeight = abs(height);
        }

        dstRect.left = (int) (x - desWidth / 2);
        dstRect.top = (int) (y - desHeight / 2);
        dstRect.right = (int) (x + desWidth / 2);
        dstRect.bottom = (int) (y + desHeight / 2);

        // scale the bitmap
        dstRect.left *= scaleX;
        dstRect.top *= scaleY;
        dstRect.right *= scaleX;
        dstRect.bottom *= scaleY;

        if (width < 0 || height < 0) {
            canvas.drawBitmap(scaleBitmap(image.getBitmap(), width < 0 ? -1 : 1, height < 0 ? -1 : 1),
                    null, dstRect, imagePaint);
        } else {
            canvas.drawBitmap(image.getBitmap(), null, dstRect, imagePaint);
        }
    }

    @Override
    public void drawScaledImage(Image image, Point coordinate, int width, int height,
                                int alpha, boolean isPushed, @Nullable String color, @Nullable PorterDuff.Mode cMode) {
        imagePaint.setAlpha(alpha);
        setColorFilter(color, cMode);

        int desWidth;
        int desHeight;
        if (isPushed) {
            float pushRatio = ((float) width) / ((float) height);
            if (pushRatio >= 1) {
                desWidth = abs(width) - PUSH_DOWN_OFFSET;
                desHeight = abs(height) - ((int) (PUSH_DOWN_OFFSET / pushRatio));
            } else {
                desWidth = abs(width) - ((int) (PUSH_DOWN_OFFSET * pushRatio));
                desHeight = abs(height) - PUSH_DOWN_OFFSET;
            }
        } else {
            desWidth = abs(width);
            desHeight = abs(height);
        }

        dstRect.left = (int) (coordinate.getX() - desWidth / 2);
        dstRect.top = (int) (coordinate.getY() - desHeight / 2);
        dstRect.right = (int) (coordinate.getX() + desWidth / 2);
        dstRect.bottom = (int) (coordinate.getY() + desHeight / 2);

        // scale the bitmap
        dstRect.left *= scaleX;
        dstRect.top *= scaleY;
        dstRect.right *= scaleX;
        dstRect.bottom *= scaleY;

        if (width < 0 || height < 0) {
            canvas.drawBitmap(scaleBitmap(image.getBitmap(), width < 0 ? -1 : 1, height < 0 ? -1 : 1),
                    null, dstRect, imagePaint);
        } else {
            canvas.drawBitmap(image.getBitmap(), null, dstRect, imagePaint);
        }
    }

    @Override
    public void drawRotateImage(Image image, float x, float y, float degree, int alpha,
                                boolean isPushed, @Nullable String color, @Nullable PorterDuff.Mode cMode) {
        imagePaint.setAlpha(alpha);
        setColorFilter(color, cMode);

        int desWidth;
        int desHeight;
        if (isPushed) {
            float pushRatio = ((float) image.getWidth()) / ((float) image.getHeight());
            if (pushRatio >= 1) {
                desWidth = image.getWidth() - PUSH_DOWN_OFFSET;
                desHeight = image.getHeight() - ((int) (PUSH_DOWN_OFFSET / pushRatio));
            } else {
                desWidth = image.getWidth() - ((int) (PUSH_DOWN_OFFSET * pushRatio));
                desHeight = image.getHeight() - PUSH_DOWN_OFFSET;
            }
        } else {
            desWidth = image.getWidth();
            desHeight = image.getHeight();
        }

        dstRect.left = (int) (x - desWidth / 2);
        dstRect.top = (int) (y - desHeight / 2);
        dstRect.right = (int) (x + desWidth / 2);
        dstRect.bottom = (int) (y + desHeight / 2);

        dstRect.left *= scaleX;
        dstRect.top *= scaleY;
        dstRect.right *= scaleX;
        dstRect.bottom *= scaleY;

        canvas.rotate(degree, x * scaleX, y * scaleY);
        canvas.drawBitmap(image.getBitmap(), null, dstRect, imagePaint);
        canvas.rotate(-degree, x * scaleX, y * scaleY);
    }

    @Override
    public void drawRotateImage(Image image, Point coordinate, float degree, int alpha,
                                boolean isPushed, @Nullable String color, @Nullable PorterDuff.Mode cMode) {
        imagePaint.setAlpha(alpha);
        setColorFilter(color, cMode);

        int desWidth;
        int desHeight;
        if (isPushed) {
            float pushRatio = ((float) image.getWidth()) / ((float) image.getHeight());
            if (pushRatio >= 1) {
                desWidth = image.getWidth() - PUSH_DOWN_OFFSET;
                desHeight = image.getHeight() - ((int) (PUSH_DOWN_OFFSET / pushRatio));
            } else {
                desWidth = image.getWidth() - ((int) (PUSH_DOWN_OFFSET * pushRatio));
                desHeight = image.getHeight() - PUSH_DOWN_OFFSET;
            }
        } else {
            desWidth = image.getWidth();
            desHeight = image.getHeight();
        }

        dstRect.left = (int) coordinate.getX() - desWidth / 2;
        dstRect.top = (int) coordinate.getY() - desHeight / 2;
        dstRect.right = (int) (coordinate.getX() + desWidth / 2);
        dstRect.bottom = (int) (coordinate.getY() + desHeight / 2);

        dstRect.left *= scaleX;
        dstRect.top *= scaleY;
        dstRect.right *= scaleX;
        dstRect.bottom *= scaleY;

        canvas.rotate(degree, coordinate.getX() * scaleX, coordinate.getY() * scaleY);
        canvas.drawBitmap(image.getBitmap(), null, dstRect, imagePaint);
        canvas.rotate(-degree, coordinate.getX() * scaleX, coordinate.getY() * scaleY);
    }

    @Override
    public void drawScaledRotatedImage(Image image, float x, float y, int width, int height,
                                       float degree, int alpha, boolean isPushed,
                                       @Nullable String color, @Nullable PorterDuff.Mode cMode) {
        imagePaint.setAlpha(alpha);
        setColorFilter(color, cMode);

        int desWidth;
        int desHeight;
        if (isPushed) {
            float pushRatio = ((float) width) / ((float) height);
            if (pushRatio >= 1) {
                desWidth = abs(width) - PUSH_DOWN_OFFSET;
                desHeight = abs(height) - ((int) (PUSH_DOWN_OFFSET / pushRatio));
            } else {
                desWidth = abs(width) - ((int) (PUSH_DOWN_OFFSET * pushRatio));
                desHeight = abs(height) - PUSH_DOWN_OFFSET;
            }
        } else {
            desWidth = abs(width);
            desHeight = abs(height);
        }

        dstRect.left = (int) x - desWidth / 2;
        dstRect.top = (int) y - desHeight / 2;
        dstRect.right = (int) (x + desWidth / 2);
        dstRect.bottom = (int) (y + desHeight / 2);

        // scale the bitmap
        dstRect.left *= scaleX;
        dstRect.top *= scaleY;
        dstRect.right *= scaleX;
        dstRect.bottom *= scaleY;


        canvas.rotate(degree, x * scaleX, y * scaleY);
        if (width < 0 || height < 0) {
            canvas.drawBitmap(scaleBitmap(image.getBitmap(), width < 0 ? -1 : 1, height < 0 ? -1 : 1),
                    null, dstRect, imagePaint);
        } else {
            canvas.drawBitmap(image.getBitmap(), null, dstRect, imagePaint);
        }
        canvas.rotate(-degree, x * scaleX, y * scaleY);
    }

    @Override
    public void drawScaledRotatedImage(Image image, Point coordinate, int width, int height,
                                       float degree, int alpha, boolean isPushed,
                                       @Nullable String color, @Nullable PorterDuff.Mode cMode) {
        imagePaint.setAlpha(alpha);
        setColorFilter(color, cMode);

        int desWidth;
        int desHeight;
        if (isPushed) {
            float pushRatio = ((float) width) / ((float) height);
            if (pushRatio >= 1) {
                desWidth = abs(width) - PUSH_DOWN_OFFSET;
                desHeight = abs(height) - ((int) (PUSH_DOWN_OFFSET / pushRatio));
            } else {
                desWidth = abs(width) - ((int) (PUSH_DOWN_OFFSET * pushRatio));
                desHeight = abs(height) - PUSH_DOWN_OFFSET;
            }
        } else {
            desWidth = abs(width);
            desHeight = abs(height);
        }

        dstRect.left = (int) (coordinate.getX() - desWidth / 2);
        dstRect.top = (int) (coordinate.getY() - desHeight / 2);
        dstRect.right = (int) (coordinate.getX() + desWidth / 2);
        dstRect.bottom = (int) (coordinate.getY() + desHeight / 2);

        // scale the bitmap
        dstRect.left *= scaleX;
        dstRect.top *= scaleY;
        dstRect.right *= scaleX;
        dstRect.bottom *= scaleY;

        canvas.rotate(degree, coordinate.getX() * scaleX, coordinate.getY() * scaleY);
        if (width < 0 || height < 0) {
            canvas.drawBitmap(scaleBitmap(image.getBitmap(), width < 0 ? -1 : 1, height < 0 ? -1 : 1),
                    null, dstRect, imagePaint);
        } else {
            canvas.drawBitmap(image.getBitmap(), null, dstRect, imagePaint);
        }
        canvas.rotate(-degree, coordinate.getX() * scaleX, coordinate.getY() * scaleY);
    }

    @Override
    public void drawCroppedImage(Image image, float x, float y, int srcX, int srcY,
                                 int srcWidth, int srcHeight, int alpha, boolean isPushed,
                                 @Nullable String color, @Nullable PorterDuff.Mode cMode) {
        imagePaint.setAlpha(alpha);
        setColorFilter(color, cMode);

        int desWidth;
        int desHeight;
        if (isPushed) {
            float pushRatio = ((float) srcWidth) / ((float) srcHeight);
            if (pushRatio >= 1) {
                desWidth = srcWidth - PUSH_DOWN_OFFSET;
                desHeight = srcHeight - ((int) (PUSH_DOWN_OFFSET / pushRatio));
            } else {
                desWidth = srcWidth - ((int) (PUSH_DOWN_OFFSET * pushRatio));
                desHeight = srcHeight - PUSH_DOWN_OFFSET;
            }
        } else {
            desWidth = srcWidth;
            desHeight = srcHeight;
        }

        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = (srcX + srcWidth);
        srcRect.bottom = (srcY + srcHeight);

        dstRect.left = (int) (x - desWidth / 2);
        dstRect.top = (int) (y - desHeight / 2);
        dstRect.right = (int) (x + desWidth / 2);
        dstRect.bottom = (int) (y + desHeight / 2);

        dstRect.left *= scaleX;
        dstRect.top *= scaleY;
        dstRect.right *= scaleX;
        dstRect.bottom *= scaleY;

        canvas.drawBitmap(image.getBitmap(), srcRect, dstRect, imagePaint);
    }

    @Override
    public void drawCroppedImage(Image image, Point coordinate, int srcX, int srcY,
                                 int srcWidth, int srcHeight, int alpha, boolean isPushed,
                                 @Nullable String color, @Nullable PorterDuff.Mode cMode) {
        imagePaint.setAlpha(alpha);
        setColorFilter(color, cMode);

        int desWidth;
        int desHeight;
        if (isPushed) {
            float pushRatio = ((float) srcWidth) / ((float) srcHeight);
            if (pushRatio >= 1) {
                desWidth = srcWidth - PUSH_DOWN_OFFSET;
                desHeight = srcHeight - ((int) (PUSH_DOWN_OFFSET / pushRatio));
            } else {
                desWidth = srcWidth - ((int) (PUSH_DOWN_OFFSET * pushRatio));
                desHeight = srcHeight - PUSH_DOWN_OFFSET;
            }
        } else {
            desWidth = srcWidth;
            desHeight = srcHeight;
        }

        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = (srcX + srcWidth);
        srcRect.bottom = (srcY + srcHeight);

        dstRect.left = (int) (coordinate.getX() - desWidth / 2);
        dstRect.top = (int) (coordinate.getY() - desHeight / 2);
        dstRect.right = (int) (coordinate.getX() + desWidth / 2);
        dstRect.bottom = (int) (coordinate.getY() + desHeight / 2);

        dstRect.left *= scaleX;
        dstRect.top *= scaleY;
        dstRect.right *= scaleX;
        dstRect.bottom *= scaleY;

        canvas.drawBitmap(image.getBitmap(), srcRect, dstRect, imagePaint);
    }

    @Override
    public void drawScaledCroppedImage(Image image, float x, float y, int width, int height, int srcX,
                                       int srcY, int srcWidth, int srcHeight, int alpha, boolean isPushed,
                                       @Nullable String color, @Nullable PorterDuff.Mode cMode) {
        imagePaint.setAlpha(alpha);
        setColorFilter(color, cMode);

        int desWidth;
        int desHeight;
        if (isPushed) {
            float pushRatio = ((float) width) / ((float) height);
            if (pushRatio >= 1) {
                desWidth = abs(width) - PUSH_DOWN_OFFSET;
                desHeight = abs(height) - ((int) (PUSH_DOWN_OFFSET / pushRatio));
            } else {
                desWidth = abs(width) - ((int) (PUSH_DOWN_OFFSET * pushRatio));
                desHeight = abs(height) - PUSH_DOWN_OFFSET;
            }
        } else {
            desWidth = abs(width);
            desHeight = abs(height);
        }

        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = (srcX + srcWidth);
        srcRect.bottom = (srcY + srcHeight);

        dstRect.left = (int) (x - desWidth / 2);
        dstRect.top = (int) (y - desHeight / 2);
        dstRect.right = (int) (x + desWidth / 2);
        dstRect.bottom = (int) (y + desHeight / 2);

        dstRect.left *= scaleX;
        dstRect.top *= scaleY;
        dstRect.right *= scaleX;
        dstRect.bottom *= scaleY;

        if (width < 0 || height < 0) {
            canvas.drawBitmap(scaleBitmap(image.getBitmap(), width < 0 ? -1 : 1, height < 0 ? -1 : 1),
                    srcRect, dstRect, imagePaint);
        } else {
            canvas.drawBitmap(image.getBitmap(), srcRect, dstRect, imagePaint);
        }
    }

    @Override
    public void drawScaledCroppedImage(Image image, Point coordinate, int width, int height, int srcX,
                                       int srcY, int srcWidth, int srcHeight, int alpha, boolean isPushed,
                                       @Nullable String color, @Nullable PorterDuff.Mode cMode) {
        imagePaint.setAlpha(alpha);
        setColorFilter(color, cMode);

        int desWidth;
        int desHeight;
        if (isPushed) {
            float pushRatio = ((float) width) / ((float) height);
            if (pushRatio >= 1) {
                desWidth = abs(width) - PUSH_DOWN_OFFSET;
                desHeight = abs(height) - ((int) (PUSH_DOWN_OFFSET / pushRatio));
            } else {
                desWidth = abs(width) - ((int) (PUSH_DOWN_OFFSET * pushRatio));
                desHeight = abs(height) - PUSH_DOWN_OFFSET;
            }
        } else {
            desWidth = abs(width);
            desHeight = abs(height);
        }

        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = (srcX + srcWidth);
        srcRect.bottom = (srcY + srcHeight);

        dstRect.left = (int) (coordinate.getX() - desWidth / 2);
        dstRect.top = (int) (coordinate.getY() - desHeight / 2);
        dstRect.right = (int) (coordinate.getX() + desWidth / 2);
        dstRect.bottom = (int) (coordinate.getY() + desHeight / 2);

        dstRect.left *= scaleX;
        dstRect.top *= scaleY;
        dstRect.right *= scaleX;
        dstRect.bottom *= scaleY;

        if (width < 0 || height < 0) {
            canvas.drawBitmap(scaleBitmap(image.getBitmap(), width < 0 ? -1 : 1, height < 0 ? -1 : 1),
                    srcRect, dstRect, imagePaint);
        } else {
            canvas.drawBitmap(image.getBitmap(), srcRect, dstRect, imagePaint);
        }
    }

    @Override
    public void drawRotateCroppedImage(Image image, float x, float y, int srcX, int srcY, int srcWidth,
                                       int srcHeight, float degree, int alpha, boolean isPushed,
                                       @Nullable String color, @Nullable PorterDuff.Mode cMode) {
        imagePaint.setAlpha(alpha);
        setColorFilter(color, cMode);

        int desWidth;
        int desHeight;
        if (isPushed) {
            float pushRatio = ((float) srcWidth) / ((float) srcHeight);
            if (pushRatio >= 1) {
                desWidth = srcWidth - PUSH_DOWN_OFFSET;
                desHeight = srcHeight - ((int) (PUSH_DOWN_OFFSET / pushRatio));
            } else {
                desWidth = srcWidth - ((int) (PUSH_DOWN_OFFSET * pushRatio));
                desHeight = srcHeight - PUSH_DOWN_OFFSET;
            }
        } else {
            desWidth = srcWidth;
            desHeight = srcHeight;
        }

        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = (srcX + srcWidth);
        srcRect.bottom = (srcY + srcHeight);

        dstRect.left = (int) (x - desWidth / 2);
        dstRect.top = (int) (y - desHeight / 2);
        dstRect.right = (int) (x + desWidth / 2);
        dstRect.bottom = (int) (y + desHeight / 2);

        dstRect.left *= scaleX;
        dstRect.top *= scaleY;
        dstRect.right *= scaleX;
        dstRect.bottom *= scaleY;

        canvas.rotate(degree, x * scaleX, y * scaleY);
        canvas.drawBitmap(image.getBitmap(), srcRect, dstRect, imagePaint);
        canvas.rotate(-degree, x * scaleX, y * scaleY);
    }

    @Override
    public void drawRotateCroppedImage(Image image, Point coordinate, int srcX, int srcY, int srcWidth,
                                       int srcHeight, float degree, int alpha, boolean isPushed,
                                       @Nullable String color, @Nullable PorterDuff.Mode cMode) {
        imagePaint.setAlpha(alpha);
        setColorFilter(color, cMode);

        int desWidth;
        int desHeight;
        if (isPushed) {
            float pushRatio = ((float) srcWidth) / ((float) srcHeight);
            if (pushRatio >= 1) {
                desWidth = srcWidth - PUSH_DOWN_OFFSET;
                desHeight = srcHeight - ((int) (PUSH_DOWN_OFFSET / pushRatio));
            } else {
                desWidth = srcWidth - ((int) (PUSH_DOWN_OFFSET * pushRatio));
                desHeight = srcHeight - PUSH_DOWN_OFFSET;
            }
        } else {
            desWidth = srcWidth;
            desHeight = srcHeight;
        }

        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = (srcX + srcWidth);
        srcRect.bottom = (srcY + srcHeight);

        dstRect.left = (int) (coordinate.getX() - desWidth / 2);
        dstRect.top = (int) (coordinate.getY() - desHeight / 2);
        dstRect.right = (int) (coordinate.getX() + desWidth / 2);
        dstRect.bottom = (int) (coordinate.getY() + desHeight / 2);

        dstRect.left *= scaleX;
        dstRect.top *= scaleY;
        dstRect.right *= scaleX;
        dstRect.bottom *= scaleY;

        canvas.rotate(degree, coordinate.getX() * scaleX, coordinate.getY() * scaleY);
        canvas.drawBitmap(image.getBitmap(), srcRect, dstRect, imagePaint);
        canvas.rotate(-degree, coordinate.getX() * scaleX, coordinate.getY() * scaleY);
    }

    @Override
    public void drawScaledRotateCroppedImage(Image image, float x, float y, int width, int height, int srcX,
                                             int srcY, int srcWidth, int srcHeight, float degree,
                                             int alpha, boolean isPushed, @Nullable String color,
                                             @Nullable PorterDuff.Mode cMode) {
        imagePaint.setAlpha(alpha);
        setColorFilter(color, cMode);

        int desWidth;
        int desHeight;
        if (isPushed) {
            float pushRatio = ((float) width) / ((float) height);
            if (pushRatio >= 1) {
                desWidth = abs(width) - PUSH_DOWN_OFFSET;
                desHeight = abs(height) - ((int) (PUSH_DOWN_OFFSET / pushRatio));
            } else {
                desWidth = abs(width) - ((int) (PUSH_DOWN_OFFSET * pushRatio));
                desHeight = abs(height) - PUSH_DOWN_OFFSET;
            }
        } else {
            desWidth = abs(width);
            desHeight = abs(height);
        }

        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = (srcX + srcWidth);
        srcRect.bottom = (srcY + srcHeight);

        dstRect.left = (int) (x - desWidth / 2);
        dstRect.top = (int) (y - desHeight / 2);
        dstRect.right = (int) (x + desWidth / 2);
        dstRect.bottom = (int) (y + desHeight / 2);

        dstRect.left *= scaleX;
        dstRect.top *= scaleY;
        dstRect.right *= scaleX;
        dstRect.bottom *= scaleY;

        canvas.rotate(degree, x * scaleX, y * scaleY);
        if (width < 0 || height < 0) {
            canvas.drawBitmap(scaleBitmap(image.getBitmap(), width < 0 ? -1 : 1, height < 0 ? -1 : 1),
                    srcRect, dstRect, imagePaint);
        } else {
            canvas.drawBitmap(image.getBitmap(), srcRect, dstRect, imagePaint);
        }
        canvas.rotate(-degree, x * scaleX, y * scaleY);
    }

    @Override
    public void drawScaledRotateCroppedImage(Image image, Point coordinate, int width, int height, int srcX,
                                             int srcY, int srcWidth, int srcHeight, float degree,
                                             int alpha, boolean isPushed, @Nullable String color,
                                             @Nullable PorterDuff.Mode cMode) {
        imagePaint.setAlpha(alpha);
        setColorFilter(color, cMode);

        int desWidth;
        int desHeight;
        if (isPushed) {
            float pushRatio = ((float) width) / ((float) height);
            if (pushRatio >= 1) {
                desWidth = abs(width) - PUSH_DOWN_OFFSET;
                desHeight = abs(height) - ((int) (PUSH_DOWN_OFFSET / pushRatio));
            } else {
                desWidth = abs(width) - ((int) (PUSH_DOWN_OFFSET * pushRatio));
                desHeight = abs(height) - PUSH_DOWN_OFFSET;
            }
        } else {
            desWidth = abs(width);
            desHeight = abs(height);
        }

        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = (srcX + srcWidth);
        srcRect.bottom = (srcY + srcHeight);

        dstRect.left = (int) (coordinate.getX() - desWidth / 2);
        dstRect.top = (int) (coordinate.getY() - desHeight / 2);
        dstRect.right = (int) (coordinate.getX() + desWidth / 2);
        dstRect.bottom = (int) (coordinate.getY() + desHeight / 2);

        dstRect.left *= scaleX;
        dstRect.top *= scaleY;
        dstRect.right *= scaleX;
        dstRect.bottom *= scaleY;

        canvas.rotate(degree, coordinate.getX() * scaleX, coordinate.getY() * scaleY);
        if (width < 0 || height < 0) {
            canvas.drawBitmap(scaleBitmap(image.getBitmap(), width < 0 ? -1 : 1, height < 0 ? -1 : 1),
                    srcRect, dstRect, imagePaint);
        } else {
            canvas.drawBitmap(image.getBitmap(), srcRect, dstRect, imagePaint);
        }
        canvas.rotate(-degree, coordinate.getX() * scaleX, coordinate.getY() * scaleY);
    }

    public void drawScaledImageWithShadow(Image image, float desX, float desY, float desWidth, float desHeight, int shadowOffsetX, int shadowOffsetY) {

        dstRect.left = (int) desX + shadowOffsetX;
        dstRect.top = (int) desY + shadowOffsetY;
        dstRect.right = (int) (desX + desWidth + shadowOffsetX);
        dstRect.bottom = (int) (desY + desHeight + shadowOffsetY);

        // scale the bitmap
        dstRect.left *= scaleX;
        dstRect.top *= scaleY;
        dstRect.right *= scaleX;
        dstRect.bottom *= scaleY;

        ColorFilter cf = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);

        paint.setColorFilter(cf);

        paint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));
        canvas.drawBitmap(image.getBitmap(), null, dstRect, paint);
        paint.setColorFilter(null);
        paint.setMaskFilter(null);

        dstRect.left = (int) desX;
        dstRect.top = (int) desY;
        dstRect.right = (int) (desX + desWidth);
        dstRect.bottom = (int) (desY + desHeight);

        // scale the bitmap
        dstRect.left *= scaleX;
        dstRect.top *= scaleY;
        dstRect.right *= scaleX;
        dstRect.bottom *= scaleY;

        canvas.drawBitmap(image.getBitmap(), null, dstRect, null);

    }


    @Override
    public int getWidth() {
        return frameBuffer.getWidth();
    }

    @Override
    public int getHeight() {
        return frameBuffer.getHeight();
    }

    private Bitmap scaleBitmap(Bitmap source, int scaleX, int scaleY) {
        Matrix matrix = new Matrix();
        matrix.preScale(scaleX, scaleY, source.getWidth() / 2, source.getHeight() / 2);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private void setColorFilter(@Nullable String color, @Nullable PorterDuff.Mode cMode) {
        if (color != null && cMode != null) {
            ColorFilter cf = new PorterDuffColorFilter(Color.parseColor("#FF" + color), cMode);
            imagePaint.setColorFilter(cf);
        } else {
            imagePaint.setColorFilter(null);
        }
    }

}