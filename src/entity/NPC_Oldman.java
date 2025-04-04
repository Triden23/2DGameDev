package entity;

import main.GamePanel;
import java.awt.*;
import java.util.Random;


public class NPC_Oldman extends Entity{
    /*
    Aaron suggestion, talk to him 50 times, get the spell tornado. I dont give a fuck if I have spells Im adding this.
     */


    private boolean tornadoMode; // easter egg boolean
    public NPC_Oldman(GamePanel gp){
        super(gp);

        direction = "down";
        speed = 1;
        tornadoMode = false;
        actionLockFrames = 120;

        //Collision box or hitbox
        solidArea = new Rectangle();

        solidArea.x = 8;
        solidArea.y = 16;

        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        solidArea.width = 32;
        solidArea.height = 32;

        getImage();
        setDialog();

    }

    public void getImage() {

        up1 = setup("/npc/oldman_up_1",gp.tileSize, gp.tileSize);
        up2 = setup("/npc/oldman_up_2",gp.tileSize, gp.tileSize);
        down1 = setup("/npc/oldman_down_1",gp.tileSize, gp.tileSize);
        down2 = setup("/npc/oldman_down_2",gp.tileSize, gp.tileSize);
        left1 = setup("/npc/oldman_left_1",gp.tileSize, gp.tileSize);
        left2 = setup("/npc/oldman_left_2",gp.tileSize, gp.tileSize);
        right1 = setup("/npc/oldman_right_1",gp.tileSize, gp.tileSize);
        right2 = setup("/npc/oldman_right_2",gp.tileSize, gp.tileSize);

    }

    public void setDialog(){

        dialogues[0] = "Hi billy";
        dialogues[1] = "That candy on the fridge \nyou left tasted wierd";
        dialogues[2] = "...... ";
        dialogues[3] = "I'm a tornado";

    }

    public void setAction(){

        actionLockCounter++;

        if(actionLockCounter == actionLockFrames||tornadoMode){

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

    public void speak() {

        if(dialogues[dialogIndex] == null){
            dialogIndex = 0;
            tornadoMode = false;
        }
        gp.ui.currentDialog = dialogues[dialogIndex];
        dialogIndex++;
        System.out.println("Dialog Counter: " + dialogIndex);
        if(dialogIndex == 3){
            tornadoMode = true;
        }

        switch(gp.player.direction){
            case "up":
                direction = "down";
                break;
            case "down":
                direction = "up";
                break;
            case "left":
                direction = "right";
                break;
            case "right":
                direction = "left";
                break;
        }

    }

}
