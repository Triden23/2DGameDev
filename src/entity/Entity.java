package entity;

import Magic.Enhancements;
import Magic.Spell;
import main.GamePanel;
import main.UtilityTool;

import tools.Circle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Entity {

    GamePanel gp;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage attackUp1,attackUp2,attackDown1,attackDown2,attackLeft1,attackLeft2,attackRight1,attackRight2;
    public BufferedImage image,image2,image3,image4,image5;

    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collision = false;
    String dialogues[] = new String[20];

    //HitBoxes

    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public Rectangle attackArea = new Rectangle(0,0,0,0);

    //Timers
    int hpBarTimer = 4*60;
    int invincibleTimer = 40;

    //STATES
    public int worldX,worldY;
    public String direction = "down";
    public int spriteNum = 1;
    int dialogIndex = 0;
    public boolean invincible = false;
    public boolean collisionOn = false;
    public boolean attacking = false;
    public boolean alive = true;
    public boolean dying = false;
    boolean hpBarOn = false;


    //COUNTERS
    public int spriteCounter = 0;
    public int invincibleCounter = 0;
    public int actionLockCounter = 0;
    public int actionLockFrames = 0;
    int hpBarCounter = 0;
    //Death related counters
    int deathCounter = 0;
    int deathFrames = 40;
    int deathInterval = 5;
    //CHARACTER ATTRIBUTES
    public int type; //Type of entity - 0 = player, 1 = npc, 2 = monster
    public String name;
    public int maxLife;
    public int life;
    public int speed;
    public int level;
    public int strength;
    public int dexterity;
    public int intelligence;
    public int attack;
    public int defense;
    public int exp;
    public int nextLevelExp;
    public int coin;

    public Enhancements enhancements = new Enhancements();

    public Entity currentWeapon;
    public Entity currentShield;

    public Spell[] spells;
    public int[] cooldown;

    //Tracking tag
    public String tag; //allows for spell management to find and track entities
    //ITEM ATTRIBUTES
    public int attackValue;
    public int defenseValue;
    public String description;
    //Spells

    public Entity(GamePanel gp){
        this.gp = gp;
    }

    //behavior for npc
    public void setAction(){ } // Set the action of the monster
    public void damageReaction(){ } // Set the behavior of the monster after taking damage
    public void speak(){

        if(dialogues[dialogIndex] == null){
            dialogIndex = 0;
        }
        gp.ui.currentDialog = dialogues[dialogIndex];
        dialogIndex++;
        System.out.println("Dialog Counter: " + dialogIndex);

        switch(gp.player.direction){
            case "up":      direction = "down";     break;
            case "down":    direction = "up";       break;
            case "left":    direction = "right";    break;
            case "right":   direction = "left";     break;
        }

    }
    public void update(){

        setAction();

        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this,false);
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.monster);
        boolean contactPlayer = gp.cChecker.checkPlayer(this);

        if(this.type == 2 && contactPlayer){
            if(!gp.player.invincible){
                int damage = attack - gp.player.defense;

                if(damage < 0) {
                    damage = 0;
                }

                gp.player.life -= damage;
                gp.player.invincible = true;
            }
        }

        if(!collisionOn){

            switch(direction){
                case "up":      worldY -= speed;    break;
                case "down":    worldY += speed;    break;
                case "left":    worldX -= speed;    break;
                case "right":   worldX += speed;    break;
            }

        }



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

        if(invincible){
            invincibleCounter++;
            if(invincibleCounter > invincibleTimer){
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }
    public void draw(Graphics2D g2){
        BufferedImage image = null;

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        //this if acts like a render distance with a buffer of 1 tile in each direction to smooth camera rendering
        if(worldX + gp.tileSize> gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize< gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize> gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize< gp.player.worldY + gp.player.screenY) {

            switch (direction) {
                case "up":
                    if(spriteNum == 1)  {   image = up1;    }
                    if(spriteNum == 2)  {   image = up2;    }
                    break;
                case "down":
                    if(spriteNum == 1)  {   image = down1;  }
                    if(spriteNum == 2)  {   image = down2;  }
                    break;
                case "left":
                    if(spriteNum == 1)  {   image = left1;  }
                    if(spriteNum == 2)  {   image = left2;  }
                    break;
                case "right":
                    if(spriteNum == 1)  {   image = right1; }
                    if(spriteNum == 2)  {   image = right2; }
                    break;
            }

            // Monster HP BAR Come up with a way to only display if they take damage or are near the player
            if(type == 2 && hpBarOn){
                double oneScale = (double)gp.tileSize/maxLife;
                double hpBarValue = oneScale*life;

                g2.setColor(new Color(35,35,35));
                g2.drawRect(screenX-2,screenY-17, gp.tileSize+2, 12);

                g2.setColor(new Color(255,0,30));
                g2.fillRect(screenX,screenY - 15, (int)hpBarValue, 10);

                hpBarCounter++;

                if(hpBarCounter>=hpBarTimer){
                    hpBarOn = false;
                    hpBarCounter=0;
                }
            }

            if(!hpBarOn && type == 2){
                if(distanceFromPlayer(gp.tileSize*2)){
                    hpBarOn = true;
                }
            }

            if(invincible){
                hpBarOn = true;
                hpBarCounter = 0;
                changeAlpha(g2,0.4f);
            }
            if(dying){
                deathAnimation(g2);
            }
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            changeAlpha(g2,1f);
        }

    }

    public boolean distanceFromPlayer(int amount){
        boolean isClose = false;
        int xDistance = Math.abs(gp.player.worldX-worldX);
        int yDistance = Math.abs(gp.player.worldY-worldY);
        //-gets the greatest of the 2 numbers and returns it
        int distance = Math.max(xDistance,yDistance);

        //If they are closer than 2 tiles, return true
        if(distance <= amount){  isClose = true; }
        //Debug
        //System.out.println("Distance: " + distance + "| Needed distance: " + gp.tileSize*4 + "| Returning: " + isClose);
        return isClose;
    }

    //Death animation can be redone later
    public void deathAnimation(Graphics2D g2) {
        deathCounter++;

        //Every 5 frames change the alpha to transparent used ternary to simplify code without reducing the ability to read it. if(dyingCounter%2==0) 0f else 1f
        float alpha = ((deathCounter / deathInterval) % 2 == 0) ? 0f : 1f;
        changeAlpha(g2,alpha);

        if (deathCounter > deathFrames) {
            dying = false;
            alive = false;
            changeAlpha(g2,1f);
        }
    }

    //SPELL UPDATE
    public void takeDamage(int amount, String message){//Only really used by spell
        life -= amount;
        gp.ui.addMessage(message);
        invincible = true;
        if(life < 0) life = 0;
        //Maybe make a death method. Might want monsters that split into smaller monsters later down the road
        if(life <= 0){
            dying = true;
            gp.ui.addMessage("Killed the " + name + "!");
            gp.ui.addMessage("Exp +" + exp + "!");
            gp.player.exp += exp;
            gp.player.checkLevelUp();
        }
    }

    public void setSpell(int index,Spell spell){
        spells[index] = spell;
        cooldown[index] = spell.baseStats.cooldown;
    }

    public void changeAlpha(Graphics2D g2, Float alpha){
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    }

    public BufferedImage setup(String imagePath, int width, int height){

        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try{
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaleImage(image, width, height);
        }catch(IOException e){
            e.printStackTrace();
        }
        return image;
    }



}
