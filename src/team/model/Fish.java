package team.model;

import base.IdentifiedObject;

public class Fish extends IdentifiedObject{
    private int id;
    private Point position;
    private int size;
    private boolean player;
    private int fishType;
    private String direction;

    public Fish(int id, Point position, double size) {
        super(id);
        this.position = position;
        this.size = (int) size;
        this.player = false;
        this.fishType = 1;
        this.direction = "right";
    }

    public Fish(int id, double x, double y, int size, boolean isPlayer, int fishType, String direction) {
        super(id);
        this.position = new Point(id, x, y);
        this.size = size;
        this.player = isPlayer;
        this.fishType = fishType;
        this.direction = direction;
    }

    public Point getCenter() { return position; }
    public double getSize() { return size; }

    public void setSize(double size) {
        if (size <= 0) throw new IllegalArgumentException("Size must be > 0");
        this.size = (int) size;
    }

    public boolean isPlayer() { return player; }
    public void setPlayer(boolean player) { this.player = player; }

    public int getFishType() { return fishType; }
    public void setFishType(int fishType) { this.fishType = fishType; }

    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }

}