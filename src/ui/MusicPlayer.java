package ui;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MusicPlayer {
    private Clip clip;

    public void reproduce(String pathFile, boolean isLoop) {
        try {
            File fileMusic = new File(pathFile);

            if (fileMusic.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(fileMusic);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
                if (isLoop) clip.loop(Clip.LOOP_CONTINUOUSLY);
                clip.start();
            } else {
                System.out.println("No se encuentra el archivo en la ruta: " + pathFile);
            }

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public Clip getClip() {
        return clip;
    }
}
