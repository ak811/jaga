package com.qkedy.jaga.interfaces;

import android.app.Activity;

import org.json.JSONException;

public interface Game {

    Audio getAudio();

    Input getInput();

    FileIO getFileIO();

    Graphics getGraphics();

    void setScreen(Screen screen);

    Screen getCurrentScreen();

    Screen getInitScreen() throws JSONException;

    Activity getActivity();
}
