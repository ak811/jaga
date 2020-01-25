package com.qkedy.jaga.interfaces;

public interface Music {

    /**
     * @apiNote play the music if it is not in playing mode
     */
    void play();

    /**
     * @apiNote stop the music if it is in playing mode
     */
    void stop();

    /**
     * @apiNote pause the music if it is not in playing mode
     */
    void pause();

    /**
     * @param looping if true, the music will play again after ending
     */
    void setLooping(boolean looping);

    /**
     * @return true if any music is still in playing mode
     */
    boolean isPlaying();

    /**
     * @return true if music is in the stop mode
     */
    boolean isStopped();

    /**
     * @return true if music set for looping
     */
    boolean isLooping();

    /**
     * @param volume the right snd left volume of the music from 0 to 1
     */
    void setVolume(float volume);

    /**
     * @apiNote stop or release the music from mediaPlayer
     */
    void dispose();

    /**
     * @apiNote seek to the beginning of the music
     */
    void seekBegin();
}
