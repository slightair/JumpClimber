package cc.clv.jumpclimber.graphics;

import cc.clv.jumpclimber.engine.*;
import cc.clv.jumpclimber.engine.Character;
import cc.clv.jumpclimber.input.GameSceneInput;

public class GameSceneDirector {
    @lombok.Getter
    private final GameSceneInput input = new GameSceneInput(this);

    private final GameMaster gameMaster = new GameMaster();

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

    public Character.Status getCharacterStatus() {
        return gameMaster.character.status;
    }
}
