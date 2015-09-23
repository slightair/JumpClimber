package cc.clv.jumpclimber.graphics;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class ScreenSwitcher {
    private static ScreenSwitcher instance;

    private Game game;

    private ScreenSwitcher() {
        super();
    }

    public static ScreenSwitcher getInstance() {
        if (instance == null) {
            instance = new ScreenSwitcher();
        }
        return instance;
    }

    public void initialize(Game game) {
        this.game = game;
    }

    public void showScreen(ScreenEnum screenEnum, Object... params) {
        Screen currentScreen = game.getScreen();

        AbstractScreen newScreen = screenEnum.getScreen(params);
        newScreen.buildStage();

        game.setScreen(newScreen);

        if (currentScreen != null) {
            currentScreen.dispose();
        }
    }
}
