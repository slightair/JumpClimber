package cc.clv.jumpclimber.graphics;

import cc.clv.jumpclimber.engine.GameMaster;
import cc.clv.jumpclimber.input.GameSceneInput;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class GameSceneDirector {
    public static final float WORLD_SCALE = 50f;

    @lombok.Getter
    private final GameSceneInput input = new GameSceneInput(this);

    private final GameMaster gameMaster = new GameMaster(Gdx.graphics.getWidth() / WORLD_SCALE,
            Gdx.graphics.getHeight() / WORLD_SCALE);

    public void step(float deltaTime) {
        gameMaster.getWorld().step(deltaTime, 6, 2);
    }

    public World getWorld() {
        return gameMaster.getWorld();
    }

    public Array<Body> getBodies() {
        Array<Body> bodies = new Array<Body>();
        getWorld().getBodies(bodies);
        return bodies;
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
