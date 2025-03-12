package object;

import entity.Entity;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class OBJ_Key extends Entity {



    public OBJ_Key(GamePanel gp) {
        super(gp);

        name = "Key";
        down1 =  gp.assetM.getAsset("OBJ_Key");
        description = "A simple key";
    }

    public void markDone(){
        isDone = true;
    }
}
