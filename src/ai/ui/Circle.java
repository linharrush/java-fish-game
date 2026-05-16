package ai.ui;

public class Circle {
    public int cx, cy, radius;
    public boolean isBlinking;
    public boolean isOceanView;

    public Circle(int cx, int cy, int radius) {
        this.cx = cx;
        this.cy = cy;
        this.radius = radius;
        this.isBlinking = false;
        this.isOceanView = false;
    }

    public void update(int cx, int cy, int radius) {
        this.cx = cx;
        this.cy = cy;
        this.radius = radius;
    }

    public void setOceanView(boolean isOceanView) {
        this.isOceanView = isOceanView;
    }
}
