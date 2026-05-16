package team.model;

public class PlayerFish extends Fish {
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
}
