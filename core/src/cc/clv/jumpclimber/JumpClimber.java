package cc.clv.jumpclimber;

import cc.clv.jumpclimber.graphics.GameSceneDirector;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

public class JumpClimber extends ApplicationAdapter {
    private SpriteBatch batch;
    private Box2DDebugRenderer box2DDebugRenderer;
    private GameSceneDirector sceneDirector;

    @Override
    public void create() {
        batch = new SpriteBatch();

        Box2D.init();
        box2DDebugRenderer = new Box2DDebugRenderer();

        sceneDirector = new GameSceneDirector();
        Gdx.input.setInputProcessor(sceneDirector.getInput());
    }

    @Override
    public void render() {
        sceneDirector.step(Gdx.graphics.getDeltaTime());

        Gdx.gl.glClearColor(0.48f, 0.84f, 0.99f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(sceneDirector.getProjectionMatrix());
        batch.begin();

        for (Sprite sprite : sceneDirector.getSprites()) {
            sprite.draw(batch);
        }

        batch.end();

        box2DDebugRenderer.render(sceneDirector.getWorld(), sceneDirector.getBox2DProjectionMatrix());
    }
}
