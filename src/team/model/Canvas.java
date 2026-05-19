package team.model;

public class Canvas {
    private Fish[] fish;

    public void initCanvas() {
        fish = new Fish[3];

        fish[0] = new PlayerFish(3, 400, 500, 90, 2, "left");
        fish[1] = new Fish(4, 100, 300, 270, false, 6, "right");
        fish[2] = new Fish(5, 500, 150, 60, false, 1, "left");
    }

    public Fish getFish(int index) {
        return fish[index];
    }

    public void removeFish(int index) {
        fish[index] = null;
    }

    public PlayerFish getPlayerFish() {
        for (Fish f : fish) {
            if (f instanceof PlayerFish) {
                return (PlayerFish) f;
            }
        }
        return null;
    }

    public int getFishCount() {
        int count = 0;
        for (Fish f : fish) {
            if (f != null) {
                count++;
            }
        }
        return count;
    }
}
