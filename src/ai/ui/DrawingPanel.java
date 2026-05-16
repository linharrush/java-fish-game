package ai.ui;

import base.Params;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import javax.swing.*;
import shared.MainRouter;
import team.model.Fish;

public class DrawingPanel extends JPanel {
    private Map<String, Point> points;
    private Map<String, Circle> circles;
    private Map<String, Fish> fish;
    private MainRouter mainRouter;
    private String draggedPointId;
    private String draggedCircleId;
    private String draggedFishId;
    private Image fishImage;
    private Image backgroundImage;
    private Image playerFishImage;

    public DrawingPanel(Map<String, Point> points, Map<String, Circle> circles, Map<String, Fish> fish, MainRouter mainRouter) {
        this.points = points;
        this.circles = circles;
        this.fish = fish;
        this.mainRouter = mainRouter;
        this.fishImage = loadFishImage(1, "left");
        this.backgroundImage = null;
        this.playerFishImage = loadFishImage(2, "right");
        setOpaque(false); // allow a video background wrapper to show through

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                draggedPointId = null;
                draggedCircleId = null;
                draggedFishId = null;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                handleMouseDragged(e);
            }
        });

        addMouseWheelListener(e -> handleMouseWheel(e));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderBackground(g);
        renderPoints(g);
        renderCircles(g);
        renderFish(g);
    }

    private void renderBackground(Graphics g) {
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void renderPoints(Graphics g) {
        for (Map.Entry<String, Point> entry : points.entrySet()) {
            Point point = entry.getValue();
            g.setColor(point.color);
            g.fillOval(point.x - 5, point.y - 5, 10, 10);
            g.setColor(Color.BLACK);
            g.drawString(entry.getKey(), point.x + 8, point.y - 5);
        }
    }

    private void renderCircles(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2.5f));

        for (Map.Entry<String, Circle> entry : circles.entrySet()) {
            Circle circle = entry.getValue();
            if (!circle.isBlinking) {
                if (circle.isOceanView) {
                    Image imageToUse = fishImage;
                    if (entry.getKey().equals("1")) {
                        imageToUse = playerFishImage != null ? playerFishImage : fishImage;
                    }
                    if (imageToUse != null) {
                        int size = circle.radius * 2;
                        int x = circle.cx - circle.radius;
                        int y = circle.cy - circle.radius;
                        g2d.drawImage(imageToUse, x, y, size, size, this);
                    }
                } else {
                    g2d.setColor(Color.BLACK);
                    g2d.drawOval(circle.cx - circle.radius, circle.cy - circle.radius, circle.radius * 2,
                            circle.radius * 2);
                }
                if (!circle.isOceanView) {
                    g2d.setColor(Color.BLACK);
                    g2d.drawString(entry.getKey(), circle.cx + circle.radius + 5, circle.cy);
                } else if (entry.getKey().equals("1")) {
                    //g2d.setColor(Color.BLACK);
                    //g2d.drawString("Player", circle.cx + circle.radius + 5, circle.cy);
                }
            }
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

                int x = (int) f.getCenter().getX() - size/2;
                int y = (int) f.getCenter().getY() - size/2;

                g2d.drawImage(fishImg, x, y, size, size, this);
            }
        }
    }

    private Image loadFishImage(int fishNumber, String direction) {
        String directionLetter = direction.equals("left") ? "L" : "R";
        String filename = "images/fish" + fishNumber + directionLetter + ".png";
        try {
            java.net.URL url = getClass().getResource(filename);
            // java.net.URL url = getClass().getResource("nemo.gif");
            if (url != null) {
                return new ImageIcon(url).getImage();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private Image loadPlayerFishImage() {
        return loadFishImage(2, "right");
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

        // Check if a fish was clicked first, so player fish takes priority over circles.
        if (fish != null) {
            for (Map.Entry<String, Fish> entry : fish.entrySet()) {
                Fish f = entry.getValue();
                if (f == null || f.getCenter() == null) {
                    continue;
                }
                int size = (int) f.getSize();
                int fishX = (int) f.getCenter().getX() - size / 2;
                int fishY = (int) f.getCenter().getY() - size / 2;
                if (x >= fishX && x <= fishX + size && y >= fishY && y <= fishY + size) {
                    if (f.isPlayer()) {
                        draggedFishId = entry.getKey();
                        draggedPointId = null;
                        draggedCircleId = null;
                        return;
                    }
                    // Non-player fish are not draggable by mouse; continue to allow circle/point selection.
                }
            }
        }

        // Check if a point was clicked
        for (Map.Entry<String, Point> entry : points.entrySet()) {
            Point p = entry.getValue();
            if (Math.abs(p.x - x) < 10 && Math.abs(p.y - y) < 10) {
                draggedPointId = entry.getKey();
                return;
            }
        }

        // Check if a circle was clicked
        for (Map.Entry<String, Circle> entry : circles.entrySet()) {
            Circle c = entry.getValue();
            double dist = Math.sqrt(Math.pow(c.cx - x, 2) + Math.pow(c.cy - y, 2));
            if (dist <= c.radius) {
                draggedCircleId = entry.getKey();
                // Stop any blinking animation on this circle
                stopBlinking(entry.getKey());
                return;
            }
        }
    }

    private void handleMouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        if (draggedPointId != null) {
            Point p = points.get(draggedPointId);
            if (p != null) {
                int pointId = Integer.parseInt(draggedPointId);
                mainRouter.route("/ocean/point/move", Params.of(pointId, (double) x, (double) y));
                repaint();
            }
        } else if (draggedFishId != null) {
            Fish f = fish.get(draggedFishId);
            if (f != null) {
                int fishId = Integer.parseInt(draggedFishId);
                mainRouter.route("/ocean/fish/move", Params.of(fishId, (double) x, (double) y));
                mainRouter.route("/ocean/fish/eat", Params.of(fishId));
                repaint();
            }
        } else if (draggedCircleId != null) {
            Circle c = circles.get(draggedCircleId);
            if (c != null) {
                int circleId = Integer.parseInt(draggedCircleId);
                mainRouter.route("/ocean/circle/move", Params.of(circleId, (double) x, (double) y));
                repaint();
            }
        }
    }

    private void handleMouseWheel(MouseWheelEvent e) {
        int x = e.getX();
        int y = e.getY();
        int rotation = e.getWheelRotation();

        // Check if hovering over a circle
        for (Map.Entry<String, Circle> entry : circles.entrySet()) {
            Circle c = entry.getValue();
            double dist = Math.sqrt(Math.pow(c.cx - x, 2) + Math.pow(c.cy - y, 2));
            if (dist <= c.radius + 10) {
                // Stop any blinking animation on this circle
                stopBlinking(entry.getKey());
                int newRadius = Math.max(10, c.radius - rotation * 5);
                int circleId = Integer.parseInt(entry.getKey());
                // mainRouter.route("/ex3/circle/radius", Params.of(circleId, (double) newRadius)); //L.A. 07.04.26
                mainRouter.route("/ocean/circle/radius", Params.of(circleId, (double) newRadius)); //L.A. 07.04.26
                repaint();
                return;
            }
        }
    }

    public void stopBlinking(String circleId) {
        Circle c = circles.get(circleId);
        if (c != null) {
            c.isBlinking = false;
            repaint();
        }
    }
}
