package team.model;

public class PlayerFish extends Fish {
    private static final int GROWTH_PER_LEVEL = 15;

    private int selectedSkin;

    public PlayerFish(int id, double x, double y, int size, int selectedSkin, String direction) {
        super(id, x, y, size, true, selectedSkin, direction);
        this.selectedSkin = selectedSkin;
    }

    public int getSelectedSkin() {
        return selectedSkin;
    }

    public void setSelectedSkin(int selectedSkin) {
        this.selectedSkin = selectedSkin;
        setFishType(selectedSkin);
    }

    public double getGrowthAmountForLevels(int levelsGained) {
        if (levelsGained <= 0) {
            return 0;
        }

        return GROWTH_PER_LEVEL * levelsGained;
    }

    public void growByLevels(int levelsGained) {
        double growthAmount = getGrowthAmountForLevels(levelsGained);
        if (growthAmount <= 0) {
            return;
        }

        setSize(getSize() + growthAmount);
    }
}
