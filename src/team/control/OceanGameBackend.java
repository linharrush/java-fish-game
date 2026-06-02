package team.control;

import shared.ui_ports.GameUiPort;
import team.model.Fish;
import team.model.GameMode;
import team.model.GameState;

public class OceanGameBackend {
    private final GameState gameState;
    private final FishMovementController fishMovementController;
    private final SpawnController spawnController;

    private GameUiPort gameUiPort() {
        return GameUiPort.getInstance();
    }

    private boolean runPeriodic = true;
    private boolean oceanViewState = false;

    public OceanGameBackend(
            GameState gameState,
            FishMovementController fishMovementController,
            SpawnController spawnController) {
        if (gameState == null) {
            throw new IllegalArgumentException("GameState cannot be null");
        }
        if (fishMovementController == null) {
            throw new IllegalArgumentException("FishMovementController cannot be null");
        }
        if (spawnController == null) {
            throw new IllegalArgumentException("SpawnController cannot be null");
        }

        this.gameState = gameState;
        this.fishMovementController = fishMovementController;
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
        fishMovementController.moveFish(fishId, x, y);
    }

    public void moveFishByIndex(int index, double dx, double dy) {
        if (!runPeriodic) {
            return;
        }

        fishMovementController.moveFishByIndex(index, dx, dy);
    }

    public void updateAutomaticFish() {
        if (!runPeriodic || gameState.getMode() != GameMode.PLAYING) {
            return;
        }

        fishMovementController.moveNonPlayerFish();
        spawnController.spawnFishIfNeeded();
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
