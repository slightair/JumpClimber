package cc.clv.jumpclimber.engine;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class GameMaster {
    private static final float WALL_WIDTH = 0.64f;
    private static final float GROUND_HEIGHT = 0.64f;
    private static final float JUMP_VELOCITY_HORIZONTAL = 5f;
    private static final float JUMP_VELOCITY_VERTICAL = 8f;

    private final float worldWidth;
    private final float worldHeight;

    private Character character;
    private Body leftWallBody;
    private Body rightWallBody;

    @lombok.Getter
    private World world;

    public GameMaster(float worldWidth, float worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;

        setUpWorld();
    }

    private void setUpWorld() {
        world = new World(new Vector2(0, -9.8f), true);

        character = new Character(world, worldWidth / 2, worldHeight / 2);

        addStaticBox(new Vector2(worldWidth / 2, GROUND_HEIGHT / 2), worldWidth, GROUND_HEIGHT);
        leftWallBody = addStaticBox(new Vector2(WALL_WIDTH / 2, worldHeight / 2), WALL_WIDTH, worldHeight * 2);
        rightWallBody = addStaticBox(new Vector2(worldWidth - WALL_WIDTH / 2, worldHeight / 2), WALL_WIDTH, worldHeight * 2);
    }

    private Body addStaticBox(Vector2 position, float width, float height) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(position);

        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);

        body.createFixture(shape, 0.0f);
        shape.dispose();

        return body;
    }

    public void step(float deltaTime) {
        world.step(deltaTime, 6, 2);

        Vector2 velocity = character.getBody().getLinearVelocity().scl(0, 1);
        leftWallBody.setLinearVelocity(velocity);
        rightWallBody.setLinearVelocity(velocity);
    }

    public Vector2 getCharacterPosition() {
        return character.getBody().getPosition();
    }

    public void characterHoldLeftWall() {
        character.setStatus(Character.Status.HOLD_LEFT_WALL);
    }

    public void characterReleaseLeftWall() {
        character.setStatus(Character.Status.RELEASE_LEFT_WALL);
    }

    public void characterJumpToRightWall() {
        character.setStatus(Character.Status.JUMPING_TO_RIGHT);
        character.getBody().setLinearVelocity(JUMP_VELOCITY_HORIZONTAL, JUMP_VELOCITY_VERTICAL);
    }

    public void characterHoldRightWall() {
        character.setStatus(Character.Status.HOLD_RIGHT_WALL);
    }

    public void characterReleaseRightWall() {
        character.setStatus(Character.Status.RELEASE_RIGHT_WALL);
    }

    public void characterJumpToLeftWall() {
        character.setStatus(Character.Status.JUMPING_TO_LEFT);
        character.getBody().setLinearVelocity(-JUMP_VELOCITY_HORIZONTAL, JUMP_VELOCITY_VERTICAL);
    }
}
