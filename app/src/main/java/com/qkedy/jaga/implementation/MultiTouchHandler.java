package com.qkedy.jaga.implementation;

import android.view.MotionEvent;
import android.view.View;

import com.qkedy.jaga.interfaces.Input;
import com.qkedy.jaga.interfaces.Input.TouchEvent;
import com.qkedy.jaga.interfaces.TouchHandler;
import com.qkedy.jaga.models.Point;
import com.qkedy.jaga.models.Pool;

import java.util.ArrayList;
import java.util.List;

public class MultiTouchHandler implements TouchHandler {

    private static final int MAX_TOUCH_POINTS = 10;

    private boolean[] isTouched = new boolean[MAX_TOUCH_POINTS];
    private Point[] touchPoint = new Point[MAX_TOUCH_POINTS];
    private int[] id = new int[MAX_TOUCH_POINTS];
    private Pool<Input.TouchEvent> touchEventPool;
    private List<Input.TouchEvent> touchEvents = new ArrayList<>();
    private List<Input.TouchEvent> touchEventsBuffer = new ArrayList<>();
    private float scaleX;
    private float scaleY;

    public MultiTouchHandler(View view, float scaleX, float scaleY) {
        Pool.PoolObjectFactory<Input.TouchEvent> factory = Input.TouchEvent::new;
        final int MAX_SIZE = 100;
        touchEventPool = new Pool<>(factory, MAX_SIZE);
        view.setOnTouchListener(this);

        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        synchronized (this) {
            int action = event.getAction() & MotionEvent.ACTION_MASK;
            int pointerIndex = (event.getAction() &
                    MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT;
            int pointerCount = event.getPointerCount();
            Input.TouchEvent touchEvent;
            for (int i = 0; i < MAX_TOUCH_POINTS; i++) {
                if (i >= pointerCount) {
                    isTouched[i] = false;
                    id[i] = -1;
                    continue;
                }
                if (event.getAction() != MotionEvent.ACTION_MOVE && i != pointerIndex) {
                    continue;
                }
                int pointerId = event.getPointerId(i);
                Point point = new Point((int) (event.getX(i) * scaleX), (int) (event.getY(i) * scaleY));
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                        touchEvent = touchEventPool.newObject();
                        touchEvent.setType(TouchEvent.TOUCH_DOWN);
                        touchEvent.setPointerId(pointerId);
                        touchEvent.setPoint(point);
                        touchPoint[i] = point;
                        isTouched[i] = true;
                        id[i] = pointerId;
                        touchEventsBuffer.add(touchEvent);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP:
                    case MotionEvent.ACTION_CANCEL:
                        touchEvent = touchEventPool.newObject();
                        touchEvent.setType(TouchEvent.TOUCH_UP);
                        touchEvent.setPointerId(pointerId);
                        touchEvent.setPoint(point);
                        touchPoint[i].setPoint(point);
                        isTouched[i] = false;
                        id[i] = -1;
                        touchEventsBuffer.add(touchEvent);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        touchEvent = touchEventPool.newObject();
                        touchEvent.setType(TouchEvent.TOUCH_MOVE);
                        touchEvent.setPointerId(pointerId);
                        touchEvent.setPoint(point);
                        touchPoint[i].setPoint(point);
                        isTouched[i] = true;
                        id[i] = pointerId;
                        touchEventsBuffer.add(touchEvent);
                        break;
                }
            }
            return true;
        }
    }

    @Override
    public boolean isScreenTouchedByThisTouchId(int pointerId) {
        synchronized (this) {
            int index = getIndex(pointerId);
            if (index < 0 || index >= MAX_TOUCH_POINTS)
                return false;
            else
                return isTouched[index];
        }
    }

    @Override
    public Point getTouchPoint(int pointerId) {
        synchronized (this) {
            int index = getIndex(pointerId);
            if (index < 0 || index >= MAX_TOUCH_POINTS)
                return null;
            else
                return touchPoint[index];
        }
    }

    @Override
    public List<TouchEvent> getTouchEvents() {
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

    private int getIndex(int pointerId) {
        for (int i = 0; i < MAX_TOUCH_POINTS; i++)
            if (id[i] == pointerId)
                return i;
        return -1;
    }

}
