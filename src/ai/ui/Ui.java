package ai.ui;

import base.Params;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import shared.MainRouter;
import team.model.Fish;

public class Ui {
    private MainRouter mainRouter;
    private Map<String, Fish> fish = new HashMap<>();
    private DrawingPanel drawingPanel;
    private GameUiPortImpl uiInstance;

    public void setUiPorts() {
        // Panel will be created in createAndShowWindow, so we defer this
    }

    public void start(MainRouter mainRouter) {
        this.mainRouter = mainRouter;
        createAndShowWindow();
        mainRouter.route("/ocean/start", Params.of()); //L.A. 07.04.26
        mainRouter.route("/ocean/oceanView", Params.of());

    }

    private void createAndShowWindow() {
        JFrame frame = new JFrame("UI Window");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        drawingPanel = new DrawingPanel(fish, mainRouter);
        VideoBackgroundPanel backgroundPanel = new VideoBackgroundPanel(drawingPanel);
        frame.add(backgroundPanel, BorderLayout.CENTER);

        frame.setVisible(true);

        // Start persistent video background immediately (will continue playing forever)
        backgroundPanel.setBackgroundVideo("images/ocean_background.mp4");

        uiInstance = new GameUiPortImpl(fish, drawingPanel, backgroundPanel);
        shared.ui_ports.GameUiPort.setInstance(uiInstance);
    }

}
