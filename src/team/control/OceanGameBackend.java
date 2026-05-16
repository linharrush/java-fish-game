package team.control;

import my_base.App;
import shared.ui_ports.GameUiPort;
import team.model.Canvas;
import team.model.Fish;

public class OceanGameBackend {

    private GameUiPort gameUiPort() {
        return GameUiPort.getInstance();
    }

    private boolean runPeriodic = true;
    private boolean oceanViewState = false;

    public void startScenario() {
        Canvas canvas = App.content().canvas();

        for (int i = 0; i < canvas.getFishCount(); i++) {
            Fish f = canvas.getFish(i);
            gameUiPort().addFish(
                    f.getId(),
                    f.getCenter().getX(),
                    f.getCenter().getY(),
                    f.getSize(),
                    f.isPlayer(),
                    f.getFishType(),
                    f.getDirection());
        }

        gameUiPort().log("Scenario started: 3 fish.");
    }

    private int fishIdToIndex(int fishId) {
        return fishId - 3;
    }

    public void moveFish(int fishId, double x, double y) {
        Canvas canvas = App.content().canvas();
        int index = fishIdToIndex(fishId);
        if (index < 0 || index >= canvas.getFishCount()) {
            return;
        }

        Fish f = canvas.getFish(index);
        if (f == null) {
            return;
        }

        f.getCenter().setX(x);
        f.getCenter().setY(y);
        gameUiPort().updateFish(fishId, x, y, f.getSize(), f.getFishType(), f.getDirection());
    }

    public void eat(int playerFishId) {
        Canvas canvas = App.content().canvas();
        int index = fishIdToIndex(playerFishId);
        if (index < 0 || index >= canvas.getFishCount()) {
            return;
        }

        Fish playerFish = canvas.getFish(index);
        if (playerFish == null || !playerFish.isPlayer()) {
            return;
        }

        double playerSize = playerFish.getSize();
        double playerX = playerFish.getCenter().getX();
        double playerY = playerFish.getCenter().getY();

        for (int i = 0; i < canvas.getFishCount(); i++) {
            if (i == index) {
                continue;
            }

            Fish otherFish = canvas.getFish(i);
            if (otherFish == null) {
                continue;
            }

            double otherSize = otherFish.getSize();
            double dx = otherFish.getCenter().getX() - playerX;
            double dy = otherFish.getCenter().getY() - playerY;
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance <= (playerSize / 2.0 + otherSize / 2.0)) {
                if (otherSize > playerSize) {
                    System.out.println("LOSE!");
                } else if (otherSize < playerSize) {
                    System.out.println("WIN!");
                }
            }
        }
    }

    public void moveFishByIndex(int index, double dx, double dy) {
        if (!runPeriodic) {
            return;
        }

        Fish f = App.content().canvas().getFish(index);
        if (f != null) {
            moveFish(f.getId(), f.getCenter().getX() + dx, f.getCenter().getY() + dy);
        }
    }

    public void toggleRunPeriodic() {
        this.runPeriodic = !this.runPeriodic;
    }

    public void oceanView() {
        this.oceanViewState = true;
        gameUiPort().updateBackgroundToOceanView();
    }

    public void normalView() {
        this.oceanViewState = false;
        gameUiPort().updateBackgroundToNormalView();
    }

    public boolean isOceanViewState() {
        return oceanViewState;
    }
}
