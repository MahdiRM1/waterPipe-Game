package project;

import javafx.stage.Screen;

public final class Constants {

    private Constants() {}

    public static final double SCREEN_WIDTH = Screen.getPrimary().getVisualBounds().getWidth();
    public static final double SCREEN_HEIGHT = SCREEN_WIDTH * 9 / 16;
}
