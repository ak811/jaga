package com.qkedy.jaga.interfaces;

import android.app.Activity;

public interface Game {

    Audio getAudio();

    Input getInput();

    FileIO getFileIO();

    Graphics getGraphics();

    Activity getActivity();
}
