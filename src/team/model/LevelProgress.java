package team.model;

import java.util.Arrays;

public class LevelProgress {
    public static final int DEFAULT_INITIAL_LEVEL = 1;
    public static final int DEFAULT_INITIAL_POINTS = 0;
    public static final int[] DEFAULT_NEXT_LEVEL_THRESHOLDS = {300, 650, 1000, 1400, 2000};
    public static final int DEFAULT_MAX_LEVEL = DEFAULT_NEXT_LEVEL_THRESHOLDS.length;

    private final int initialLevel;
    private final int initialPoints;
    private final int[] nextLevelThresholds;
    private int currentLevel;
    private int currentPoints;
    private final int maxLevel;

    public static int getDefaultInitialLevelThreshold() {
        return getThresholdForLevel(DEFAULT_INITIAL_LEVEL, DEFAULT_NEXT_LEVEL_THRESHOLDS);
    }

    public LevelProgress() {
        this(
                DEFAULT_INITIAL_LEVEL,
                DEFAULT_INITIAL_POINTS,
                DEFAULT_NEXT_LEVEL_THRESHOLDS);
    }

    public LevelProgress(int currentLevel, int currentPoints, int[] nextLevelThresholds) {
        if (nextLevelThresholds == null || nextLevelThresholds.length == 0) {
            throw new IllegalArgumentException("Level thresholds cannot be empty");
        }

        this.initialLevel = currentLevel;
        this.initialPoints = currentPoints;
        this.nextLevelThresholds = Arrays.copyOf(nextLevelThresholds, nextLevelThresholds.length);
        this.currentLevel = currentLevel;
        this.currentPoints = currentPoints;
        this.maxLevel = nextLevelThresholds.length;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getCurrentPoints() {
        return currentPoints;
    }

    public int getNextLevelThreshold() {
        return getThresholdForLevel(currentLevel, nextLevelThresholds);
    }

    private static int getThresholdForLevel(int level, int[] thresholds) {
        int levelIndex = level - 1;

        if (levelIndex < 0) {
            return thresholds[0];
        }

        if (levelIndex >= thresholds.length) {
            return thresholds[thresholds.length - 1];
        }

        return thresholds[levelIndex];
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void addPoints(int points) {
        currentPoints += points;
    }

    public boolean hasReachedThreshold() {
        return currentPoints >= getNextLevelThreshold();
    }

    public int advanceLevelIfThresholdReached() {
        int previousLevel = currentLevel;

        while (currentLevel < maxLevel && hasReachedThreshold()) {
            currentLevel++;
        }

        return currentLevel - previousLevel;
    }

    public void reset() {
        currentLevel = initialLevel;
        currentPoints = initialPoints;
    }
}
