package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Shield_Wood extends Entity {
    public OBJ_Shield_Wood(GamePanel gp) {
        super(gp);

        name = "Wooden Shield";
        down1 = setup("/objects/shield",gp.tileSize,gp.tileSize);
        defenseValue = 1;
        description = "A wooden shield, it may protect you";
    }
}
