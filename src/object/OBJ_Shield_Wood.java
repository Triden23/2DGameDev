package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Shield_Wood extends Entity {
    public OBJ_Shield_Wood(GamePanel gp) {
        super(gp);

        name = "Wooden Shield";
        down1 = gp.assetM.getAsset("OBJ_Shield");
        defenseValue = 1;
        description = "A wooden shield, it may protect you";
    }

    public void markDone(){
        isDone = true;
    }
}
