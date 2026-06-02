package team.model;

public class GameState {
    private final Canvas canvas;
    private final LevelProgress levelProgress;
    private GameMode mode;

    public GameState(Canvas canvas, LevelProgress levelProgress) {
        this.canvas = canvas;
        this.levelProgress = levelProgress;
        this.mode = GameMode.MAIN_MENU;
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

    public void addFish(Fish fish) {
        canvas.addFish(fish);
    }

    public int getFishCount() {
        return canvas.getFishCount();
    }

    public int getNextFishId() {
        return canvas.getNextFishId();
    }

    public int getActiveNonPlayerFishCount() {
        return canvas.getActiveNonPlayerFishCount();
    }

    public LevelProgress getLevelProgress() {
        return levelProgress;
    }

    public GameMode getMode() {
        return mode;
    }

    public void setMode(GameMode mode) {
        this.mode = mode;
    }

    public void reset() {
        canvas.initCanvas();
        levelProgress.reset();
        mode = GameMode.MAIN_MENU;
    }
}
