package cc.clv.jumpclimber;

import cc.clv.jumpclimber.graphics.ScreenEnum;
import cc.clv.jumpclimber.graphics.ScreenSwitcher;
import com.badlogic.gdx.Game;

public class JumpClimber extends Game {
    @Override
    public void create() {
        ScreenSwitcher screenSwitcher = ScreenSwitcher.getInstance();
        screenSwitcher.initialize(this);
        screenSwitcher.showScreen(ScreenEnum.TITLE);
    }
}
