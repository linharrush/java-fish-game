package team.model;

public class LevelProgress {
    private final int initialLevel;
    private final int initialPoints;
    private final int initialNextLevelThreshold;
    private int currentLevel;
    private int currentPoints;
    private int nextLevelThreshold;
    private int maxLevel;

    public LevelProgress(int currentLevel, int currentPoints, int nextLevelThreshold, int maxLevel) {
        this.initialLevel = currentLevel;
        this.initialPoints = currentPoints;
        this.initialNextLevelThreshold = nextLevelThreshold;
        this.currentLevel = currentLevel;
        this.currentPoints = currentPoints;
        this.nextLevelThreshold = nextLevelThreshold;
        this.maxLevel = maxLevel;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getCurrentPoints() {
        return currentPoints;
    }

    public int getNextLevelThreshold() {
        return nextLevelThreshold;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void addPoints(int points) {
        currentPoints += points;
    }

    public boolean hasReachedThreshold() {
        return currentPoints >= nextLevelThreshold;
    }

    public void reset() {
        currentLevel = initialLevel;
        currentPoints = initialPoints;
        nextLevelThreshold = initialNextLevelThreshold;
    }
}
