package object;

import Assets.TagTracker;
import entity.Entity;
import main.GamePanel;

public class OBJ_Sword_Normal extends Entity {

    public OBJ_Sword_Normal(GamePanel gp) {
        super(gp);

        name = "Normal Sword";
        uniqueTag = "NormalSword";
        tagTracker = TagTracker.OBJ_Sword_Normal;
        down1 = gp.assetM.getAsset("OBJ_Sword");
        attackValue = 1;
        description = "A really rusty sword";
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
