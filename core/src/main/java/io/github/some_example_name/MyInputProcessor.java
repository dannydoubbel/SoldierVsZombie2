package io.github.some_example_name;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class MyInputProcessor implements InputProcessor {
    SharedVariables sharedVariables = SharedVariables.getInstance();
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
                sharedVariables.goLeft = true;
                break;
            case Input.Keys.RIGHT:
                sharedVariables.goRight = true;
                break;
            case Input.Keys.UP:
                sharedVariables.goUp = true;
                break;
            case Input.Keys.DOWN:
                sharedVariables.goDown = true;
                break;
            case Input.Keys.ENTER:
                handleAction("enterAction");
                break;
            case Input.Keys.SPACE:
                handleAction("spaceAction");
                break;
            case Input.Keys.ESCAPE:
                handleAction("escapeAction");
                break;
            case Input.Keys.PAUSE:
                handleAction("pauseAction");
                break;
            case Input.Keys.PAGE_DOWN:
                sharedVariables.setZoomIn(true);
                break;
            case Input.Keys.PAGE_UP:
                sharedVariables.setZoomOut(true);
                break;
            case Input.Keys.INSERT:
                sharedVariables.setDebugScreen(!sharedVariables.isDebugScreen());
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
                sharedVariables.goLeft = false;
                break;
            case Input.Keys.RIGHT:
                sharedVariables.goRight = false;
                break;
            case Input.Keys.UP:
                sharedVariables.goUp = false;
                break;
            case Input.Keys.DOWN:
                sharedVariables.goDown = false;
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

