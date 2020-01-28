package com.qkedy.jaga.implementation;

import android.view.MotionEvent;
import android.view.View;

import com.qkedy.jaga.interfaces.Input;
import com.qkedy.jaga.interfaces.TouchHandler;
import com.qkedy.jaga.models.Point;
import com.qkedy.jaga.models.Pool;

import java.util.ArrayList;
import java.util.List;

public class SingleTouchHandler implements TouchHandler {

    private Point point;
    private Pool<Input.TouchEvent> touchEventPool;
    private List<Input.TouchEvent> touchEvents = new ArrayList<>();
    private List<Input.TouchEvent> touchEventsBuffer = new ArrayList<>();
    private float scaleX;
    private float scaleY;
    private boolean isTouched;

    public SingleTouchHandler(View view, float scaleX, float scaleY) {
        final int MAX_SIZE = 100;
        Pool.PoolObjectFactory<Input.TouchEvent> factory = Input.TouchEvent::new;
        touchEventPool = new Pool<>(factory, MAX_SIZE);
        view.setOnTouchListener(this);

        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        synchronized (this) {
            Input.TouchEvent touchEvent = touchEventPool.newObject();
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touchEvent.setType(Input.TouchEvent.TOUCH_DOWN);
                    isTouched = true;
                    break;
                case MotionEvent.ACTION_SCROLL:
                case MotionEvent.ACTION_MOVE:
                    touchEvent.setType(Input.TouchEvent.TOUCH_MOVE);
                    isTouched = true;
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    touchEvent.setType(Input.TouchEvent.TOUCH_UP);
                    isTouched = false;
                    break;
            }

            float x = (int) (motionEvent.getX() * scaleX);
            float y = (int) (motionEvent.getY() * scaleY);

            point.setX(x);
            point.setY(y);

            touchEvent.setPoint(point);

            touchEventsBuffer.add(touchEvent);

            return true;
        }
    }

    @Override
    public boolean isScreenTouchedByThisTouchId(int pointerId) {
        synchronized (this) {
            return pointerId == 0 && isTouched;
        }
    }

    @Override
    public Point getTouchPoint(int pointerId) {
        return point;
    }

    @Override
    public List<Input.TouchEvent> getTouchEvents() {
        synchronized (this) {
            int len = touchEvents.size();
            for (int i = 0; i < len; i++)
                touchEventPool.free(touchEvents.get(i));
            touchEvents.clear();
            touchEvents.addAll(touchEventsBuffer);
            touchEventsBuffer.clear();
            return touchEvents;
        }
    }

    public boolean isTouched() {
        return isTouched;
    }

    public void setTouched(boolean touched) {
        isTouched = touched;
    }
}
