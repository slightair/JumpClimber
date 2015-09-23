package cc.clv.jumpclimber.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

public class TitleScreen extends AbstractScreen {

    @Override
    public void buildStage() {
        Skin skin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        Label label = new Label("Jump Climber", skin);
        label.setPosition(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, Align.center);
        addActor(label);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        ScreenSwitcher.getInstance().showScreen(ScreenEnum.GAME);
        return false;
    }
}
