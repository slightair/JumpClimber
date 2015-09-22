package cc.clv.jumpclimber.graphics;

import cc.clv.jumpclimber.engine.GameMaster;
import cc.clv.jumpclimber.input.GameSceneInput;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class GameSceneDirector {
    public static final float WORLD_SCALE = 50f;
    private static final float CAMERA_OFFSET_Y = 80f;

    private final OrthographicCamera camera;

    @lombok.Getter
    private final GameSceneInput input;

    private final GameMaster gameMaster;

    public GameSceneDirector() {
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);

        gameMaster = new GameMaster(width / WORLD_SCALE, height / WORLD_SCALE);
        input = new GameSceneInput(gameMaster);
    }

    private void moveCamera() {
        camera.position.y = gameMaster.getCharacterPosition().scl(WORLD_SCALE).y + CAMERA_OFFSET_Y;
        camera.update();
    }

    public void step(float deltaTime) {
        gameMaster.step(deltaTime);

        moveCamera();
    }

    public World getWorld() {
        return gameMaster.getWorld();
    }

    public Matrix4 getProjectionMatrix() {
        return camera.combined;
    }

    public Matrix4 getBox2DProjectionMatrix() {
        return camera.combined.scl(WORLD_SCALE);
    }

    public Array<Sprite> getSprites() {
        Array<Body> bodies = new Array<Body>();
        getWorld().getBodies(bodies);

        Array<Sprite> sprites = new Array<Sprite>();

        for (Body body : bodies) {
            Sprite sprite = (Sprite) body.getUserData();
            if (sprite != null) {
                Vector2 position = body.getPosition().scl(WORLD_SCALE);
                sprite.setRotation(MathUtils.radiansToDegrees * body.getAngle());
                sprite.setPosition(position.x - sprite.getWidth() / 2, position.y - sprite.getHeight() / 2);
                sprites.add(sprite);
            }
        }

        return sprites;
    }
}
