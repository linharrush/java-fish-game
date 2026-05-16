package shared.ui_ports;

public abstract class OceanGameUiPort {

    private static OceanGameUiPort instance;

    public static void setInstance(OceanGameUiPort ui) {
        if (ui == null) throw new IllegalArgumentException("Ex3UiPort instance cannot be null");
        if (instance != null) throw new IllegalStateException("Ex3UiPort instance already set");
        instance = ui;
    }

    public static OceanGameUiPort getInstance() {
        if (instance == null) throw new IllegalStateException("Ex3UiPort instance not set yet");
        return instance;
    }

    // Your UI commands here, for example:
    public abstract void addPoint(int id, double x, double y);
    public abstract void updatePoint(int id, double x, double y);

    public abstract void addCircle(int id, double cx, double cy, double r);
    public abstract void updateCircle(int id, double cx, double cy, double r);
    public abstract void updateCircleToOceanView(int id, double cx, double cy, double r);
    public abstract void backCircleToNormalView(int id, double cx, double cy, double r);

    public abstract void paintPoint(int pointId, String colorName);
    public abstract void blinkCircle(int circleId, int times);
    public abstract void updateBackgroundToOceanView();
    public abstract void updateBackgroundToNormalView();
    public abstract void log(String message);

    public abstract void addFish(int id, double x, double y, double size, boolean isPlayer, int fishType, String direction);
    public abstract void updateFish( int id, double x, double y, double size, int fishType, String direction);
}