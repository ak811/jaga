package com.qkedy.jaga.interfaces;

import com.qkedy.jaga.entities.Point;

import java.util.List;

public interface Input {

    class TouchEvent {

        /**
         * @apiNote hand movements of the user:
         * TOUCH_DOWN: when the finger touches the screen, touch_down event will be called.
         * TOUCH_UP: when the finger touches the screen and then picks up, touch_up event will be called.
         * TOUCH_MOVE: when finger moves on the screen, touch_move event will be called.
         */
        public static final int TOUCH_DOWN = 0;
        public static final int TOUCH_UP = 1;
        public static final int TOUCH_MOVE = 2;

        /**
         * @apiNote values of 'type' variable: TOUCH_DOWN, TOUCH_UP, OUCH_DRAGGED, TOUCH_HOLD
         */
        private int type;

        /**
         * @apiNote point of finger touch event.
         */
        private Point point;

        /**
         * @apiNote when simultaneous touches occur, each touch will specify with an id, respectively.
         */
        private int touchId;

        public int getType() {
            return type;
        }

        public Point getPoint() {
            return point;
        }

        public int getTouchId() {
            return touchId;
        }
    }

    boolean isTouchDown(int pointer);

    int getTouchX(int pointer);

    int getTouchY(int pointer);

    /**
     * @return a list of touch events in the buffer
     */
    List<TouchEvent> getTouchEvents();
}
 