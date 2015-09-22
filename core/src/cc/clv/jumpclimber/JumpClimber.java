package cc.clv.jumpclimber;

import cc.clv.jumpclimber.graphics.GameSceneDirector;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

public class JumpClimber extends ApplicationAdapter {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Box2DDebugRenderer box2DDebugRenderer;
    private GameSceneDirector sceneDirector;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false,
                Gdx.graphics.getWidth() / GameSceneDirector.WORLD_SCALE,
                Gdx.graphics.getHeight() / GameSceneDirector.WORLD_SCALE);
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

        batch.begin();

        for (Body body : sceneDirector.getBodies()) {
            Sprite sprite = (Sprite) body.getUserData();
            if (sprite != null) {
                Vector2 position = body.getPosition().scl(GameSceneDirector.WORLD_SCALE);
                sprite.setRotation(MathUtils.radiansToDegrees * body.getAngle());
                sprite.setPosition(position.x - sprite.getWidth() / 2, position.y - sprite.getHeight() / 2);
                sprite.draw(batch);
            }
        }

        batch.end();

        box2DDebugRenderer.render(sceneDirector.getWorld(), camera.combined);
    }
}
