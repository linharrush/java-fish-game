package ai.ui;

import base.Params;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import javax.swing.*;
import shared.MainRouter;
import team.model.Fish;

public class DrawingPanel extends JPanel {
    private Map<String, Fish> fish;
    private MainRouter mainRouter;
    private String draggedFishId;
    private Image backgroundImage;

    public DrawingPanel(Map<String, Fish> fish, MainRouter mainRouter) {
        this.fish = fish;
        this.mainRouter = mainRouter;
        this.backgroundImage = null;
        setOpaque(false);

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

            Image fishImg = loadFishImage(f.getFishType(), f.getDirection());

            if (fishImg != null) {
                int size = (int) f.getSize();
                int x = (int) f.getCenter().getX() - size / 2;
                int y = (int) f.getCenter().getY() - size / 2;
                g2d.drawImage(fishImg, x, y, size, size, this);
            }
        }
    }

    private Image loadFishImage(int fishNumber, String direction) {
        String directionLetter = direction.equals("left") ? "L" : "R";
        String filename = "images/fish" + fishNumber + directionLetter + ".png";
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

    private void handleMousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        if (fish != null) {
            for (Map.Entry<String, Fish> entry : fish.entrySet()) {
                Fish f = entry.getValue();
                if (f == null || f.getCenter() == null) {
                    continue;
                }

                int size = (int) f.getSize();
                int fishX = (int) f.getCenter().getX() - size / 2;
                int fishY = (int) f.getCenter().getY() - size / 2;

                if (x >= fishX && x <= fishX + size && y >= fishY && y <= fishY + size && f.isPlayer()) {
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
                mainRouter.route("/ocean/fish/eat", Params.of(fishId));
                repaint();
            }
        }
    }
}
