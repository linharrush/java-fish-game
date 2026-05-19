package team.control;

import shared.ui_ports.GameUiPort;
import my_base.App;
import team.model.Fish;
import team.model.GameState;
import team.model.PlayerFish;

public class CollisionController {
    private static final double HITBOX_WIDTH_SCALE = 0.9;
    private static final double HITBOX_HEIGHT_RATIO = 0.45;

    private final GameState gameState;

    public CollisionController(GameState gameState) {
        this.gameState = gameState;
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
                App.content().gameController().playerLost();
                return;
            } else if (canPlayerEat(otherFish)) {
                System.out.println("Points before eating: " + gameState.getLevelProgress().getCurrentPoints());
                gameState.removeFish(i);
                gameState.getLevelProgress().addPoints((int) otherFish.getSize());
                System.out.println("Points after eating: " + gameState.getLevelProgress().getCurrentPoints());
                gameUiPort().removeFish(otherFish.getId());
            }
        }
    }
}
