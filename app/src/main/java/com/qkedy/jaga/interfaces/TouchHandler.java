package com.qkedy.jaga.interfaces;

import android.view.View.OnTouchListener;

import com.qkedy.jaga.models.Point;

import java.util.List;

public interface TouchHandler extends OnTouchListener {

    /**
     * @param touchId the id of specific touch event
     * @return true if the touch event that specified with this touch id be a touch-down event
     */
    boolean isTouchDown(int touchId);

    /**
     * @param touchId the id of specific touch event
     * @return the point of the touch event specified with this touch id
     */
    Point getTouchX(int touchId);

    /**
     * @return a list of touch events in the buffer
     */
    List<Input.TouchEvent> getTouchEvents();
}