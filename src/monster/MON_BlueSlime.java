package monster;

import Assets.TagTracker;
import entity.Behavior;
import entity.Entity;
import main.GamePanel;
import tools.Transform;

import java.util.Random;

public class MON_BlueSlime extends Entity {

    GamePanel gp;
    Random random;
    Transform node;
    boolean check = false;

    public MON_BlueSlime(GamePanel gp) {
        super(gp);
        tag = "MON_BlueSlime";
        behavior = Behavior.MoveToPoint;
        tagTracker = TagTracker.MON_BlueSlime;
        this.gp = gp;
        random = new Random();
        type = 2;
        name = "Blue Slime";
        speed = 1;
        maxLife = random.nextInt(40 - 5) + 5;
        life = maxLife;
        attack = 5;
        defense = 0;
        exp = 2;

        solidArea.x = 9;
        solidArea.y = 18;
        solidArea.width = 39;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        //Make sure to set this or they will have no logic
        actionLockFrames = 60;

        getImage();
    }

    public void getImage() {
        up1 = gp.assetM.getAsset("MON_BlueSlimeUp1");
        up2 = gp.assetM.getAsset("MON_BlueSlimeUp2");
        down1 = gp.assetM.getAsset("MON_BlueSlimeUp1");
        down2 = gp.assetM.getAsset("MON_BlueSlimeUp2");
        left1 = gp.assetM.getAsset("MON_BlueSlimeUp1");
        left2 = gp.assetM.getAsset("MON_BlueSlimeUp2");
        right1 = gp.assetM.getAsset("MON_BlueSlimeUp1");
        right2 = gp.assetM.getAsset("MON_BlueSlimeUp2");
    }

    //Behavior
    public void setAction() {
        if(behavior == Behavior.MoveToPoint){
            pathFindingController();
        }
        /*actionLockCounter++;

        if (actionLockCounter == actionLockFrames) {

            Random random = new Random();
            int i = random.nextInt(100) + 1; // picks a number form 1 - 100

            if (i <= 25) {
                direction = "up";
            }
            if (i > 25 && i <= 50) {
                direction = "down";
            }
            if (i > 50 && i <= 75) {
                direction = "left";
            }
            if (i > 75 && i <= 100) {
                direction = "right";
            }

            actionLockCounter = 0;

        }*/

    }

    public void pathFindingController(){
        if(!check){
            move();

        }else{
            if(gp.tool.distanceCalc(location,node) < gp.navMesh.range){
                check = false;
            }
        }
    }


    //pathFinding.markCurrentPointDone();

    public void move() {
        if (pathFinding.isPathPossible()) {
            String direction = pathFinding.getNextDirection();
            Transform nextLocation = pathFinding.getNextLocation();

            if (nextLocation != null && direction != null) {
                moveTowards(nextLocation, direction);
                check = true;
                  // Mark the current point as done
            }
        } else {
            // Handle path invalid, recalculate path, or fallback logic
            // Example: recalculate the path to a new target
            pathFinding.recalculatePath(new Transform(500, 500));  // New target location
        }
    }

    public void moveTowards(Transform t, String direction){
        node = t;
        this.direction = direction;
    }



    public void damageReaction() {
        actionLockCounter = 0;
        direction = gp.player.direction;
    }

}
