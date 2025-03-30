package object;

import Assets.TagTracker;
import entity.Entity;
import main.GamePanel;

public class OBJ_Shield_Wood extends Entity {
    public OBJ_Shield_Wood(GamePanel gp) {
        super(gp);

        name = "Wooden Shield";
        uniqueTag = "WoodenShield";
        tagTracker = TagTracker.OBJ_Shield_Wood;
        type = 3;
        down1 = gp.assetM.getAsset("OBJ_Shield");
        defenseValue = 1;
        description = "A wooden shield, it may protect you";
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
