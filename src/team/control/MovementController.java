package team.control;

import shared.ui_ports.GameUiPort;
import team.model.GameState;
import team.model.PlayerFish;

public class MovementController {
    private final GameState gameState;

    public MovementController(GameState gameState) {
        this.gameState = gameState;
    }

    private GameUiPort gameUiPort() {
        return GameUiPort.getInstance();
    }

    public void movePlayer(double x, double y) {
        PlayerFish playerFish = gameState.getPlayerFish();
        if (playerFish == null) {
            return;
        }

        playerFish.getCenter().setX(x);
        playerFish.getCenter().setY(y);

        gameUiPort().updatePlayerPosition(playerFish.getId(), x, y);
    }
}
