package team.model;

public class Canvas {
    private Fish[] fish;

    public void initCanvas() {
        fish = new Fish[3];

        fish[0] = new Fish(3, 700, 300, 100, true, 3, "left");
        fish[1] = new Fish(4, 400, 500, 250, false, 6, "right");
        fish[2] = new Fish(5, 500, 150, 50, false, 1, "left");
    }

    public Fish getFish(int index) {
        return fish[index];
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
