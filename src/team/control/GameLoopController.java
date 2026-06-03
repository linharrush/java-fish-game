package team.control;

import team.model.GameMode;
import team.model.GameState;

public class GameLoopController {
    private final GameState gameState;
    private final FishMovementController fishMovementController;
    private final SpawnController spawnController;

    private boolean runPeriodic = true;

    public GameLoopController(
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
}
