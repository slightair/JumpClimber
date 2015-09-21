package cc.clv.jumpclimber.engine;

public class GameMaster {
    public Character character = new Character();

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
