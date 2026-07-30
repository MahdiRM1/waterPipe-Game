package project;

import javafx.scene.media.AudioClip;

public class SoundManager {

    private SoundManager(){}
    private static final AudioClip click = new AudioClip(SoundManager.class.getResource("/Audio/click.mp3").toExternalForm());
    private static final AudioClip hover = new AudioClip(SoundManager.class.getResource("/Audio/hover.mp3").toExternalForm());
    private static final AudioClip rotate = new AudioClip(SoundManager.class.getResource("/Audio/pipe_rotate.mp3").toExternalForm());
    private static final AudioClip lose = new AudioClip(SoundManager.class.getResource("/Audio/lose.mp3").toExternalForm());
    private static final AudioClip win = new AudioClip(SoundManager.class.getResource("/Audio/win.mp3").toExternalForm());
    private static final AudioClip error = new AudioClip(SoundManager.class.getResource("/Audio/error.mp3").toExternalForm());

    public static void playClick(){
        click.play();
    }

    public static void playHover(){
        hover.play();
    }

    public static void playRotate(){
        rotate.play();
    }

    public static void playLose(){
        lose.play();
    }

    public static void playWin(){
        win.play();
    }

    public static void playError(){
        error.play();
    }
}
