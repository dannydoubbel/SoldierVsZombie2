package io.github.SoldierVsZombies;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class MyInputProcessor implements InputProcessor {
    final PressedKeys pressedKeys = PressedKeys.getInstance();
    final SharedVariables sharedVariables = SharedVariables.getInstance();
    ViewParameters viewParameters = ViewParameters.getInstance();

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.ENTER: {
                pressedKeys.enter = true;
                break;
            }
            case Input.Keys.M: {
                sharedVariables.musicAllowed = true;
                break;
            }
            case Input.Keys.LEFT:
                resetAllGoDirections();
                pressedKeys.goLeft = true;
                break;
            case Input.Keys.RIGHT:
                resetAllGoDirections();
                pressedKeys.goRight = true;
                break;
            case Input.Keys.UP:
                resetAllGoDirections();
                pressedKeys.goUp = true;
                break;
            case Input.Keys.DOWN:
                resetAllGoDirections();
                pressedKeys.goDown = true;
                break;
            case Input.Keys.ALT_LEFT:
            case Input.Keys.ALT_RIGHT:
                pressedKeys.fireALT = true;
                break;
            case Input.Keys.ESCAPE:
                handleAction("escapeAction");
                break;
            case Input.Keys.PAUSE:
                handleAction("pauseAction");
                break;
            case Input.Keys.PAGE_DOWN:
                pressedKeys.zoomIn = true;
                break;
            case Input.Keys.PAGE_UP:
                pressedKeys.zoomOut = true;
                break;
            case Input.Keys.INSERT:
                break;
            case Input.Keys.SPACE:
                pressedKeys.fireSpace = true;
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.ENTER: {
                pressedKeys.enter = false;
            }
            case Input.Keys.LEFT:
                pressedKeys.goLeft = false;
                break;
            case Input.Keys.RIGHT:
                pressedKeys.goRight = false;
                break;
            case Input.Keys.UP:
                pressedKeys.goUp = false;
                break;
            case Input.Keys.DOWN:
                pressedKeys.goDown = false;
                break;
            case Input.Keys.SPACE:
                pressedKeys.fireSpace = false;
                break;
            case Input.Keys.ALT_LEFT:
            case Input.Keys.ALT_RIGHT:
                pressedKeys.fireALT = false;
                break;
        }
        return false;
    }

    private void resetAllGoDirections() {
        pressedKeys.goRight = false;
        pressedKeys.goLeft = false;
        pressedKeys.goUp = false;
        pressedKeys.goDown = false;
        pressedKeys.fireSpace = false;
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
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
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
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    private void handleAction(String actionName) {
        // Implement your actual actions here
        System.out.println("Action: " + actionName);
    }
    // Other input methods (keyUp, touchDown, etc.) can be implemented as needed


}

