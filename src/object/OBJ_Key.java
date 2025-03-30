package object;

import Assets.TagTracker;
import entity.Entity;
import main.GamePanel;

public class OBJ_Key extends Entity {


    public OBJ_Key(GamePanel gp) {
        super(gp);

        name = "Key";
        uniqueTag = "Key";
        tagTracker = TagTracker.OBJ_Key;
        type = 3;
        down1 = gp.assetM.getAsset("OBJ_Key");
        description = "A simple key";
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
