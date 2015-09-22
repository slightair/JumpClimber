package cc.clv.jumpclimber.engine;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class GameMaster {
    private Character character;

    @lombok.Getter
    private World world;

    public GameMaster(float worldWidth, float worldHeight) {
        setUpWorld(worldWidth, worldHeight);
    }

    private void setUpWorld(float worldWidth, float worldHeight) {
        world = new World(new Vector2(0, -98f), true);

        character = new Character(world, worldWidth / 2, worldHeight / 2);
    }

    public void characterHoldLeftWall() {
        character.status = Character.Status.HOLD_LEFT_WALL;
    }

    public void characterReleaseLeftWall() {
        character.status = Character.Status.RELEASE_LEFT_WALL;
    }

    public void characterJumpToRightWall() {
        character.status = Character.Status.JUMPING_TO_RIGHT;
    }

    public void characterHoldRightWall() {
        character.status = Character.Status.HOLD_RIGHT_WALL;
    }

    public void characterReleaseRightWall() {
        character.status = Character.Status.RELEASE_RIGHT_WALL;
    }

    public void characterJumpToLeftWall() {
        character.status = Character.Status.JUMPING_TO_LEFT;
    }
}
