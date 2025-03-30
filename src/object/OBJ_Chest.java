package object;

import Assets.TagTracker;
import entity.Entity;
import main.GamePanel;

public class OBJ_Chest extends Entity {

    public OBJ_Chest(GamePanel gp) {
        super(gp);
        name = "Chest";
        uniqueTag = "Chest";
        tagTracker = TagTracker.OBJ_Chest;
        down1 = gp.assetM.getAsset("OBJ_Chest");
    }

    public void markDone() {
        isDone = true;
    }

    public void reUse(int x, int y) {
        isDone = false;
        requestTransform(x, y);
        worldX = location.getWorldX();
        worldY = location.getWorldY();
    }

}
