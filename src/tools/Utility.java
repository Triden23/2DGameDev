package tools;

public class Utility {
    public int distanceCalc(Transform target1, Transform target2) {
        int xDistance = Math.abs(target1.getWorldX() - target2.getWorldX());
        int yDistance = Math.abs(target1.getWorldY() - target2.getWorldY());
        //-gets the greatest of the 2 numbers and returns it
        int distance = Math.max(xDistance, yDistance);
        return distance;
    }

    public int calculateMagnitude(int deltaX, int deltaY) {

        return (int) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

    }
}
