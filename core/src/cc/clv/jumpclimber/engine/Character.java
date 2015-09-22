package cc.clv.jumpclimber.engine;

import cc.clv.jumpclimber.graphics.GameSceneDirector;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Character {
    public enum Status {
        ON_GROUND,
        HOLD_LEFT_WALL,
        HOLD_RIGHT_WALL,
        RELEASE_LEFT_WALL,
        RELEASE_RIGHT_WALL,
        JUMPING_TO_RIGHT,
        JUMPING_TO_LEFT,
        DEAD,
    }

    @lombok.Getter
    @lombok.Setter
    private Status status = Status.ON_GROUND;

    @lombok.Getter
    private Body body;

    private Sprite sprite;

    public Character(World world, float positionX, float positionY) {
        Texture texture = new Texture("hikari.png");
        sprite = new Sprite(texture);

        createBody(world, positionX, positionY);
    }

    private void createBody(World world, float positionX, float positionY) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(positionX, positionY);

        body = world.createBody(bodyDef);
        body.setUserData(sprite);
        body.setFixedRotation(true);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth() / 2 / GameSceneDirector.WORLD_SCALE, sprite.getHeight() / 2 / GameSceneDirector.WORLD_SCALE);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.filter.categoryBits = GameMaster.OBJECT_CATEGORY_CHARACTER;

        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public void removeVerticalVelocity() {
        Vector2 velocity = body.getLinearVelocity().scl(1, 0);
        body.setLinearVelocity(velocity);
    }
}
