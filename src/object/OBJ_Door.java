package object;

import Assets.TagTracker;
import entity.Entity;
import main.GamePanel;

public class OBJ_Door extends Entity {


    public OBJ_Door(GamePanel gp) {

        super(gp);
        name = "Door";
        uniqueTag = "Door";
        tagTracker = TagTracker.OBJ_Door;
        type = 3;
        down1 = gp.assetM.getAsset("OBJ_Door");
        collision = true;

        solidArea.x = 0;
        solidArea.y = 16;
        solidArea.width = 48;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    public void markDone() {
        isDone = true;
    }

    public void reUse(int x, int y) {
        isDone = false;
        requestTransform(x, y);
        collision = true;
        worldX = location.getWorldX();
        worldY = location.getWorldY();
    }

}
