package cc.clv.jumpclimber.desktop;

import cc.clv.jumpclimber.JumpClimber;
import cc.clv.jumpclimber.graphics.AbstractScreen;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = AbstractScreen.SCREEN_WIDTH;
        config.height = AbstractScreen.SCREEN_HEIGHT;
        new LwjglApplication(new JumpClimber(), config);
    }
}
