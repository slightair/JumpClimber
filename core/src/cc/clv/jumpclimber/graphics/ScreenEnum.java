package cc.clv.jumpclimber.graphics;

public enum ScreenEnum {
    TITLE {
        public AbstractScreen getScreen(Object... params) {
            return new TitleScreen();
        }
    },

    GAME {
        public AbstractScreen getScreen(Object... params) {
            return new GameScreen();
        }
    };

    public abstract AbstractScreen getScreen(Object... params);
}
