package ai.ui;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.Timer;
import shared.ui_ports.OceanGameUiPort;
import team.model.Fish;

public class OceanGameUiPortImpl extends OceanGameUiPort {
    private Map<String, Point> points;
    private Map<String, Circle> circles;
    private Map<String, Fish> fish;
    private JPanel panel;
    private VideoBackgroundPanel backgroundPanel;
    private Map<Integer, Timer> blinkTimers = new HashMap<>();

    public OceanGameUiPortImpl(Map<String, Point> points, Map<String, Circle> circles, Map<String, Fish> fish, JPanel panel,
            VideoBackgroundPanel backgroundPanel) {
        this.points = points;
        this.circles = circles;
        this.fish = fish;
        this.panel = panel;
        this.backgroundPanel = backgroundPanel;
    }

    @Override
    public void addPoint(int pointId, double x, double y) {
        points.put(String.valueOf(pointId), new Point((int) x, (int) y));
        panel.repaint();
    }

    @Override
    public void updatePoint(int pointId, double x, double y) {
        Point point = points.get(String.valueOf(pointId));
        if (point != null) {
            point.x = (int) x;
            point.y = (int) y;
            panel.repaint();
        }
    }

    @Override
    public void addCircle(int circleId, double cx, double cy, double radius) {
        circles.put(String.valueOf(circleId), new Circle((int) cx, (int) cy, (int) radius));
        panel.repaint();
    }

    @Override
    public void updateCircle(int circleId, double cx, double cy, double radius) {
        Circle circle = circles.get(String.valueOf(circleId));
        if (circle != null) {
            circle.update((int) cx, (int) cy, (int) radius);
            circle.setOceanView(false);
            panel.repaint();
        }
    }

    @Override
    public void updateCircleToOceanView(int circleId, double cx, double cy, double radius) {
        Circle circle = circles.get(String.valueOf(circleId));
        if (circle != null) {
            circle.update((int) cx, (int) cy, (int) radius);
            circle.setOceanView(true);
            panel.repaint();
        }
    }

    @Override
    public void backCircleToNormalView(int circleId, double cx, double cy, double radius) {
        Circle circle = circles.get(String.valueOf(circleId));
        if (circle != null) {
            circle.update((int) cx, (int) cy, (int) radius);
            circle.setOceanView(false);
            panel.repaint();
        }
    }

    @Override
    public void paintPoint(int pointId, String color) {
        Point point = points.get(String.valueOf(pointId));
        if (point != null) {
            point.color = parseColor(color);
            panel.repaint();
        }
    }

    @Override
    public void blinkCircle(int circleId, int count) {
        Circle circle = circles.get(String.valueOf(circleId));
        if (circle != null) {
            // Cancel any existing blink timer for this circle
            Timer existingTimer = blinkTimers.get(circleId);
            if (existingTimer != null) {
                existingTimer.stop();
            }

            circle.isBlinking = true;
            int[] blinkCount = { 0 };

            Timer blinkTimer = new Timer(250, e -> {
                circle.isBlinking = !circle.isBlinking;
                blinkCount[0]++;
                panel.repaint();

                // Stop blinking after count blinks
                if (blinkCount[0] >= count * 2) {
                    ((Timer) e.getSource()).stop();
                    circle.isBlinking = false;
                    blinkTimers.remove(circleId);
                    panel.repaint();
                }
            });
            blinkTimer.setRepeats(true);
            blinkTimer.start();
            blinkTimers.put(circleId, blinkTimer);

            panel.repaint();
        }
    }

    private Color parseColor(String colorStr) {
        try {
            switch (colorStr.toLowerCase()) {
                case "red":
                    return Color.RED;
                case "green":
                    return Color.GREEN;
                case "blue":
                    return Color.BLUE;
                case "yellow":
                    return Color.YELLOW;
                case "black":
                    return Color.BLACK;
                case "white":
                    return Color.WHITE;
                case "cyan":
                    return Color.CYAN;
                case "magenta":
                    return Color.MAGENTA;
                default:
                    return Color.BLACK;
            }
        } catch (Exception e) {
            return Color.BLACK;
        }
    }

    public void updateBackgroundToOceanView() {
        // Video background is persistent and always visible. No action needed.
        // The video was started once at application startup and continues playing in a loop.
    }

    public void updateBackgroundToNormalView() {
        // Video background is persistent and always visible. Do not hide or clear it.
        // The video continues to play seamlessly regardless of the current view.
    }

    @Override
    public void log(String message) {
        System.out.println(message);
    }

    @Override
    public void addFish(
            int id,
            double x,
            double y,
            double size,
            boolean isPlayer,
            int fishType,
            String direction) {

        fish.put(
            String.valueOf(id),
            new Fish(id, x, y, (int) size, isPlayer, fishType, direction)
        );

        panel.repaint();
    }

    @Override
    public void updateFish(
            int id,
            double x,
            double y,
            double size,
            int fishType,
            String direction) {

        Fish f = fish.get(String.valueOf(id));

        if (f != null) {
            f.getCenter().setX(x);
            f.getCenter().setY(y);
            f.setSize(size);
            f.setFishType(fishType);
            f.setDirection(direction);

            panel.repaint();
        }
    }  
}