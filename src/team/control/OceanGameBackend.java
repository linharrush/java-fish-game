package team.control;

import shared.ui_ports.GameUiPort;
import team.model.Fish;
import team.model.GameMode;
import team.model.GameState;

public class OceanGameBackend {
    private static final int SCREEN_WIDTH = 800;
    private static final int OFFSCREEN_MARGIN = 120;

    private final GameState gameState;
    private final SpawnController spawnController;

    private GameUiPort gameUiPort() {
        return GameUiPort.getInstance();
    }

    private boolean runPeriodic = true;
    private boolean oceanViewState = false;

    public OceanGameBackend(GameState gameState, SpawnController spawnController) {
        if (gameState == null) {
            throw new IllegalArgumentException("GameState cannot be null");
        }
        if (spawnController == null) {
            throw new IllegalArgumentException("SpawnController cannot be null");
        }

        this.gameState = gameState;
        this.spawnController = spawnController;
    }

    public void startScenario() {
        spawnController.resetSpawnTimer();

        for (int i = 0; i < gameState.getFishCount(); i++) {
            Fish f = gameState.getFish(i);
            gameUiPort().addFish(
                    f.getId(),
                    f.getCenter().getX(),
                    f.getCenter().getY(),
                    f.getSize(),
                    f.isPlayer(),
                    f.getFishType(),
                    f.getDirection());
        }

        gameUiPort().log("Scenario started.");
    }

    public void moveFish(int fishId, double x, double y) {
        int index = getFishIndexById(gameState, fishId);
        if (index < 0 || index >= gameState.getFishCount()) {
            return;
        }

        Fish f = gameState.getFish(index);
        if (f == null) {
            return;
        }

        f.getCenter().setX(x);
        f.getCenter().setY(y);
        gameUiPort().updateFish(fishId, x, y, f.getSize(), f.getFishType(), f.getDirection());
    }

    public void moveFishByIndex(int index, double dx, double dy) {
        if (!runPeriodic) {
            return;
        }

        Fish f = gameState.getFish(index);
        if (f != null) {
            moveFish(f.getId(), f.getCenter().getX() + dx, f.getCenter().getY() + dy);
        }
    }

    public void updateAutomaticFish() {
        if (!runPeriodic || gameState.getMode() != GameMode.PLAYING) {
            return;
        }

        moveNonPlayerFish();
        spawnController.spawnFishIfNeeded();
    }

    private void moveNonPlayerFish() {
        for (int i = 0; i < gameState.getFishCount(); i++) {
            Fish f = gameState.getFish(i);
            if (f == null || f.isPlayer()) {
                continue;
            }

            double dx = getSwimSpeed(f);
            if ("left".equals(f.getDirection())) {
                dx = -dx;
            }

            double nextX = f.getCenter().getX() + dx;
            if (isOffscreen(nextX)) {
                gameUiPort().removeFish(f.getId());
                gameState.removeFish(i);
            } else {
                moveFish(f.getId(), nextX, f.getCenter().getY());
            }
        }
    }

    private double getSwimSpeed(Fish fish) {
        return Math.max(4.0, 14.0 - fish.getSize() / 30.0);
    }

    private boolean isOffscreen(double x) {
        return x < -OFFSCREEN_MARGIN || x > SCREEN_WIDTH + OFFSCREEN_MARGIN;
    }

    private int getFishIndexById(GameState gameState, int fishId) {
        for (int i = 0; i < gameState.getFishCount(); i++) {
            Fish f = gameState.getFish(i);
            if (f != null && f.getId() == fishId) {
                return i;
            }
        }

        return -1;
    }

    public void toggleRunPeriodic() {
        this.runPeriodic = !this.runPeriodic;
    }

    public void oceanView() {
        this.oceanViewState = true;
        gameUiPort().updateBackgroundToOceanView();
    }

    public void normalView() {
        this.oceanViewState = false;
        gameUiPort().updateBackgroundToNormalView();
    }

    public boolean isOceanViewState() {
        return oceanViewState;
    }

}
