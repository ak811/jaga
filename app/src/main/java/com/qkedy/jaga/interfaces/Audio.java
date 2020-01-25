package com.qkedy.jaga.interfaces;

public interface Audio {

    /**
     * @param fileName the name of the music file
     * @return the converted file to Music
     */
    Music createMusic(String fileName);

    /**
     * @param fileName the name of the sound file
     * @return the converted file to Sound
     */
    Sound createSound(String fileName);
}
