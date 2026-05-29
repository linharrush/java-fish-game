package ai.ui;

import base.Params;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import shared.MainRouter;
import team.model.Fish;

public class Ui {
    private static final String MAIN_MENU_CARD = "mainMenu";
    private static final String GAME_CARD = "game";
    private static final String LOSE_CARD = "lose";
    private static final String WIN_CARD = "win";

    private MainRouter mainRouter;
    private Map<String, Fish> fish = new HashMap<>();
    private DrawingPanel drawingPanel;
    private GameUiPortImpl uiInstance;
    private JPanel cardsPanel;
    private CardLayout cardLayout;

    public void setUiPorts() {
        // Panel will be created in createAndShowWindow, so we defer this
    }

    public void start(MainRouter mainRouter) {
        this.mainRouter = mainRouter;
        createAndShowWindow();
        uiInstance.showMainMenu();

    }

    private void createAndShowWindow() {
        JFrame frame = new JFrame("UI Window");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        drawingPanel = new DrawingPanel(fish, mainRouter);
        VideoBackgroundPanel backgroundPanel = new VideoBackgroundPanel(drawingPanel);

        MainMenuPanel mainMenuPanel = new MainMenuPanel(mainRouter);
        VideoBackgroundPanel menuBackgroundPanel = new VideoBackgroundPanel(mainMenuPanel);
        LoseScreenPanel loseScreenPanel = new LoseScreenPanel(mainRouter);
        VideoBackgroundPanel loseBackgroundPanel = new VideoBackgroundPanel(loseScreenPanel);
        WinScreenPanel winScreenPanel = new WinScreenPanel(mainRouter);
        VideoBackgroundPanel winBackgroundPanel = new VideoBackgroundPanel(winScreenPanel);
        cardLayout = new CardLayout();
        cardsPanel = new JPanel(cardLayout);
        cardsPanel.add(menuBackgroundPanel, MAIN_MENU_CARD);
        cardsPanel.add(backgroundPanel, GAME_CARD);
        cardsPanel.add(loseBackgroundPanel, LOSE_CARD);
        cardsPanel.add(winBackgroundPanel, WIN_CARD);
        frame.add(cardsPanel, BorderLayout.CENTER);

        frame.setVisible(true);

        // Start persistent video background immediately (will continue playing forever)
        menuBackgroundPanel.setBackgroundVideo("images/ocean_background.mp4");
        backgroundPanel.setBackgroundVideo("images/ocean_background.mp4");
        loseBackgroundPanel.setBackgroundVideo("images/ocean_background.mp4");
        winBackgroundPanel.setBackgroundVideo("images/ocean_background.mp4");

        uiInstance = new GameUiPortImpl(
                fish,
                drawingPanel,
                backgroundPanel,
                () -> cardLayout.show(cardsPanel, MAIN_MENU_CARD),
                () -> cardLayout.show(cardsPanel, GAME_CARD),
                () -> cardLayout.show(cardsPanel, LOSE_CARD),
                () -> cardLayout.show(cardsPanel, WIN_CARD));
        shared.ui_ports.GameUiPort.setInstance(uiInstance);
    }

}
