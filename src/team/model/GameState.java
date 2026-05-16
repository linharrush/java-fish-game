package team.model;

public class GameState {
    private final Canvas canvas;
    private final LevelProgress levelProgress;

    public GameState(Canvas canvas, LevelProgress levelProgress) {
        this.canvas = canvas;
        this.levelProgress = levelProgress;
    }

    public PlayerFish getPlayerFish() {
        return canvas.getPlayerFish();
    }

    public Fish getFish(int index) {
        return canvas.getFish(index);
    }

    public void removeFish(int index) {
        canvas.removeFish(index);
    }

    public int getFishCount() {
        return canvas.getFishCount();
    }

    public LevelProgress getLevelProgress() {
        return levelProgress;
    }
}
