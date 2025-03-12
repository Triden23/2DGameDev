package object;

import entity.Entity;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Heart extends Entity {



    public OBJ_Heart(GamePanel gp) { // Only object from SuperObject -> Entity change that will use the image variables
        super(gp);

        name = "Heart";

        image =  gp.assetM.getAsset("OBJ_FullHeart");
        image2 = gp.assetM.getAsset("OBJ_QuarterMissingHeart");
        image3 = gp.assetM.getAsset("OBJ_HalfHeart");
        image4 = gp.assetM.getAsset("OBJ_QuarterHeart");
        image5 = gp.assetM.getAsset("OBJ_EmptyHeart");

    }

    public void markDone(){
        isDone = true;
    }


}
