package shared.ui_ports;

public abstract class OceanGameUiPort {

    private static OceanGameUiPort instance;

    public static void setInstance(OceanGameUiPort ui) {
        if (ui == null) throw new IllegalArgumentException("OceanGameUiPort instance cannot be null");
        if (instance != null) throw new IllegalStateException("OceanGameUiPort instance already set");
        instance = ui;
    }

    public static OceanGameUiPort getInstance() {
        if (instance == null) throw new IllegalStateException("OceanGameUiPort instance not set yet");
        return instance;
    }

    public abstract void updateBackgroundToOceanView();
    public abstract void updateBackgroundToNormalView();
    public abstract void log(String message);

    public abstract void addFish(int id, double x, double y, double size, boolean isPlayer, int fishType, String direction);
    public abstract void updateFish(int id, double x, double y, double size, int fishType, String direction);
}
