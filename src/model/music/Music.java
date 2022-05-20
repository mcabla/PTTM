package model.music;

import javafx.scene.image.Image;
import model.image.ImageCache;
import tools.Files;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;


/**
 * Object that can be used to play and control the background song.
 */
//codejava.net/coding/how-to-play-back-audio-in-java-with-examples
public class Music {
    private Clip audioClip;

    /**
     * The song is loaded upon constructing this object.
     */
    public Music(){
        File audioFile = new File(Files.AUDIO);

        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat format = audioInputStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            audioClip = (Clip) AudioSystem.getLine(info);
            audioClip.open(audioInputStream);

        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Play or continue the song in a loop.
     */
    public void start(){
        audioClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * Pause the song
     */
    public void stop() {
        audioClip.stop();
    }

    /**
     * Swtich between play and pauze.
     */
    public void mute(){
        if(isRunning()) stop();
        else start();
    }

    private boolean isRunning(){
        return audioClip.isRunning();
    }

    /**
     * Returns an image that can be used for a mute button.
     * @return
     */
    public Image getImage(){
        if(isRunning()) return getSoundImage();
        return getMuteImage();
    }

    private Image getSoundImage(){
        return ImageCache.getImage("soundOn.png");
    }
    private Image getMuteImage() {
        return ImageCache.getImage("soundOff.png");
    }
}
