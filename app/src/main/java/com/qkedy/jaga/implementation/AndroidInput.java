package com.qkedy.jaga.implementation;

import android.content.Context;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;

import com.qkedy.jaga.interfaces.Input;
import com.qkedy.jaga.interfaces.TouchHandler;
import com.qkedy.jaga.models.Point;

import java.util.List;

public class AndroidInput implements Input {

    private TouchHandler touchHandler;

    public AndroidInput(Context context, View view, float scaleX, float scaleY) {
        if (Integer.parseInt(Build.VERSION.SDK) < 5)
            touchHandler = new SingleTouchHandler(view, scaleX, scaleY);
        else
            touchHandler = new MultiTouchHandler(view, scaleX, scaleY);
    }

    @Override
    public boolean isScreenTouchedByThisTouchId(int pointerId) {
        return touchHandler.isScreenTouchedByThisTouchId(pointerId);
    }

    @Override
    public Point getTouchPoint(int pointerId) {
        return touchHandler.getTouchPoint(pointerId);
    }

    @Override
    public List<TouchEvent> getTouchEvents() {
        return touchHandler.getTouchEvents();
    }

    public TouchHandler getTouchHandler() {
        return touchHandler;
    }

    public void setTouchHandler(TouchHandler touchHandler) {
        this.touchHandler = touchHandler;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}

