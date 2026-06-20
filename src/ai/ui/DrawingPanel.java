package ai.ui;

import base.Params;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import javax.swing.*;
import shared.MainRouter;
import team.model.Fish;
import team.model.LevelProgress;

public class DrawingPanel extends JPanel {
    private static final int LEVEL_IMAGE_WIDTH = 190;
    private static final int LEVEL_PULSE_FRAMES = 18;
    private static final int LEVEL_PULSE_DELAY_MS = 25;
    private static final double LEVEL_PULSE_SCALE = 1.22;

    private Map<String, Fish> fish;
    private MainRouter mainRouter;
    private String draggedFishId;
    private Image backgroundImage;
    private JProgressBar scoreProgressBar;
    private JLabel levelImageLabel;
    private Timer levelPulseTimer;

    public DrawingPanel(Map<String, Fish> fish, MainRouter mainRouter) {
        this.fish = fish;
        this.mainRouter = mainRouter;
        this.backgroundImage = null;
        setOpaque(false);
        setLayout(new BorderLayout());
        add(createTopHudPanel(), BorderLayout.NORTH);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                draggedFishId = null;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                handleMouseDragged(e);
            }
        });
    }

    private JPanel createTopHudPanel() {
        JPanel levelPanel = createLevelPanel();
        JPanel scorePanel = createScorePanel();
        JPanel topHudPanel = new JPanel(null) {
            @Override
            public Dimension getPreferredSize() {
                Dimension levelSize = levelPanel.getPreferredSize();
                Dimension scoreSize = scorePanel.getPreferredSize();
                return new Dimension(1, Math.max(levelSize.height, scoreSize.height));
            }

            @Override
            public void doLayout() {
                Dimension levelSize = levelPanel.getPreferredSize();
                Dimension scoreSize = scorePanel.getPreferredSize();
                int levelX = (getWidth() - levelSize.width) / 2;

                levelPanel.setBounds(levelX, 0, levelSize.width, levelSize.height);
                scorePanel.setBounds(getWidth() - scoreSize.width, 0, scoreSize.width, scoreSize.height);
            }
        };
        topHudPanel.setOpaque(false);
        topHudPanel.add(levelPanel);
        topHudPanel.add(scorePanel);
        return topHudPanel;
    }

    private JPanel createLevelPanel() {
        JPanel levelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 12));
        levelPanel.setOpaque(false);
        levelImageLabel = createImageLabel(getLevelImageName(LevelProgress.DEFAULT_INITIAL_LEVEL), LEVEL_IMAGE_WIDTH);
        levelPanel.add(levelImageLabel);
        return levelPanel;
    }

    private JPanel createScorePanel() {
        JPanel scorePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 12));
        scorePanel.setOpaque(false);
        JPanel scoreMeterPanel = new JPanel(new BorderLayout(0, 4));
        scoreMeterPanel.setOpaque(false);

        JLabel scoreTitleLabel = createScoreTitleLabel();

        scoreProgressBar = new JProgressBar(
                LevelProgress.DEFAULT_INITIAL_POINTS,
                LevelProgress.getDefaultInitialLevelThreshold());
        scoreProgressBar.setPreferredSize(new Dimension(220, 28));
        scoreProgressBar.setStringPainted(true);
        scoreProgressBar.setForeground(new Color(255, 190, 60));
        scoreProgressBar.setBackground(new Color(0, 0, 0, 120));
        scoreProgressBar.setFont(scoreProgressBar.getFont().deriveFont(Font.BOLD, 14f));
        scoreProgressBar.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 170), 2));
        updateScore(
                LevelProgress.DEFAULT_INITIAL_POINTS,
                LevelProgress.getDefaultInitialLevelThreshold());

        scoreMeterPanel.add(scoreTitleLabel, BorderLayout.NORTH);
        scoreMeterPanel.add(scoreProgressBar, BorderLayout.CENTER);
        scorePanel.add(scoreMeterPanel);
        return scorePanel;
    }

    private JLabel createScoreTitleLabel() {
        ImageIcon pointsIcon = loadImageIcon("images/points.png", 160);
        JLabel scoreTitleLabel = new JLabel(pointsIcon, SwingConstants.CENTER);

        if (pointsIcon == null) {
            scoreTitleLabel.setText("\u05de\u05d3 \u05e0\u05e7\u05d5\u05d3\u05d5\u05ea");
            scoreTitleLabel.setForeground(Color.WHITE);
            scoreTitleLabel.setFont(scoreTitleLabel.getFont().deriveFont(Font.BOLD, 14f));
        }

        return scoreTitleLabel;
    }

    private JLabel createImageLabel(String imageName, int targetWidth) {
        return new JLabel(loadImageIcon(imageName, targetWidth), SwingConstants.CENTER);
    }

    private ImageIcon loadImageIcon(String imageName, int targetWidth) {
        java.net.URL url = getClass().getResource(imageName);
        if (url == null) {
            return null;
        }

        Image originalImage = new ImageIcon(url).getImage();
        int targetHeight = getScaledHeight(originalImage, targetWidth);
        Image scaledImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderBackground(g);
        renderFish(g);
    }

    private void renderBackground(Graphics g) {
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void renderFish(Graphics g) {
        if (fish == null) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;

        for (Map.Entry<String, Fish> entry : fish.entrySet()) {
            Fish f = entry.getValue();

            if (f == null || f.getCenter() == null) {
                continue;
            }

            Image fishImg = loadFishImage(f);

            if (fishImg != null) {
                int size = (int) f.getSize();
                int drawWidth = size;
                int drawHeight = getScaledHeight(fishImg, drawWidth);
                int x = (int) f.getCenter().getX() - drawWidth / 2;
                int y = (int) f.getCenter().getY() - drawHeight / 2;
                g2d.drawImage(fishImg, x, y, drawWidth, drawHeight, this);
            }
        }
    }

    private int getScaledHeight(Image image, int targetWidth) {
        int originalWidth = image.getWidth(this);
        int originalHeight = image.getHeight(this);

        if (originalWidth <= 0 || originalHeight <= 0) {
            return targetWidth;
        }

        return Math.max(1, (int) Math.round(targetWidth * (originalHeight / (double) originalWidth)));
    }

    private Image loadFishImage(Fish f) {
        String directionLetter = f.getDirection().equals("left") ? "L" : "R";
        String imagePrefix = f.isPlayer() ? "playerFish" : "fish";
        String filename = "images/" + imagePrefix + f.getFishType() + directionLetter + ".png";
        try {
            java.net.URL url = getClass().getResource(filename);
            if (url != null) {
                return new ImageIcon(url).getImage();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    public void setBackgroundImage(String imageName) {
        try {
            java.net.URL url = getClass().getResource(imageName);
            if (url != null) {
                backgroundImage = new ImageIcon(url).getImage();
            }
        } catch (Exception ignored) {
        }
    }

    public void clearBackgroundImage() {
        backgroundImage = null;
    }

    public void updateScore(int score, int targetScore) {
        int safeTargetScore = Math.max(1, targetScore);
        int visibleScore = Math.max(0, Math.min(score, safeTargetScore));

        scoreProgressBar.setMaximum(safeTargetScore);
        scoreProgressBar.setValue(visibleScore);
        scoreProgressBar.setString(score + " / " + safeTargetScore);
    }

    public void updateLevel(int level) {
        String levelImageName = getLevelImageName(level);
        levelImageLabel.setIcon(loadImageIcon(levelImageName, LEVEL_IMAGE_WIDTH));
        playLevelPulse(levelImageName);
    }

    private String getLevelImageName(int level) {
        return "images/level" + level + ".png";
    }

    private void playLevelPulse(String levelImageName) {
        if (levelPulseTimer != null && levelPulseTimer.isRunning()) {
            levelPulseTimer.stop();
        }

        final int[] frame = {0};
        levelPulseTimer = new Timer(LEVEL_PULSE_DELAY_MS, e -> {
            double progress = frame[0] / (double) LEVEL_PULSE_FRAMES;
            double pulse = Math.sin(progress * Math.PI);
            int animatedWidth = (int) Math.round(LEVEL_IMAGE_WIDTH * (1.0 + (LEVEL_PULSE_SCALE - 1.0) * pulse));

            levelImageLabel.setIcon(loadImageIcon(levelImageName, animatedWidth));
            levelImageLabel.revalidate();
            levelImageLabel.repaint();

            frame[0]++;
            if (frame[0] > LEVEL_PULSE_FRAMES) {
                ((Timer) e.getSource()).stop();
                levelImageLabel.setIcon(loadImageIcon(levelImageName, LEVEL_IMAGE_WIDTH));
            }
        });
        levelPulseTimer.start();
    }

    private void handleMousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        if (fish != null) {
            for (Map.Entry<String, Fish> entry : fish.entrySet()) {
                Fish f = entry.getValue();
                if (f == null || f.getCenter() == null) {
                    continue;
                }

                Image fishImg = loadFishImage(f);
                int fishWidth = (int) f.getSize();
                int fishHeight = fishImg != null ? getScaledHeight(fishImg, fishWidth) : fishWidth;
                int fishX = (int) f.getCenter().getX() - fishWidth / 2;
                int fishY = (int) f.getCenter().getY() - fishHeight / 2;

                if (x >= fishX && x <= fishX + fishWidth && y >= fishY && y <= fishY + fishHeight && f.isPlayer()) {
                    draggedFishId = entry.getKey();
                    return;
                }
            }
        }
    }

    private void handleMouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        if (draggedFishId != null) {
            Fish f = fish.get(draggedFishId);
            if (f != null) {
                int fishId = Integer.parseInt(draggedFishId);
                mainRouter.route("/ocean/player/move", Params.of(fishId, (double) x, (double) y));
                repaint();
            }
        }
    }
}
