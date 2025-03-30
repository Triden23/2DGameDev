package Navigation;

import entity.Entity;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NavCube {
    public Rectangle rect;
    boolean occupied;
    boolean terrain;
    List<NavCube> neighbors = new ArrayList<>();

    public NavCube(int x,int xRange,int y, int yRange,Boolean terrain){
        rect = new Rectangle(x,y,xRange,yRange);
        this.terrain = terrain;
        this.occupied = false;
    }

    public void setOccupied(boolean condition){
        occupied = condition;
    }

    public void setNeighbors(List<NavCube> c){
        if(!neighbors.containsAll(c)){
            neighbors.clear();
            neighbors.addAll(c);
        }
    }

    public List<NavCube> getNeighbors(){
        return neighbors;
    }

    public boolean isPathable() {
        return !(terrain || occupied);
    }
}
