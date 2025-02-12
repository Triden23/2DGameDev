package tools;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Circle {
    public int x, y; // Center position
    public int radius;

    public Circle(int x, int y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public Ellipse2D getBounds() {
        return new Ellipse2D.Double(x - radius, y - radius, radius * 2, radius * 2);
    }

    public boolean intersects(Rectangle rect) {
        // Find the closest point on the rectangle to the circle's center
        int closestX = Math.max(rect.x, Math.min(x, rect.x + rect.width));
        int closestY = Math.max(rect.y, Math.min(y, rect.y + rect.height));

        // Calculate the distance between the circle's center and this point
        int dx = x - closestX;
        int dy = y - closestY;
        int distanceSquared = dx * dx + dy * dy;

        // Collision occurs if distanceSquared is less than or equal to radius squared
        return distanceSquared <= (radius * radius);
    }
}

