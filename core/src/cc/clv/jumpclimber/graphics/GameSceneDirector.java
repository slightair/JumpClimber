package cc.clv.jumpclimber.graphics;

import cc.clv.jumpclimber.engine.GameMaster;
import cc.clv.jumpclimber.input.GameSceneInput;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
    private final GameSceneInput input = new GameSceneInput(this);

    private final GameMaster gameMaster;

    private Array<Sprite> backgroundSprites;

    public GameSceneDirector() {
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);

        gameMaster = new GameMaster(width / WORLD_SCALE, height / WORLD_SCALE);
        createBackground(width, height * 2);
    }

    private void moveCamera() {
        camera.position.y = gameMaster.getCharacterPosition().scl(WORLD_SCALE).y + CAMERA_OFFSET_Y;
        camera.update();
    }

    private void createBackground(float width, float height) {
        backgroundSprites = new Array<Sprite>();

        Texture texture = new Texture("block.png");

        for (int y = 0; y < height / texture.getWidth(); y++) {
            for (int x = 0; x < width / texture.getWidth(); x++) {
                Sprite sprite = new Sprite(texture);

                float gray = (float) (Math.random() * 0.05 + 0.5);
                if (y == 0 || x == 0 || x == width / texture.getWidth() - 1) {
                    gray = 0.8f;
                }
                sprite.setColor(gray, gray, gray, 1.0f);
                sprite.setPosition(x * texture.getWidth(), y * texture.getHeight());

                backgroundSprites.add(sprite);
            }
        }
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

        Array<Sprite> sprites = new Array<Sprite>(backgroundSprites);

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

    public void requestHoldLeftWall() {
        gameMaster.characterHoldLeftWall();
    }

    public void requestReleaseLeftWall() {
        gameMaster.characterReleaseLeftWall();
    }

    public void requestJumpToRightWall() {
        gameMaster.characterJumpToRightWall();
    }

    public void requestHoldRightWall() {
        gameMaster.characterHoldRightWall();
    }

    public void requestReleaseRightWall() {
        gameMaster.characterReleaseRightWall();
    }

    public void requestJumpToLeftWall() {
        gameMaster.characterJumpToLeftWall();
    }
}
