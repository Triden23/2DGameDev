package monster;

import entity.Entity;
import main.GamePanel;

import java.util.Random;

public class MON_BlueSlime extends Entity {

    GamePanel gp;
    Random random;
    public MON_BlueSlime(GamePanel gp) {
        super(gp);
        this.gp = gp;
        random = new Random();
        type = 2;
        name = "Blue Slime";
        speed = 1;
        maxLife = random.nextInt(40-5)+5;
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
        up1 = setup("/monster/blueslime_down_1",gp.tileSize, gp.tileSize);
        up2 = setup("/monster/blueslime_down_2",gp.tileSize, gp.tileSize);
        down1 = setup("/monster/blueslime_down_1",gp.tileSize, gp.tileSize);
        down2 = setup("/monster/blueslime_down_2",gp.tileSize, gp.tileSize);
        left1 = setup("/monster/blueslime_down_1",gp.tileSize, gp.tileSize);
        left2 = setup("/monster/blueslime_down_2",gp.tileSize, gp.tileSize);
        right1 = setup("/monster/blueslime_down_1",gp.tileSize, gp.tileSize);
        right2 = setup("/monster/blueslime_down_2",gp.tileSize, gp.tileSize);
    }

    //Behavior
    public void setAction(){
        actionLockCounter++;

        if(actionLockCounter == actionLockFrames){

            Random random = new Random();
            int i = random.nextInt(100)+1; // picks a number form 1 - 100

            if(i <= 25){
                direction = "up";
            }
            if(i > 25 && i <= 50){
                direction = "down";
            }
            if(i > 50 && i <= 75){
                direction = "left";
            }
            if(i > 75 && i <= 100){
                direction = "right";
            }

            actionLockCounter = 0;

        }

    }

    public void damageReaction(){
        actionLockCounter = 0;
        direction = gp.player.direction;
    }

}
