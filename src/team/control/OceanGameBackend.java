package team.control;

import base.PeriodicLoop;
import java.util.Random;
import my_base.App;
import shared.ui_ports.GameUiPort;
import team.model.Canvas;
import team.model.Fish;
import team.model.GameMode;
import team.model.LevelProgress;

public class OceanGameBackend {
    private static final int MAX_NON_PLAYER_FISH = 15;
    private static final int SPAWN_INTERVAL_MS = 600;
    private static final int SCREEN_WIDTH = 800;
    private static final int SPAWN_MARGIN = 80;
    private static final int MIN_SWIM_Y = 70;
    private static final int MAX_SWIM_Y = 530;
    private static final int OFFSCREEN_MARGIN = 120;

    private final Random random = new Random();
    private long lastSpawnTime = -SPAWN_INTERVAL_MS;

    private GameUiPort gameUiPort() {
        return GameUiPort.getInstance();
    }

    private boolean runPeriodic = true;
    private boolean oceanViewState = false;

    public void startScenario() {
        Canvas canvas = App.content().canvas();
        lastSpawnTime = -SPAWN_INTERVAL_MS;

        for (int i = 0; i < canvas.getFishCount(); i++) {
            Fish f = canvas.getFish(i);
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
        Canvas canvas = App.content().canvas();
        int index = getFishIndexById(canvas, fishId);
        if (index < 0 || index >= canvas.getFishCount()) {
            return;
        }

        Fish f = canvas.getFish(index);
        if (f == null) {
            return;
        }

        f.getCenter().setX(x);
        f.getCenter().setY(y);
        gameUiPort().updateFish(fishId, x, y, f.getSize(), f.getFishType(), f.getDirection());
    }

    public void moveFishByIndex(int index, double dx, double dy) {
        if (!runPeriodic) {
            return;
        }

        Fish f = App.content().canvas().getFish(index);
        if (f != null) {
            moveFish(f.getId(), f.getCenter().getX() + dx, f.getCenter().getY() + dy);
        }
    }

    public void updateAutomaticFish() {
        if (!runPeriodic || App.content().gameState().getMode() != GameMode.PLAYING) {
            return;
        }

        moveNonPlayerFish();
        spawnFishIfNeeded();
    }

    private void moveNonPlayerFish() {
        Canvas canvas = App.content().canvas();

        for (int i = 0; i < canvas.getFishCount(); i++) {
            Fish f = canvas.getFish(i);
            if (f == null || f.isPlayer()) {
                continue;
            }

            double dx = getSwimSpeed(f);
            if ("left".equals(f.getDirection())) {
                dx = -dx;
            }

            double nextX = f.getCenter().getX() + dx;
            if (isOffscreen(nextX)) {
                gameUiPort().removeFish(f.getId());
                canvas.removeFish(i);
            } else {
                moveFish(f.getId(), nextX, f.getCenter().getY());
            }
        }
    }

    private void spawnFishIfNeeded() {
        long now = PeriodicLoop.elapsedTime();
        Canvas canvas = App.content().canvas();

        if (now - lastSpawnTime < SPAWN_INTERVAL_MS
                || canvas.getActiveNonPlayerFishCount() >= MAX_NON_PLAYER_FISH) {
            return;
        }

        Fish spawnedFish = createRandomFishForCurrentLevel(canvas);
        canvas.addFish(spawnedFish);
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

    private Fish createRandomFishForCurrentLevel(Canvas canvas) {
        LevelProgress levelProgress = App.content().gameState().getLevelProgress();
        int level = levelProgress.getCurrentLevel();
        FishSizeCategory category = chooseSizeCategory(level, levelProgress.getMaxLevel());
        boolean fromLeft = random.nextBoolean();
        String direction = fromLeft ? "right" : "left";
        int x = fromLeft ? -SPAWN_MARGIN : SCREEN_WIDTH + SPAWN_MARGIN;
        int y = MIN_SWIM_Y + random.nextInt(MAX_SWIM_Y - MIN_SWIM_Y + 1);
        int fishType = getFishType(category);
        int size = getFishSize(category);

        return new Fish(canvas.getNextFishId(), x, y, size, false, fishType, direction);
    }

    private FishSizeCategory chooseSizeCategory(int currentLevel, int maxLevel) {
        int levelIndex = Math.max(0, currentLevel - 1);
        int maxLevelIndex = Math.max(1, maxLevel - 1);
        double difficulty = Math.min(1.0, levelIndex / (double) maxLevelIndex);
        double smallChance = 0.60 - 0.35 * difficulty;
        double mediumChance = 0.25;
        double roll = random.nextDouble();

        if (roll < smallChance) {
            return FishSizeCategory.SMALL;
        }

        if (roll < smallChance + mediumChance) {
            return FishSizeCategory.MEDIUM;
        }

        return FishSizeCategory.LARGE;
    }

    private int getFishType(FishSizeCategory category) {
        switch (category) {
            case SMALL:
                return 1 + random.nextInt(2);
            case MEDIUM:
                return 3 + random.nextInt(2);
            case LARGE:
                return 5 + random.nextInt(2);
            default:
                throw new IllegalArgumentException("Unknown fish size category");
        }
    }

    private int getFishSize(FishSizeCategory category) {
        switch (category) {
            case SMALL:
                return 45 + random.nextInt(36);
            case MEDIUM:
                return 95 + random.nextInt(56);
            case LARGE:
                return 180 + random.nextInt(101);
            default:
                throw new IllegalArgumentException("Unknown fish size category");
        }
    }

    private double getSwimSpeed(Fish fish) {
        return Math.max(4.0, 14.0 - fish.getSize() / 30.0);
    }

    private boolean isOffscreen(double x) {
        return x < -OFFSCREEN_MARGIN || x > SCREEN_WIDTH + OFFSCREEN_MARGIN;
    }

    private int getFishIndexById(Canvas canvas, int fishId) {
        for (int i = 0; i < canvas.getFishCount(); i++) {
            Fish f = canvas.getFish(i);
            if (f != null && f.getId() == fishId) {
                return i;
            }
        }

        return -1;
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

    private enum FishSizeCategory {
        SMALL,
        MEDIUM,
        LARGE
    }
}
