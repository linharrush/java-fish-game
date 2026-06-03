package team.control;

import shared.ui_ports.GameUiPort;
import team.model.Fish;
import team.model.GameState;
import team.model.PlayerFish;

public class CollisionController {
    private static final double HITBOX_WIDTH_SCALE = 0.9;
    private static final double HITBOX_HEIGHT_RATIO = 0.45;

    private final GameState gameState;
    private final GameController gameController;

    public CollisionController(GameState gameState, GameController gameController) {
        if (gameState == null) {
            throw new IllegalArgumentException("GameState cannot be null");
        }
        if (gameController == null) {
            throw new IllegalArgumentException("GameController cannot be null");
        }

        this.gameState = gameState;
        this.gameController = gameController;
    }

    private GameUiPort gameUiPort() {
        return GameUiPort.getInstance();
    }

    public boolean canPlayerEat(Fish otherFish) {
        PlayerFish playerFish = gameState.getPlayerFish();
        if (playerFish == null || otherFish == null) {
            return false;
        }

        return isColliding(playerFish, otherFish) && otherFish.getSize() < playerFish.getSize();
    }

    private boolean isColliding(Fish firstFish, Fish secondFish) {
        double dx = secondFish.getCenter().getX() - firstFish.getCenter().getX();
        double dy = secondFish.getCenter().getY() - firstFish.getCenter().getY();
        double combinedHalfWidth = getHitboxWidth(firstFish) / 2.0 + getHitboxWidth(secondFish) / 2.0;
        double combinedHalfHeight = getHitboxHeight(firstFish) / 2.0 + getHitboxHeight(secondFish) / 2.0;

        double normalizedX = dx / combinedHalfWidth;
        double normalizedY = dy / combinedHalfHeight;

        return normalizedX * normalizedX + normalizedY * normalizedY <= 1.0;
    }

    private double getHitboxWidth(Fish fish) {
        return fish.getSize() * HITBOX_WIDTH_SCALE;
    }

    private double getHitboxHeight(Fish fish) {
        return fish.getSize() * HITBOX_HEIGHT_RATIO;
    }

    public boolean isPlayerEatenBy(Fish otherFish) {
        PlayerFish playerFish = gameState.getPlayerFish();
        if (playerFish == null || otherFish == null) {
            return false;
        }

        return isColliding(playerFish, otherFish) && otherFish.getSize() > playerFish.getSize();
    }

    public void handlePlayerCollision(int playerFishId) {
        PlayerFish playerFish = gameState.getPlayerFish();
        if (playerFish == null || playerFish.getId() != playerFishId) {
            return;
        }

        for (int i = 0; i < gameState.getFishCount(); i++) {
            Fish otherFish = gameState.getFish(i);
            if (otherFish == null || otherFish == playerFish) {
                continue;
            }

            if (isPlayerEatenBy(otherFish)) {
                gameController.playerLost();
                return;
            } else if (canPlayerEat(otherFish)) {
                gameState.removeFish(i);
                gameState.getLevelProgress().addPoints((int) otherFish.getSize());
                int levelsGained = gameState.getLevelProgress().advanceLevelIfThresholdReached();
                double growthAmount = playerFish.getGrowthAmountForLevels(levelsGained);
                playerFish.growByLevels(levelsGained);
                gameUiPort().removeFish(otherFish.getId());
                gameUiPort().growPlayerFish(playerFish.getId(), growthAmount);
                if (levelsGained > 0) {
                    gameUiPort().updateLevel(gameState.getLevelProgress().getCurrentLevel());
                }
                gameUiPort().updateScore(
                        gameState.getLevelProgress().getCurrentPoints(),
                        gameState.getLevelProgress().getNextLevelThreshold());
                if (gameState.getLevelProgress().hasCompletedAllLevels()) {
                    gameController.playerWon();
                    return;
                }
            }
        }
    }
}
