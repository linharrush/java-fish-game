package ai.ui;

import java.util.Map;
import javax.swing.JPanel;
import shared.ui_ports.GameUiPort;
import team.model.Fish;

public class GameUiPortImpl extends GameUiPort {
    private Map<String, Fish> fish;
    private JPanel panel;
    private VideoBackgroundPanel backgroundPanel;

    public GameUiPortImpl(Map<String, Fish> fish, JPanel panel, VideoBackgroundPanel backgroundPanel) {
        this.fish = fish;
        this.panel = panel;
        this.backgroundPanel = backgroundPanel;
    }

    @Override
    public void updateBackgroundToOceanView() {
        // Video background is persistent and always visible. No action needed.
    }

    @Override
    public void updateBackgroundToNormalView() {
        // Video background is persistent and always visible. No action needed.
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

        fish.put(String.valueOf(id), new Fish(id, x, y, (int) size, isPlayer, fishType, direction));
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

    @Override
    public void updatePlayerPosition(int id, double x, double y) {
        Fish f = fish.get(String.valueOf(id));

        if (f != null) {
            f.getCenter().setX(x);
            f.getCenter().setY(y);
            panel.repaint();
        }
    }
}
