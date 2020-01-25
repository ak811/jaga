package com.qkedy.jaga.interfaces;

import com.qkedy.jaga.models.Point;

public interface Input extends TouchHandler {

    class TouchEvent {

        /**
         * @apiNote touch movements of the user:
         * TOUCH_DOWN: when the finger touches the screen, touch-down event will be called.
         * TOUCH_UP: when the finger touches the screen and then picks up, touch-up event will be called.
         * TOUCH_MOVE: when finger moves on the screen, touch-move event will be called.
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

        /**
         * @return the type of the touch event
         */
        public int getType() {
            return type;
        }

        /**
         * @return the point of the touch event
         */
        public Point getPoint() {
            return point;
        }

        /**
         * @return the touchId of the touch event
         */
        public int getTouchId() {
            return touchId;
        }
    }
}
 