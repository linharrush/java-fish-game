package team.control;

import java.util.Random;

public class DifficultyController {
    private final Random random;

    public DifficultyController(Random random) {
        if (random == null) {
            throw new IllegalArgumentException("Random cannot be null");
        }

        this.random = random;
    }

    public FishSpawnProfile createFishSpawnProfile(int currentLevel, int maxLevel) {
        FishSizeCategory category = chooseSizeCategory(currentLevel, maxLevel);
        return new FishSpawnProfile(getFishType(category), getFishSize(category));
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

    public static class FishSpawnProfile {
        private final int fishType;
        private final int size;

        public FishSpawnProfile(int fishType, int size) {
            this.fishType = fishType;
            this.size = size;
        }

        public int getFishType() {
            return fishType;
        }

        public int getSize() {
            return size;
        }
    }

    private enum FishSizeCategory {
        SMALL,
        MEDIUM,
        LARGE
    }
}
