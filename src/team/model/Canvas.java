package team.model;

import java.util.ArrayList;
import java.util.List;

public class Canvas {
    private static final int PLAYER_FISH_ID = 3;
    private List<Fish> fish;
    private int nextFishId;

    public void initCanvas() {
        fish = new ArrayList<>();
        nextFishId = PLAYER_FISH_ID + 1;

        fish.add(new PlayerFish(PLAYER_FISH_ID, 400, 500, 90, 2, "left"));
    }

    public Fish getFish(int index) {
        return fish.get(index);
    }

    public void addFish(Fish newFish) {
        fish.add(newFish);
        nextFishId = Math.max(nextFishId, newFish.getId() + 1);
    }

    public int getNextFishId() {
        return nextFishId++;
    }

    public void removeFish(int index) {
        fish.set(index, null);
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
        return fish.size();
    }

    public int getActiveNonPlayerFishCount() {
        int count = 0;
        for (Fish f : fish) {
            if (f != null && !f.isPlayer()) {
                count++;
            }
        }
        return count;
    }
}
