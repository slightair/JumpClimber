package cc.clv.jumpclimber.engine;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

public class GameMaster {
    private static final float BLOCK_SIZE = 0.64f;
    private static final float PREPARE_NEXT_PATTERN_THRESHOLD = BLOCK_SIZE * 16;
    private static final float CHARACTER_FALL_LIMIT = BLOCK_SIZE * 16;
    private static final float JUMP_VELOCITY_HORIZONTAL = 5f;
    private static final float JUMP_VELOCITY_VERTICAL = 8f;
    private static final float HOLD_FEEDBACK = 5f;

    public static final short OBJECT_CATEGORY_GROUND = 1 << 0;
    public static final short OBJECT_CATEGORY_LEFT_WALL = 1 << 1;
    public static final short OBJECT_CATEGORY_RIGHT_WALL = 1 << 2;
    public static final short OBJECT_CATEGORY_CHARACTER = 1 << 3;
    public static final short OBJECT_CATEGORY_OBSTACLE = 1 << 4;
    public static final short OBJECT_CATEGORY_ITEM = 1 << 5;

    private final float worldWidth;
    private final float worldHeight;

    private Character character;
    private TiledMap patternsTiledMap;
    private Array<TiledMapTileLayer> patterns;
    private float preparedPatternAltitude = 0;

    @lombok.Getter
    private World world;

    @lombok.Getter
    private float characterReachedAltitude = 0;

    public GameMaster(float worldWidth, float worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;

        patternsTiledMap = new TmxMapLoader().load("patterns.tmx");
        patterns = new Array<TiledMapTileLayer>();

        for (MapLayer layer : patternsTiledMap.getLayers()) {
            if (layer instanceof TiledMapTileLayer && !layer.getName().startsWith("_")) {
                patterns.add((TiledMapTileLayer) layer);
            }
        }
        setUpWorld();
    }

