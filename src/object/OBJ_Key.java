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
        down1 =  setup("/objects/key",gp.tileSize, gp.tileSize);
        description = "A simple key";
    }
}
