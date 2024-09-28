package io.github.some_example_name;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;

public class MyInputProcessor implements InputProcessor {


    @Override
    public boolean keyDown(int keycode) {
        SharedVariables sharedVariables = SharedVariables.getInstance();
        int indexSoldier = sharedVariables.getTextureIndexSoldier();
        int previousIndexSoldier = indexSoldier;
        int leftOffset = sharedVariables.getTileMapLeftOffset();

        float zoomValue = sharedVariables.getzoomValue();
        float previousZoomValue = zoomValue;
        switch (keycode) {
            case Input.Keys.LEFT:
                sharedVariables.setCurrentSolderDirection(Directions.lt);
                leftOffset+=sharedVariables.LEFT_OFFSET_STEP_SIZE;
                indexSoldier++;
                break;
            case Input.Keys.RIGHT:
                sharedVariables.setCurrentSolderDirection(Directions.rt);
                indexSoldier++;
                leftOffset-=sharedVariables.LEFT_OFFSET_STEP_SIZE;
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
            case Input.Keys.PAGE_DOWN:
                zoomValue *= 2;
                break;
            case Input.Keys.PAGE_UP:
                zoomValue = zoomValue > 0.5f ? zoomValue / 2 : sharedVariables.ZOOM_MIN_VALUE;
                break;
            case Input.Keys.TAB:
                break;
            default:
                break;
        }

        if (previousIndexSoldier != indexSoldier) {
            indexSoldier = indexSoldier % 7; // Ensures indexSoldier wraps around to 0 if it exceeds 6
            sharedVariables.setTextureIndexSoldier(indexSoldier);
        }

        if (previousZoomValue != zoomValue) {
            zoomValue = MathUtils.clamp(zoomValue, sharedVariables.ZOOM_MIN_VALUE, sharedVariables.ZOOM_MAX_VALUE);
            sharedVariables.setzoomValue(zoomValue);
        }
        // todo check previous value
        sharedVariables.setTileMapLeftOffset(leftOffset);

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

