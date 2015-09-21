package cc.clv.jumpclimber;

import cc.clv.jumpclimber.graphics.GameSceneDirector;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class JumpClimber extends ApplicationAdapter {
    private GameSceneDirector sceneDirector;

    SpriteBatch batch;
    Texture img;

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("hikari.png");

        sceneDirector = new GameSceneDirector();
        Gdx.input.setInputProcessor(sceneDirector.getInput());
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.48f, 0.84f, 0.99f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        switch (sceneDirector.getCharacterStatus()) {
            case GROUND:
                batch.draw(img, 136, 260);
                break;
            case HOLD_LEFT_WALL:
            case RELEASE_LEFT_WALL:
                batch.draw(img, 0, 260);
                break;
            case HOLD_RIGHT_WALL:
            case RELEASE_RIGHT_WALL:
                batch.draw(img, 272, 260);
                break;
            case JUMPING_TO_RIGHT:
            case JUMPING_TO_LEFT:
                batch.draw(img, 136, 260);
        }

        batch.end();
    }


}
