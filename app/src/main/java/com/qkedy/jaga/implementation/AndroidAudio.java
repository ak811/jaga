package com.qkedy.jaga.implementation;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import androidx.annotation.NonNull;

import com.qkedy.jaga.interfaces.Audio;
import com.qkedy.jaga.interfaces.Music;
import com.qkedy.jaga.interfaces.Sound;

import java.io.IOException;

public class AndroidAudio implements Audio {

    private AssetManager assets;
    private SoundPool soundPool;

    public AndroidAudio(@NonNull Activity activity) {
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        this.assets = activity.getAssets();
        final int MAX_STREAMS = 20;
        final int SRC_QUALITY = 0;
        this.soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, SRC_QUALITY);
    }

    @NonNull
    @Override
    public Music createMusic(@NonNull String fileName) {
        try (AssetFileDescriptor assetDescriptor = assets.openFd(fileName)) {
            return new AndroidMusic(assetDescriptor);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Couldn't load music. fileName: %s", fileName));
        }
    }

    @NonNull
    @Override
    public Sound createSound(@NonNull String fileName) {
        try (AssetFileDescriptor assetDescriptor = assets.openFd(fileName)) {
            int soundId = soundPool.load(assetDescriptor, 0);
            return new AndroidSound(soundPool, soundId);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Couldn't load sound. fileName: %s", fileName));
        }
    }
}
