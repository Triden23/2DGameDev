package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Sword_Normal extends Entity{

    public OBJ_Sword_Normal(GamePanel gp) {
        super(gp);

        name = "Normal Sword";
        down1 = gp.assetM.getAsset("OBJ_Sword");
        attackValue = 1;
        description = "A really rusty sword";
    }

    public void markDone(){
        isDone = true;
    }
}
