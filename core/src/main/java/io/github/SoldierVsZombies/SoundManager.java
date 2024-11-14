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

/* to create a voice in python :

in the terminal :     -------------->

pip install gtts


in the scratch file : -------------->

from gtts import gTTS

# Define the text and language
text = "collect wood first"
language = 'en'
tts = gTTS(text=text, lang=language, slow=False)

# Save the audio as an mp3 file
tts.save("collect_wood_first.mp3")

 */

/* or use this code to make it male voice :

pip install pyttsx3

import pyttsx3

# Initialize pyttsx3 engine
engine = pyttsx3.init()

# Find a male voice (this may vary by system)
for voice in engine.getProperty('voices'):
    if 'male' in voice.name.lower():
        engine.setProperty('voice', voice.id)
        break

# Set the speed and volume
engine.setProperty('rate', 200)  # Adjust speed
engine.setProperty('volume', 1)  # Max volume

# Save to the root of the C: drive
engine.save_to_file("collect wood first", "C:\\collect_wood_first_male.mp3")
engine.runAndWait()

 */
