package cc.clv.jumpclimber.graphics;

import cc.clv.jumpclimber.engine.GameMaster;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

public class GameScreenInputProcessor extends InputMultiplexer {
    private GameMaster gameMaster;

    public GameScreenInputProcessor(GameMaster gameMaster) {
        this.gameMaster = gameMaster;

        addProcessor(new GestureDetector(new GestureListener()));
        addProcessor(new KeyboardInputProcessor());
    }

    private class GestureListener implements GestureDetector.GestureListener {

        @Override
        public boolean touchDown(float x, float y, int pointer, int button) {
            return false;
        }

        @Override
        public boolean tap(float x, float y, int count, int button) {
            return false;
        }

        @Override
        public boolean longPress(float x, float y) {
            return false;
        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            return false;
        }

        @Override
        public boolean pan(float x, float y, float deltaX, float deltaY) {
            return false;
        }

        @Override
        public boolean panStop(float x, float y, int pointer, int button) {
            return false;
        }

        @Override
        public boolean zoom(float initialDistance, float distance) {
            return false;
        }

        @Override
        public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
            return false;
        }
    }

    private class KeyboardInputProcessor implements InputProcessor {

        @Override
        public boolean keyDown(int keycode) {
            switch (keycode) {
                case Input.Keys.A:
                case Input.Keys.LEFT:
                    gameMaster.characterRequestHoldLeftWall();
                    break;
                case Input.Keys.D:
                case Input.Keys.RIGHT:
                    gameMaster.characterRequestHoldRightWall();
                    break;
            }
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            switch (keycode) {
                case Input.Keys.A:
                case Input.Keys.LEFT:
                    gameMaster.characterRequestJumpToRightWall();
                    break;
                case Input.Keys.D:
                case Input.Keys.RIGHT:
                    gameMaster.characterRequestJumpToLeftWall();
                    break;
            }
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            return false;
        }
    }
}
