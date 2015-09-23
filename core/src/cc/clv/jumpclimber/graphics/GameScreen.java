package cc.clv.jumpclimber.graphics;

import cc.clv.jumpclimber.engine.GameMaster;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

public class GameScreen extends AbstractScreen {
    public static final float WORLD_SCALE = 50f;
    private static final float CAMERA_OFFSET_Y = 80f;

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Box2DDebugRenderer box2DDebugRenderer;
    private GameMaster gameMaster;
    private Label heightLabel;

    @Override
    public void buildStage() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);

        batch = new SpriteBatch();

        Box2D.init();
        box2DDebugRenderer = new Box2DDebugRenderer();

        gameMaster = new GameMaster(SCREEN_WIDTH / WORLD_SCALE, SCREEN_HEIGHT / WORLD_SCALE);

        Skin skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
        heightLabel = new Label("0.0m", skin);
        heightLabel.setPosition(8, SCREEN_HEIGHT - 8, Align.topLeft);
        addActor(heightLabel);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new GameScreenInputProcessor(gameMaster));
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if (gameMaster.isOver()) {
            ScreenSwitcher.getInstance().showScreen(ScreenEnum.TITLE);
            return;
        }

        heightLabel.setText(String.format("%.1fm", gameMaster.getCharacterAltitude()));

        gameMaster.step(delta);
        updateCamera();

        renderWorldObjects();

        box2DDebugRenderer.render(gameMaster.getWorld(), camera.combined.scl(WORLD_SCALE));
    }

    private void updateCamera() {
        if (gameMaster.needUpdateCameraPosition()) {
            camera.position.y = gameMaster.getCharacterPosition().scl(WORLD_SCALE).y + CAMERA_OFFSET_Y;
        }

        camera.update();
    }

    private void renderWorldObjects() {
        Array<Body> bodies = new Array<Body>();
        gameMaster.getWorld().getBodies(bodies);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        for (Body body : bodies) {
            if (body.getUserData() instanceof Sprite) {
                Sprite sprite = (Sprite) body.getUserData();

                Vector2 position = body.getPosition().scl(WORLD_SCALE);
                sprite.setRotation(MathUtils.radiansToDegrees * body.getAngle());
                sprite.setPosition(position.x - sprite.getWidth() / 2, position.y - sprite.getHeight() / 2);

                sprite.draw(batch);
            }
        }

        batch.end();
    }
}
