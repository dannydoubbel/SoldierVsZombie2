package io.github.some_example_name;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class MyInputProcessor implements InputProcessor {
    @Override
    public boolean  keyDown(int keycode) {
        SharedVariables sharedVariables = SharedVariables.getInstance();
        int indexSoldier = sharedVariables.getTextureIndexSoldier();
        switch (keycode) {
            case Input.Keys.LEFT:
                sharedVariables.setCurrentSolderDirection(Directions.lt);
                indexSoldier++;
                break;
            case Input.Keys.RIGHT:
                sharedVariables.setCurrentSolderDirection(Directions.rt);
                indexSoldier++;
                break;
            case Input.Keys.UP:
                sharedVariables.setCurrentSolderDirection(Directions.up);
                indexSoldier++;
                break;
            case Input.Keys.DOWN:
                sharedVariables.setCurrentSolderDirection(Directions.dn);
                indexSoldier++;
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
            default:
                break;
        }
        if (indexSoldier>6) {
            indexSoldier = 0;
        }
        sharedVariables.setTextureIndexSoldier(indexSoldier);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
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
