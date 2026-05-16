package team.model;

public class LevelProgress {
    private int currentLevel;
    private int currentPoints;
    private int nextLevelThreshold;
    private int maxLevel;

    public LevelProgress(int currentLevel, int currentPoints, int nextLevelThreshold, int maxLevel) {
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
}
