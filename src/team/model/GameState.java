package team.model;

public class GameState {
    private final Canvas canvas;

    public GameState(Canvas canvas) {
        this.canvas = canvas;
    }

    public PlayerFish getPlayerFish() {
        return canvas.getPlayerFish();
    }

    public Fish getFish(int index) {
        return canvas.getFish(index);
    }

    public int getFishCount() {
        return canvas.getFishCount();
    }
}
