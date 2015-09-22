package cc.clv.jumpclimber.engine;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class GameMaster {
    private static final float WALL_WIDTH = 0.64f;
    private static final float GROUND_HEIGHT = 0.64f;
    private static final float JUMP_VELOCITY_HORIZONTAL = 5f;
    private static final float JUMP_VELOCITY_VERTICAL = 8f;
    private static final float HOLD_FEEDBACK = 5f;

    private final float worldWidth;
    private final float worldHeight;

    private Character character;
    private Body groundBody;
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
        world.setContactListener(new ContactListener() {
            class ContactPair {
                @lombok.Getter
                private final Body self;

                @lombok.Getter
                private final Body counterpart;

                public ContactPair(Body self, Body counterpart) {
                    this.self = self;
                    this.counterpart = counterpart;
                }
            }

            private ContactPair characterContactPair(Contact contact) {
                Body contactBodyA = contact.getFixtureA().getBody();
                Body contactBodyB = contact.getFixtureB().getBody();

                Body contactCharacterBody = null;
                Body anotherBody = null;
                if (contactBodyA == character.getBody()) {
                    contactCharacterBody = contactBodyA;
                    anotherBody = contactBodyB;
                } else if (contactBodyA == character.getBody()) {
                    contactCharacterBody = contactBodyB;
                    anotherBody = contactBodyA;
                }

                if (contactCharacterBody == null || anotherBody == null) {
                    return null;
                }

                return new ContactPair(contactCharacterBody, anotherBody);
            }

            @Override
            public void beginContact(Contact contact) {
                ContactPair characterContactPair = characterContactPair(contact);
                if (characterContactPair != null) {
                    characterBeginContact(characterContactPair.counterpart);
                }
            }

            @Override
            public void endContact(Contact contact) {
                ContactPair characterContactPair = characterContactPair(contact);
                if (characterContactPair != null) {
                    characterEndContact(characterContactPair.counterpart);
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });

        character = new Character(world, worldWidth / 2, worldHeight / 2);

        groundBody = addStaticBox(new Vector2(worldWidth / 2, GROUND_HEIGHT / 2), worldWidth, GROUND_HEIGHT);
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

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.0f;

        body.createFixture(fixtureDef);
        shape.dispose();

        return body;
    }

    public void step(float deltaTime) {
        world.step(deltaTime, 6, 2);

        if (character.getStatus() == Character.Status.HOLD_LEFT_WALL || character.getStatus() == Character.Status.HOLD_RIGHT_WALL) {
            character.getBody().applyForceToCenter(0, HOLD_FEEDBACK, true);
        }

        Vector2 velocity = character.getBody().getLinearVelocity().scl(0, 1);
        leftWallBody.setLinearVelocity(velocity);
        rightWallBody.setLinearVelocity(velocity);
    }

    public Vector2 getCharacterPosition() {
        return character.getBody().getPosition();
    }

    public void characterRequestHoldLeftWall() {
    }

    public void characterRequestJumpToRightWall() {
        if (!(character.getStatus() == Character.Status.HOLD_LEFT_WALL || character.getStatus() == Character.Status.GROUND)) {
            return;
        }

        character.setStatus(Character.Status.JUMPING_TO_RIGHT);
        character.getBody().setLinearVelocity(JUMP_VELOCITY_HORIZONTAL, JUMP_VELOCITY_VERTICAL);
    }

    public void characterRequestHoldRightWall() {

    }

    public void characterRequestJumpToLeftWall() {
        if (!(character.getStatus() == Character.Status.HOLD_RIGHT_WALL || character.getStatus() == Character.Status.GROUND)) {
            return;
        }

        character.setStatus(Character.Status.JUMPING_TO_LEFT);
        character.getBody().setLinearVelocity(-JUMP_VELOCITY_HORIZONTAL, JUMP_VELOCITY_VERTICAL);
    }

    private void characterBeginContact(Body body) {
        if (body == groundBody) {
            character.setStatus(Character.Status.GROUND);
        } else if (body == leftWallBody) {
            character.setStatus(Character.Status.HOLD_LEFT_WALL);
            character.removeVerticalVelocity();
        } else if (body == rightWallBody) {
            character.setStatus(Character.Status.HOLD_RIGHT_WALL);
            character.removeVerticalVelocity();
        }
    }

    private void characterEndContact(Body body) {

    }
}
