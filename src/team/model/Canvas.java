package team.model;

public class Canvas {
    private Point[] points;
    private Circle[] circles;
    private Fish[] fish;

    public void initCanvas() {
        points = new Point[2]; // Assuming 2 points
        circles = new Circle[100]; // Assuming 3 circles
        fish = new Fish[3];

        points[0] = new Point(0,110, 110); // inside circle1 (expected true)
        points[1] = new Point(1, 200, 200); // outside circle1 (expected false)

        // Circle centers (internal objects, but still ID'd)
        Point c1Center = new Point(0, 700, 300);
        Point c2Center = new Point(1, 400, 500);
        Point c3Center = new Point(2, 500, 150);

        // Circles (IDs 1..3)
        circles[0] = new Circle(0, c1Center, 50);
        circles[1] = new Circle(1, c2Center, 30);
        circles[2] = new Circle(2, c3Center, 20);

        fish[0] = new Fish(3, 700, 300, 100, true, 3, "left");
        fish[1] = new Fish(4, 400, 500, 250, false, 6, "right");
        fish[2] = new Fish(5, 500, 150, 50, false, 1, "left");
    }

    public Circle getCircle(int index) {
        return circles[index];
    }
    public Point getPoint(int index) {
        return points[index];
    }   

    public Fish getFish(int index) {
        return fish[index];
    }

    public int getFishCount() {
        int count = 0;
        for (Fish f : fish) {
            if (f != null) count++;
        }
        return count;
    }

    public void deleteCircle(int circleId) {
        // for (int i = 0; i < circles.length; i++) {
        //     if (circles[i].getId() == circleId) {
        //         circles[i] = null; // Mark as deleted
        //         break;
        //     }
        // }
        circles[circleId].setR(0.1);
        circles[circleId].getCenter().setX(0);
        circles[circleId].getCenter().setY(0);
    }

    public void addCircle(int circleId, double cx, double cy, double r) {
        Point center = new Point(circleId, cx, cy);
        Circle newCircle = new Circle(circleId, center, r);
        for (int i = 0; i < circles.length; i++) {
            if (circles[i] == null) { // Find first empty slot
                circles[i] = newCircle;
                break;
            }
        }
    }

    public int getCircleCount() {
        int count = 0;
        for (Circle c : circles) {
            if (c != null) count++;
        }
        return count;
    }
}
