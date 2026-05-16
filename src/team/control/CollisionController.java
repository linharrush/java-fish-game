package team.control;

import shared.ui_ports.GameUiPort;
import team.model.Fish;
import team.model.GameState;
import team.model.PlayerFish;

public class CollisionController {
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
        double distance = Math.sqrt(dx * dx + dy * dy);

        return distance <= (firstFish.getSize() / 2.0 + secondFish.getSize() / 2.0);
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
                System.out.println("LOSE!");
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
