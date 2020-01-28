package com.qkedy.jaga.interfaces;

import android.view.View.OnTouchListener;

import com.qkedy.jaga.models.Point;

import java.util.List;

public interface TouchHandler extends OnTouchListener {

    /**
     * @param pointerId the id of specific touch event
     * @return true if the touch event that specified with this touch id be a touches
     * the screen in touch-down and touch-move events. false if the type of touch event
     * that specified with this touch id is touch=up
     */
    boolean isScreenTouchedByThisTouchId(int pointerId);

    /**
     * @param pointerId the id of specific touch event
     * @return the point of the touch event specified with this touch id
     */
    Point getTouchPoint(int pointerId);

    /**
     * @return a list of touch events in the buffer
     */
    List<Input.TouchEvent> getTouchEvents();
}