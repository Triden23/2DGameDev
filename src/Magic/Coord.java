package Magic;

public class Coord {
    //Made to act kinda like the spells targeting and

    public int x,y,targetX,targetY,dx,dy;
    int endX,endY;//used to store final x and y for effects
    public int startingX,startingY;
    String tag;

    public Coord(int x,int y, int targetX, int targetY){
        this.x = x;
        this.y = y;
        startingX = x;
        startingY = y;
        this.targetX = targetX;
        this.targetY = targetY;
    }

    public void setCoord(int x, int y, int targetX, int targetY) {
        this.x = x;
        this.y = y;
        this.targetX = targetX;
        this.targetY = targetY;
    }

    public double[] getDirectionVector() {
        int dx = targetX - startingX;
        int dy = targetY - startingY;

        double magnitude = Math.sqrt(dx * dx + dy * dy);
        if (magnitude != 0) {
            return new double[]{dx / magnitude, dy / magnitude};
        }
        return new double[]{0, 0}; // Default to no movement
    }

    public int getStartingX(){
        return startingX;
    }

    public int getStartingY(){
        return startingY;
    }

}
