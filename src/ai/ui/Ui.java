package ai.ui;

import base.Params;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import shared.MainRouter;
import shared.ui_ports.OceanGameUiPort;
import team.model.Fish;

public class Ui {
    private MainRouter mainRouter;
    private Map<String, Point> points = new HashMap<>();
    private Map<String, Circle> circles = new HashMap<>();
    private Map<String, Fish> fish = new HashMap<>();
    private DrawingPanel drawingPanel;
    private OceanGameUiPortImpl uiInstance;

    public void setUiPorts() {
        // Panel will be created in createAndShowWindow, so we defer this
    }

    public void start(MainRouter mainRouter) {
        this.mainRouter = mainRouter;
        createAndShowWindow();
        // mainRouter.route("/ex3/start", Params.of()); //L.A. 07.04.26
        mainRouter.route("/ocean/start", Params.of()); //L.A. 07.04.26
        mainRouter.route("/ocean/oceanView", Params.of());

    }

    private void createAndShowWindow() {
        JFrame frame = new JFrame("UI Window");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        drawingPanel = new DrawingPanel(points, circles, fish, mainRouter);
        VideoBackgroundPanel backgroundPanel = new VideoBackgroundPanel(drawingPanel);
        frame.add(backgroundPanel, BorderLayout.CENTER);

        frame.setVisible(true);

        // Start persistent video background immediately (will continue playing forever)
        backgroundPanel.setBackgroundVideo("images/ocean_background.mp4");

        // Initialize Ex3UiPortImpl with references to points, circles, and panel
        // uiInstance = new Ex3UiPortImpl(points, circles, drawingPanel);
        // Ex3UiPort.setInstance((Ex3UiPort) uiInstance);
        uiInstance = new OceanGameUiPortImpl(points, circles, fish, drawingPanel, backgroundPanel);
        OceanGameUiPort.setInstance((OceanGameUiPort) uiInstance);

    //     fish.put("0", new Fish(0, 200, 200, 50, false, 4, "right"));
    //     fish.put("1", new Fish(1, 400, 250, 150, true, 5, "left"));
    //     fish.put("2", new Fish(2, 600, 300, 250, false, 6, "right"));
    }

}