    private void setUpWorld() {
        world = new World(new Vector2(0, -9.8f), true);
        world.setContactListener(new ContactListener() {
            class ContactPair {
                @lombok.Getter
                private final Fixture self;

                @lombok.Getter
                private final Fixture counterpart;

                public ContactPair(Fixture self, Fixture counterpart) {
                    this.self = self;
                    this.counterpart = counterpart;
                }
            }

            private ContactPair characterContactPair(Contact contact) {
                Fixture contactCharacter = null;
                Fixture another = null;
                if (contact.getFixtureA().getFilterData().categoryBits == OBJECT_CATEGORY_CHARACTER) {
                    contactCharacter = contact.getFixtureA();
                    another = contact.getFixtureB();
                } else if (contact.getFixtureB().getFilterData().categoryBits == OBJECT_CATEGORY_CHARACTER) {
                    contactCharacter = contact.getFixtureB();
                    another = contact.getFixtureA();
                }

                if (contactCharacter == null || another == null) {
                    return null;
                }

                return new ContactPair(contactCharacter, another);
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
        addKinematicBox(new Vector2(worldWidth / 2, -BLOCK_SIZE / 2), worldWidth, BLOCK_SIZE, OBJECT_CATEGORY_GROUND, false);

        setUpNextPattern();
    }

    private void setUpNextPattern() {
        TiledMapTileLayer layer = patterns.random();
        createObstacles(layer);

        float wallHeight = layer.getHeight() * BLOCK_SIZE;
        float wallPositionY = preparedPatternAltitude + wallHeight / 2;
        addKinematicBox(new Vector2(BLOCK_SIZE / 2, wallPositionY), BLOCK_SIZE, wallHeight, OBJECT_CATEGORY_LEFT_WALL, true);
        addKinematicBox(new Vector2(worldWidth - BLOCK_SIZE / 2, wallPositionY), BLOCK_SIZE, wallHeight, OBJECT_CATEGORY_RIGHT_WALL, true);

        preparedPatternAltitude += wallHeight;
    }

    private Body addKinematicBox(Vector2 position, float width, float height, short objectCategory, boolean isSensor) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(position);

        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.0f;
        fixtureDef.filter.categoryBits = objectCategory;
        fixtureDef.isSensor = isSensor;

        body.createFixture(fixtureDef);
        shape.dispose();

        return body;
    }

    private void createObstacles(TiledMapTileLayer layer) {
        for (int y = 0; y < layer.getHeight(); y++) {
            for (int x = 0; x < layer.getWidth(); x++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                if (cell != null) {
                    TiledMapTile tile = cell.getTile();
                    Sprite sprite = new Sprite(tile.getTextureRegion());
                    Vector2 position = new Vector2(BLOCK_SIZE / 2 + x * BLOCK_SIZE, preparedPatternAltitude + BLOCK_SIZE / 2 + y * BLOCK_SIZE);
                    Body body = addKinematicBox(position, BLOCK_SIZE, BLOCK_SIZE, OBJECT_CATEGORY_OBSTACLE, false);
                    body.setUserData(sprite);
                }
            }
        }
    }

    private void removeOutsideObjects() {
        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);

        float removeLine = characterReachedAltitude - worldHeight;
        for (Body body : bodies) {
            if (body.getPosition().y < removeLine) {
                world.destroyBody(body);
            }
        }
    }

    public void step(float deltaTime) {
        world.step(deltaTime, 6, 2);

        characterReachedAltitude = Math.max(character.getAltitude(), characterReachedAltitude);
        if (preparedPatternAltitude - PREPARE_NEXT_PATTERN_THRESHOLD < characterReachedAltitude) {
            setUpNextPattern();
            removeOutsideObjects();
        }

        Body characterBody = character.getBody();

        float characterFallDeadLine = characterReachedAltitude - CHARACTER_FALL_LIMIT - worldHeight / 2;
        if (characterBody.getPosition().y < characterFallDeadLine) {
            character.setStatus(Character.Status.DEAD);
        }

        if (character.getStatus() == Character.Status.HOLD_LEFT_WALL || character.getStatus() == Character.Status.HOLD_RIGHT_WALL) {
            characterBody.applyForceToCenter(0, HOLD_FEEDBACK, true);
        }
    }

    public boolean needUpdateCameraPosition() {
        return character.getBody().getPosition().y >= characterReachedAltitude - CHARACTER_FALL_LIMIT;
    }

    public Vector2 getCharacterPosition() {
        return character.getBody().getPosition();
    }

    public float getCharacterAltitude() {
        return character.getAltitude();
    }

    public void characterRequestHoldLeftWall() {
    }

    public void characterRequestJumpToRightWall() {
        if (!(character.getStatus() == Character.Status.HOLD_LEFT_WALL || character.getStatus() == Character.Status.ON_GROUND)) {
            return;
        }

        character.setStatus(Character.Status.JUMPING_TO_RIGHT);
        character.getBody().setLinearVelocity(JUMP_VELOCITY_HORIZONTAL, JUMP_VELOCITY_VERTICAL);
    }

    public void characterRequestHoldRightWall() {

    }

    public void characterRequestJumpToLeftWall() {
        if (!(character.getStatus() == Character.Status.HOLD_RIGHT_WALL || character.getStatus() == Character.Status.ON_GROUND)) {
            return;
        }

        character.setStatus(Character.Status.JUMPING_TO_LEFT);
        character.getBody().setLinearVelocity(-JUMP_VELOCITY_HORIZONTAL, JUMP_VELOCITY_VERTICAL);
    }

    public boolean isOver() {
        return character.getStatus() == Character.Status.DEAD;
    }

    private void characterBeginContact(Fixture fixture) {
        if (character.getStatus() == Character.Status.DEAD) {
            return;
        }

        switch (fixture.getFilterData().categoryBits) {
            case OBJECT_CATEGORY_GROUND:
                character.setStatus(Character.Status.ON_GROUND);
                break;
            case OBJECT_CATEGORY_LEFT_WALL:
                character.setStatus(Character.Status.HOLD_LEFT_WALL);
                character.restrictVelocity();
                break;
            case OBJECT_CATEGORY_RIGHT_WALL:
                character.setStatus(Character.Status.HOLD_RIGHT_WALL);
                character.restrictVelocity();
                break;
            case OBJECT_CATEGORY_ITEM:
                break;
            case OBJECT_CATEGORY_OBSTACLE:
                character.setStatus(Character.Status.DEAD);
                break;
        }
    }

    private void characterEndContact(Fixture fixture) {

    }
}
