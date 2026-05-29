package ai.ui;

import java.util.Map;
import javax.swing.JPanel;
import shared.ui_ports.GameUiPort;
import team.model.Fish;

public class GameUiPortImpl extends GameUiPort {
    private Map<String, Fish> fish;
    private JPanel panel;
    private VideoBackgroundPanel backgroundPanel;
    private Runnable showMainMenuAction;
    private Runnable showGameScreenAction;
    private Runnable showLoseScreenAction;
    private Runnable showWinScreenAction;

    public GameUiPortImpl(
            Map<String, Fish> fish,
            JPanel panel,
            VideoBackgroundPanel backgroundPanel,
            Runnable showMainMenuAction,
            Runnable showGameScreenAction,
            Runnable showLoseScreenAction,
            Runnable showWinScreenAction) {
        this.fish = fish;
        this.panel = panel;
        this.backgroundPanel = backgroundPanel;
        this.showMainMenuAction = showMainMenuAction;
        this.showGameScreenAction = showGameScreenAction;
        this.showLoseScreenAction = showLoseScreenAction;
        this.showWinScreenAction = showWinScreenAction;
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
    public void showMainMenu() {
        showMainMenuAction.run();
    }

    @Override
    public void showGameScreen() {
        showGameScreenAction.run();
    }

    @Override
    public void showLoseScreen() {
        showLoseScreenAction.run();
    }

    @Override
    public void showWinScreen() {
        showWinScreenAction.run();
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
            double previousX = f.getCenter().getX();

            if (x < previousX) {
                f.setDirection("left");
            } else if (x > previousX) {
                f.setDirection("right");
            }

            f.getCenter().setX(x);
            f.getCenter().setY(y);
            panel.repaint();
        }
    }

    @Override
    public void growPlayerFish(int id, double growthAmount) {
        Fish f = fish.get(String.valueOf(id));

        if (f != null && growthAmount > 0) {
            f.setSize(f.getSize() + growthAmount);
            panel.repaint();
        }
    }

    @Override
    public void updateLevel(int level) {
        if (panel instanceof DrawingPanel) {
            ((DrawingPanel) panel).updateLevel(level);
            panel.repaint();
        }
    }

    @Override
    public void updateScore(int score, int targetScore) {
        if (panel instanceof DrawingPanel) {
            ((DrawingPanel) panel).updateScore(score, targetScore);
            panel.repaint();
        }
    }

    @Override
    public void removeFish(int id) {
        fish.remove(String.valueOf(id));
        panel.repaint();
    }

    @Override
    public void clearFish() {
        fish.clear();
        panel.repaint();
    }
}
