package io.github.SoldierVsZombies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class SoundManager {
    private final Music[] sounds = new Music[SoundEffects.values().length];

    public SoundManager() {
        initializeSounds();
        sounds[SoundEffects.background.getValue()].setLooping(true);
    }
    private void initializeSounds() {
        for (SoundEffects soundEffect: SoundEffects.values()) {
            sounds[soundEffect.getValue()] = Gdx.audio.newMusic(Gdx.files.internal(soundEffect.getFileName()));
        }
    }
    public void playSoundEffect(SoundEffects soundEffect,float volume) {
        sounds[soundEffect.getValue()].setVolume(volume);
        sounds[soundEffect.getValue()].play();
    }
    public  void pauseSoundEffect(SoundEffects soundEffect) {
        sounds[soundEffect.getValue()].pause();
    }
    public void stopSoundEffect(SoundEffects soundEffect) {
        sounds[soundEffect.getValue()].stop();
    }
    public void setVolume(SoundEffects soundEffect, float volume) {
        sounds[soundEffect.getValue()].setVolume(volume);
    }

    public boolean isPlayingSoundEffect(SoundEffects soundEffect) {
        return sounds[soundEffect.getValue()].isPlaying();
    }

    public void dispose() {
        for (Music sound: sounds) {
            sound.dispose();
        }
    }
}
