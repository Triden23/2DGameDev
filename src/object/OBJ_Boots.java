package object;

import Assets.TagTracker;
import entity.Entity;
import main.GamePanel;

public class OBJ_Boots extends Entity {

    float MS = 0.25f;

    public OBJ_Boots(GamePanel gp) {
        super(gp);
        tagTracker = TagTracker.OBJ_Boots;

        name = "Boots";
        uniqueTag = "Boots";
        type = 3;
        down1 = gp.assetM.getAsset("OBJ_Boots");
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
