package cc.clv.jumpclimber.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public abstract class AbstractScreen extends Stage implements Screen {
    public static final int SCREEN_WIDTH = 320;
    public static final int SCREEN_HEIGHT = 568;

    protected AbstractScreen() {
        super(new StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT, new OrthographicCamera()));
    }

    public abstract void buildStage();

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.48f, 0.84f, 0.99f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.act(delta);
        super.draw();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void resize(int width, int height) {
        getViewport().update(width, height, true);
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }
}
