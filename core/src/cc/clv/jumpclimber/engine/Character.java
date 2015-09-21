package cc.clv.jumpclimber.engine;

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
}
