package tools;

public class Transform {
    int worldX;
    int worldY;
    Boolean isDone;

    public Transform(int x, int y) {
        worldX = x;
        worldY = y;
        isDone = false;
    }

    public int getWorldX() {
        return worldX;
    }

    public void setWorldX(int x) {
        worldX = x;
    }

    public int getWorldY() {
        return worldY;
    }

    public void setWorldY(int y) {
        worldY = y;
    }

    public void addWorldX(int x) {
        worldX += x;
    }

    public void minusWorldX(int x) {
        worldX -= x;
    }

    public void addWorldY(int y) {
        worldY += y;
    }

    public void minusWorldY(int y) {
        worldY -= y;
    }

    public Boolean getDone() {
        return isDone;
    }

    public void markDone() {
        isDone = true;
    }

    public void reUse(int x, int y) {
        isDone = false;
        worldX = x;
        worldY = y;
    }
}
