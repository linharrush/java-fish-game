package team.control;

import shared.ui_ports.GameUiPort;
import team.model.Fish;
import team.model.GameMode;
import team.model.GameState;
import team.model.PlayerFish;

public class GameController {
    private final GameState gameState;
    private final SpawnController spawnController;

    public GameController(GameState gameState, SpawnController spawnController) {
        if (gameState == null) {
            throw new IllegalArgumentException("GameState cannot be null");
        }
        if (spawnController == null) {
            throw new IllegalArgumentException("SpawnController cannot be null");
        }

        this.gameState = gameState;
        this.spawnController = spawnController;
    }

    private GameUiPort gameUiPort() {
        return GameUiPort.getInstance();
    }

    public void startGame(int selectedSkin) {
        gameUiPort().clearFish();
        gameState.reset();
        spawnController.resetSpawnTimer();

        PlayerFish playerFish = gameState.getPlayerFish();
        if (playerFish != null) {
            playerFish.setSelectedSkin(selectedSkin);
        }

        gameState.setMode(GameMode.PLAYING);
        gameUiPort().updateLevel(gameState.getLevelProgress().getCurrentLevel());
        gameUiPort().updateScore(
                gameState.getLevelProgress().getCurrentPoints(),
                gameState.getLevelProgress().getNextLevelThreshold());
        addActiveFishToUi();
        gameUiPort().showGameScreen();
        gameUiPort().log("Scenario started.");
    }

    private void addActiveFishToUi() {
        for (int i = 0; i < gameState.getFishCount(); i++) {
            Fish f = gameState.getFish(i);
            if (f == null) {
                continue;
            }

            gameUiPort().addFish(
                    f.getId(),
                    f.getCenter().getX(),
                    f.getCenter().getY(),
                    f.getSize(),
                    f.isPlayer(),
                    f.getFishType(),
                    f.getDirection());
        }
    }

    public void playerLost() {
        gameState.setMode(GameMode.LOST);
        gameUiPort().showLoseScreen();
    }

    public void playerWon() {
        gameState.setMode(GameMode.WON);
        gameUiPort().showWinScreen();
    }

    public void showMainMenu() {
        gameState.setMode(GameMode.MAIN_MENU);
        gameUiPort().showMainMenu();
    }
}
