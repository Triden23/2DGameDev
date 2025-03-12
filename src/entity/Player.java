package entity;

import Magic.Coord;
import Magic.Spell;
import main.GamePanel;
import main.KeyHandler;

import object.OBJ_Key;
import object.OBJ_Shield_Wood;
import object.OBJ_Sword_Normal;




import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends Entity{

    KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public int standCounter = 0;
    public boolean attackCancled = false;

    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = gp.ui.slotColMax * gp.ui.slotRowMax;

    public Player(GamePanel gp, KeyHandler keyH){
        super(gp);
        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        spells = new Spell[2];//Limit for player unless they get new items that may let them stack spells in a slot
        cooldown = new int[spells.length];
        cooldown[0] = 0;
        cooldown[1] = 0;

        //Collision box or hitbox
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        attackArea.width = 36;
        attackArea.height = 36;


        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImages();
        setItems();
    }

    public void setUpClass(String playerClass){
        switch(playerClass){
            case "Fighter":
                maxLife = 14;
                life = maxLife;
                spells[0] = gp.spellM.getSpell("fireball",this);
                spells[1] = gp.spellM.getSpell("fireball",this);
                //other stats effected by class selection
                break;
            case "Mage":
                maxLife = 25;
                life = maxLife;
                spells[0] = gp.spellM.getSpell("fireball",this);
                spells[1] = gp.spellM.getSpell("fireball",this);
                //other stats effected by the class selection
                break;
            case "Archer":
                maxLife = 10;
                life = maxLife;
                spells[0] = gp.spellM.getSpell("fireball",this);
                spells[1] = gp.spellM.getSpell("fireball",this);
                //Other stats effected by the class selection
                break;
            case "Tornado":
                life = maxLife;
                maxLife = 20;
                spells[0] = gp.spellM.getSpell("fireball",this);
                spells[1] = gp.spellM.getSpell("fireball",this);
                break;
        }
    }

    public void setDefaultValues(){
        spells = new Spell[2];//Default value is 2 for player as there will only be 2 spells, maybe right click will be coded as spell and so will left click.
        location = gp.objectP.getTransform(gp.tileSize * 23,gp.tileSize * 21);
        speed = 4;
        direction = "down";
        tag = "Player";
        //PLAYER STATUS

        //4 life = 1 heart
        //start with 3
        maxLife = 16;
        life = maxLife;
        level = 1;
        strength = 1; // the more strength, the more damage given
        dexterity = 1; // the more defense, the less damage you recieve
        exp = 0;
        nextLevelExp = 5;
        coin = 0;
        //Maybe
        currentWeapon = new OBJ_Sword_Normal(gp);
        currentShield = new OBJ_Shield_Wood(gp);
        attack = getAttack(); // the total attack from strength
        defense = getDefense();
    }

    public void setItems(){

        inventory.add(currentWeapon);
        inventory.add(currentShield);
        inventory.add(new OBJ_Key(gp));
        inventory.add(new OBJ_Key(gp));
        inventory.add(new OBJ_Key(gp));
        inventory.add(new OBJ_Key(gp));
        inventory.add(new OBJ_Key(gp));
        inventory.add(new OBJ_Key(gp));
        inventory.add(new OBJ_Key(gp));
        inventory.add(new OBJ_Key(gp));
        inventory.add(new OBJ_Key(gp));
        inventory.add(new OBJ_Key(gp));

    }

    public int getAttack(){
        return attack = strength * currentWeapon.attackValue;
    }

    public int getDefense(){
        return defense = dexterity * currentShield.defenseValue;
    }

    public void update(){
        worldX = location.getWorldX();
        worldY = location.getWorldY();
        if(attacking){
            attacking();
        }

        //Key Handling, put in its own method in the future and call it.
        //Locks animation changing for only when a key is pressed.
        else if(keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed || keyH.enterPressed){
            if(keyH.upPressed){
                direction = "up";
            }else if(keyH.downPressed){
                direction = "down";
            }else if(keyH.leftPressed){
                direction = "left";
            }else if(keyH.rightPressed){
                direction = "right";
            }

            // Check Tile collision
            collisionOn = false;
            gp.cChecker.checkTile(this);

            // Check object collision
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            // Check for npc collision
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            // Check for monster collision
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);



            //CHECK EVENT
            gp.eHandler.checkEvent();

        // If collision is false, player can move

            if(!collisionOn && !keyH.enterPressed){

                switch(direction){
                    case "up":      location.minusWorldY(speed);    break;
                    case "down":    location.addWorldY(speed);    break;
                    case "left":   location.minusWorldX(speed);    break;
                    case "right":   location.addWorldX(speed);    break;
                }

            }
            //Checking to see if the enter key is pressed and if there is anything preventing the attack
            if(keyH.enterPressed && !attackCancled){
                gp.playSE(7);
                attacking = true;
                spriteCounter = 0;
            }
            attackCancled = false;
            //Reset enter key in case of any player interactions with npc
            if(keyH.enterPressed) { keyH.enterPressed = false; }


            //Simple, Dirty animation changer (redo later)
            spriteCounter++;
            if(spriteCounter > 12){
                if(spriteNum == 1){
                    spriteNum = 2;
                } else if(spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }else{
            //returns the player to the idle position after 12 frames.
            if(spriteNum != 1) {

                standCounter++;
                if(standCounter >= 12){
                    standCounter = 0;
                    spriteNum = 1;
                }

            }

        }
        //Q right now
        for(int i = 0; i < spells.length; i++){
            if(cooldown[i] > 0){
                cooldown[i]--;
            }

        }
        if(keyH.spellOnePressed){

            if(cooldown[0] == 0){
                castSpell(0);
            }

            keyH.spellOnePressed = false;
        }

        if(keyH.spellTwoPressed){

            if(cooldown[1] == 0){
                castSpell(1);
            }

            keyH.spellTwoPressed = false;
        }


        //Outside the key press statements
        if(invincible){
            invincibleCounter++;
            if(invincibleCounter > 60){
                invincible = false;
                invincibleCounter = 0;
            }
        }


    }

    public void castSpell(int index){
        spells[index].casting = true;
        int[] target = getTarget(index);
        spells[index].coord.setCoord(location.getWorldX(),location.getWorldY(),target[0],target[1]);
        //System.out.println("" + worldX + " " + worldY + " " + target[0] + " " + target[1]);
        //System.out.println("" + spells[index].coord.x + " " + spells[index].coord.y + " " + spells[index].coord.targetX + " " + spells[index].coord.targetY);
        gp.spellM.addSpell(spells[index]);
        cooldown[index] = spells[index].baseStats.cooldown;
        spells[index] = gp.spellM.getSpell(spells[index].name,this);
    }

    public int[] getTarget(int index){
        int[] target = new int[2];
        if(direction.equals("up")){
            target[0] = location.getWorldX();
            target[1] = location.getWorldY()-(spells[index].baseStats.range);
        }
        if(direction.equals("down")){
            target[0] = location.getWorldX();
            target[1] = worldY+(spells[index].baseStats.range);
        }
        if(direction.equals("left")){
            target[0] = location.getWorldX()-(spells[index].baseStats.range);
            target[1] = location.getWorldY();
        }
        if(direction.equals("right")){
            target[0] = location.getWorldX()+(spells[index].baseStats.range);
            target[1] = location.getWorldY();
        }
        return target;
    }

    private void attacking() {

        spriteCounter++;

        if(spriteCounter <= 5){
            spriteNum = 1;
        }
        if(spriteCounter > 5 && spriteCounter <= 25){
            spriteNum = 2;

            //Save the current worldX, worldY, solidArea
            int currentWorldX = location.getWorldX();
            int currentWorldY = location.getWorldY();
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            //Adjust the player's worldX worldY for the attackArea
            switch(direction) {
                case "up": location.minusWorldY(attackArea.height); break;
                case "down": location.addWorldY(attackArea.height); break;
                case "left": location.minusWorldX(attackArea.width); break;
                case "right": location.addWorldX(attackArea.width); break;
            }

            //attackArea becomes solidArea
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;
            //Check monster collision with the updated world X World Y and solidArea
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            //SPELL UPDATE - ADDED INT AMOUNT
            damageMonster(monsterIndex);
            //After checking collision, resolve the original data
            location.setWorldX(currentWorldX);
            location.setWorldY(currentWorldY);
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;

        }
        if(spriteCounter > 25){
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }

    }

    public void getPlayerImage() {
        up1 =   gp.assetM.getAsset("PlayerUp1");
        up2 =   gp.assetM.getAsset("PlayerUp2");
        down1 = gp.assetM.getAsset("PlayerDown1");
        down2 = gp.assetM.getAsset("PlayerDown2");
        left1 = gp.assetM.getAsset("PlayerLeft1");
        left2 = gp.assetM.getAsset("PlayerLeft2");
        right1 =gp.assetM.getAsset("PlayerRight1");
        right2 =gp.assetM.getAsset("PlayerRight2");
    }

    public void getPlayerAttackImages() {
        attackUp1 =     gp.assetM.getAsset("PlayerAttackUp1");
        attackUp2 =     gp.assetM.getAsset("PlayerAttackUp2");
        attackDown1 =   gp.assetM.getAsset("PlayerAttackDown1");
        attackDown2 =   gp.assetM.getAsset("PlayerAttackDown2");
        attackLeft1 =   gp.assetM.getAsset("PlayerAttackLeft1");
        attackLeft2 =   gp.assetM.getAsset("PlayerAttackLeft2");
        attackRight1 =  gp.assetM.getAsset("PlayerAttackRight1");
        attackRight2 =  gp.assetM.getAsset("PlayerAttackRight2");
    }

    public void pickUpObject(int i){

        //Check to see if it is not the default return
        if(i != 999){

        }

    }

    public void interactNPC(int i) {

        //Check to see if it is not the default return
        if(keyH.enterPressed) {
            if(i != 999){
                attackCancled = true;
                gp.gameState = gp.dialogState;
                gp.npc[i].speak();
            }
        }

    }

    public void contactMonster(int i) {
        if(i != 999){

            if(!invincible){
                //Take damage sound effect
                gp.playSE(6);
                int damage = gp.monster[i].attack - defense;

                if(damage < 0) {
                    damage = 0;
                }
                life -= damage;
                invincible = true;
            }

        }
    }

    public void damageMonster(int i){
        if(i != 999){
            //Check if it is invincible
            if(!gp.monster[i].invincible){
                gp.playSE(5);

                int damage = attack - gp.monster[i].defense;

                if(damage < 0) {
                    damage = 0;
                }


                gp.monster[i].life -= damage;
                gp.ui.addMessage(damage + " damage!");
                gp.monster[i].invincible = true;
                gp.monster[i].damageReaction();
                if(gp.monster[i].life < 0) gp.monster[i].life = 0;
                //Maybe make a death method. Might want monsters that split into smaller monsters later down the road
                if(gp.monster[i].life <= 0){
                    gp.monster[i].dying = true;
                    gp.ui.addMessage("Killed the " + gp.monster[i].name + "!");
                    gp.ui.addMessage("Exp +" + gp.monster[i].exp + "!");
                    exp += gp.monster[i].exp;
                    checkLevelUp();

                }
            }

        }
    }

    public void takeDamage(int amount, String message){
        gp.ui.addMessage(message);
        invincible = true;
        life-=amount;
    }

    public void checkLevelUp(){

        if(exp >= nextLevelExp){
            level++;
            nextLevelExp = nextLevelExp*2;//Change value later
            maxLife+=4;
            strength++;
            dexterity++;
            attack = getAttack();
            defense = getDefense();

            gp.playSE(8);
            gp.gameState = gp.dialogState;
            gp.ui.currentDialog = "You are level " + level + " now!";
        }

    }

    public void draw(Graphics2D g2){

        BufferedImage image = null;

        int tempScreenX = screenX;
        int tempScreenY = screenY;


        switch (direction) {
            case "up":
                if(!attacking){
                    if(spriteNum == 1)  {image = up1;}
                    if(spriteNum == 2)  {image = up2;}
                }
                if(attacking){
                    tempScreenY = screenY-gp.tileSize;
                    if(spriteNum == 1)  {image = attackUp1;}
                    if(spriteNum == 2)  {image = attackUp2;}
                }
                break;

            case "down":
                if(!attacking){
                    if(spriteNum == 1)  {image = down1;}
                    if(spriteNum == 2)  {image = down2;}
                }
                if(attacking){
                    if(spriteNum == 1)  {image = attackDown1;}
                    if(spriteNum == 2)  {image = attackDown2;}
                }
                break;

            case "left":
                if(!attacking){
                    if(spriteNum == 1)  {image = left1;}
                    if(spriteNum == 2)  {image = left2;}
                }
                if(attacking){
                    tempScreenX = screenX - gp.tileSize;
                    if(spriteNum == 1)  {image = attackLeft1;}
                    if(spriteNum == 2)  {image = attackLeft2;}
                }
                break;

            case "right":
                if(!attacking){
                    if(spriteNum == 1)  {image = right1;}
                    if(spriteNum == 2)  {image = right2;}
                }
                if(attacking){
                    if(spriteNum == 1)  {image = attackRight1;}
                    if(spriteNum == 2)  {image = attackRight2;}
                }
                break;
        }


        
        if(gp.showCollisionArea){
            g2.setColor(Color.red);
            g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        }

        //If the player is in an Invisible frames it will be slightly transparent
        if(invincible){
            changeAlpha(g2,0.3f);
        }
        g2.drawImage(image,tempScreenX,tempScreenY,null);
        //Reset alpha or everything will be slightly transparent
        changeAlpha(g2,1f);



        //DEBUG
        //g2.setFont(new Font("Arial", Font.PLAIN, 26));
        //g2.setColor(Color.white);
        //g2.drawString("Invincible: " + invincibleCounter, 10,400);
    }




}
