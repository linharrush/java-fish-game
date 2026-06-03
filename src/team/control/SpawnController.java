package team.control;

import base.PeriodicLoop;
import java.util.Random;
import shared.ui_ports.GameUiPort;
import team.control.DifficultyController.FishSpawnProfile;
import team.model.Fish;
import team.model.GameState;
import team.model.LevelProgress;

public class SpawnController {
    private static final int MAX_NON_PLAYER_FISH = 15;
    private static final int SPAWN_INTERVAL_MS = 600;
    private static final int SCREEN_WIDTH = 800;
    private static final int SPAWN_MARGIN = 80;
    private static final int MIN_SWIM_Y = 70;
    private static final int MAX_SWIM_Y = 530;

    private final GameState gameState;
    private final DifficultyController difficultyController;
    private final Random random;
    private long lastSpawnTime = -SPAWN_INTERVAL_MS;

    public SpawnController(
            GameState gameState,
            DifficultyController difficultyController,
            Random random) {
        if (gameState == null) {
            throw new IllegalArgumentException("GameState cannot be null");
        }
        if (difficultyController == null) {
            throw new IllegalArgumentException("DifficultyController cannot be null");
        }
        if (random == null) {
            throw new IllegalArgumentException("Random cannot be null");
        }

        this.gameState = gameState;
        this.difficultyController = difficultyController;
        this.random = random;
    }

    private GameUiPort gameUiPort() {
        return GameUiPort.getInstance();
    }

    public void resetSpawnTimer() {
        lastSpawnTime = -SPAWN_INTERVAL_MS;
    }

    public void spawnFishIfNeeded() {
        long now = PeriodicLoop.elapsedTime();

        if (now - lastSpawnTime < SPAWN_INTERVAL_MS
                || gameState.getActiveNonPlayerFishCount() >= MAX_NON_PLAYER_FISH) {
            return;
        }

        Fish spawnedFish = createRandomFishForCurrentLevel();
        gameState.addFish(spawnedFish);
        gameUiPort().addFish(
                spawnedFish.getId(),
                spawnedFish.getCenter().getX(),
                spawnedFish.getCenter().getY(),
                spawnedFish.getSize(),
                spawnedFish.isPlayer(),
                spawnedFish.getFishType(),
                spawnedFish.getDirection());
        lastSpawnTime = now;
    }

    private Fish createRandomFishForCurrentLevel() {
        LevelProgress levelProgress = gameState.getLevelProgress();
        int level = levelProgress.getCurrentLevel();
        boolean fromLeft = random.nextBoolean();
        String direction = fromLeft ? "right" : "left";
        int x = fromLeft ? -SPAWN_MARGIN : SCREEN_WIDTH + SPAWN_MARGIN;
        int y = MIN_SWIM_Y + random.nextInt(MAX_SWIM_Y - MIN_SWIM_Y + 1);
        FishSpawnProfile profile = difficultyController.createFishSpawnProfile(
                level,
                levelProgress.getMaxLevel());

        return new Fish(
                gameState.getNextFishId(),
                x,
                y,
                profile.getSize(),
                false,
                profile.getFishType(),
                direction);
    }
}
