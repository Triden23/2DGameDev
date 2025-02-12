package object;

import entity.Entity;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Heart extends Entity {



    public OBJ_Heart(GamePanel gp) { // Only object from SuperObject -> Entity change that will use the image variables
        super(gp);

        name = "Heart";


        image = setup("/objects/full_heart",gp.tileSize, gp.tileSize);
        image2 = setup("/objects/quarter_missing_heart",gp.tileSize, gp.tileSize);
        image3 = setup("/objects/half_heart",gp.tileSize, gp.tileSize);
        image4 = setup("/objects/quarter_heart",gp.tileSize, gp.tileSize);
        image5 = setup("/objects/empty_heart",gp.tileSize, gp.tileSize);


    }


}
