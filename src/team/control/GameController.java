package team.control;

import shared.ui_ports.GameUiPort;
import team.model.GameMode;
import team.model.GameState;
import team.model.PlayerFish;

public class GameController {
    private final GameState gameState;

    public GameController(GameState gameState) {
        this.gameState = gameState;
    }

    private GameUiPort gameUiPort() {
        return GameUiPort.getInstance();
    }

    public void startGame(int selectedSkin) {
        gameUiPort().clearFish();
        gameState.reset();

        PlayerFish playerFish = gameState.getPlayerFish();
        if (playerFish != null) {
            playerFish.setSelectedSkin(selectedSkin);
        }

        gameState.setMode(GameMode.PLAYING);
        gameUiPort().showGameScreen();
    }

    public void playerLost() {
        gameState.setMode(GameMode.LOST);
        gameUiPort().showLoseScreen();
    }

    public void showMainMenu() {
        gameState.setMode(GameMode.MAIN_MENU);
        gameUiPort().showMainMenu();
    }
}
