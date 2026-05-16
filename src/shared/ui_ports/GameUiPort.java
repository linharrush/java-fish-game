package shared.ui_ports;

public abstract class GameUiPort {

    private static GameUiPort instance;

    public static void setInstance(GameUiPort ui) {
        if (ui == null) throw new IllegalArgumentException("GameUiPort instance cannot be null");
        if (instance != null) throw new IllegalStateException("GameUiPort instance already set");
        instance = ui;
    }

    public static GameUiPort getInstance() {
        if (instance == null) throw new IllegalStateException("GameUiPort instance not set yet");
        return instance;
    }

    public abstract void updateBackgroundToOceanView();
    public abstract void updateBackgroundToNormalView();
    public abstract void log(String message);

    public abstract void addFish(int id, double x, double y, double size, boolean isPlayer, int fishType, String direction);
    public abstract void updateFish(int id, double x, double y, double size, int fishType, String direction);
    public abstract void updatePlayerPosition(int id, double x, double y);
    public abstract void removeFish(int id);
}
