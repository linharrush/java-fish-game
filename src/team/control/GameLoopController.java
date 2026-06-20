package team.control;

import team.model.GameMode;
import team.model.GameState;
import team.model.PlayerFish;

public class GameLoopController {
    private final GameState gameState;
    private final MovementController movementController;
    private final SpawnController spawnController;
    private final CollisionController collisionController;

    private boolean runPeriodic = true;

    public GameLoopController(
            GameState gameState,
            MovementController movementController,
            SpawnController spawnController,
            CollisionController collisionController) {
        if (gameState == null) {
            throw new IllegalArgumentException("GameState cannot be null");
        }
        if (movementController == null) {
            throw new IllegalArgumentException("MovementController cannot be null");
        }
        if (spawnController == null) {
            throw new IllegalArgumentException("SpawnController cannot be null");
        }
        if (collisionController == null) {
            throw new IllegalArgumentException("CollisionController cannot be null");
        }

        this.gameState = gameState;
        this.movementController = movementController;
        this.spawnController = spawnController;
        this.collisionController = collisionController;
    }

    public void updateAutomaticFish() {
        if (!runPeriodic || gameState.getMode() != GameMode.PLAYING) {
            return;
        }

        movementController.moveNonPlayerFish();
        handlePlayerCollision();
        spawnController.spawnFishIfNeeded();
    }

    public void toggleRunPeriodic() {
        this.runPeriodic = !this.runPeriodic;
    }

    private void handlePlayerCollision() {
        PlayerFish playerFish = gameState.getPlayerFish();
        if (playerFish != null) {
            collisionController.handlePlayerCollision(playerFish.getId());
        }
    }
}
