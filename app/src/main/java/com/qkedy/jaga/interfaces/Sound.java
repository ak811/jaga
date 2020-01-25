package com.qkedy.jaga.interfaces;

public interface Sound {

    /**
     * @param volume the right snd left volume of the sound from 0 to 1
     */
    //todo: add loop and music rate options
    void play(float volume);

    /**
     * @apiNote unload the sound from soundPool
     */
    void dispose();
}
