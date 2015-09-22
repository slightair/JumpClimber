package cc.clv.jumpclimber.engine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;

public class Character {
    public enum Status {
        GROUND,
        HOLD_LEFT_WALL,
        RELEASE_LEFT_WALL,
        HOLD_RIGHT_WALL,
        RELEASE_RIGHT_WALL,
        JUMPING_TO_RIGHT,
        JUMPING_TO_LEFT,
    }

    @lombok.Getter
    @lombok.Setter
    public Status status = Status.GROUND;

    @lombok.Getter
    public Body body;

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

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(sprite.getWidth() / 2, sprite.getHeight() / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;

        body.createFixture(fixtureDef);
        shape.dispose();
    }
}
