package Magic;

import entity.Entity;
import Magic.Spell;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FireBall extends Spell{
    Spell child;

    int childrenAmount = 5;
    public FireBall(GamePanel gp, Entity caster){
        super(gp,false, caster);
        spiralProgress = 0;
    }

    //copy and remove unused stats
    public void setVariables(){
        //Attributes
        name = "fireball"; //name of the spell
        currentHitBox = "projectile"; //How it handles logic
        type = "basic"; //Damage type
        childrenAmount = 10;
        effectHitBoxType = "circle";
        projectileHitBoxType = "rectangle";
        effectSpriteIncrement = 60;//Variable in charge of cycleing effect code
        projectileSpriteIncrement = 30;

        //Stats
        baseStats.scalingType = "intelligence";
        baseStats.percentOfStat = 30.00f;
        baseStats.baseDamage = 10;
        baseStats.manaCost = 10;
        baseStats.cooldown = 6;
        baseStats.castSpeed = 30;
        baseStats.projectileSpeed = 4;

        baseStats.range = 3*gp.tileSize;

        baseStats.projectileSizeX = 30;
        baseStats.projectileSizeY = 30;



        baseStats.effectSizeX = 80;
        baseStats.effectSizeY = 80;

    }

    public void setSprites(){
        projectileSprite = new BufferedImage[2];
        effectSprite = new BufferedImage[2];

        System.out.println(updatedStats.projectileSizeX);
        projectileSprite[0] = uTool.setup("/attacks/flamecircle_1",updatedStats.projectileSizeX,updatedStats.projectileSizeY);
        projectileSprite[1] = uTool.setup("/attacks/flamecircle_2",updatedStats.projectileSizeX,updatedStats.projectileSizeY);
        
        effectSprite[0] = uTool.setup("/attacks/flamecircle_4",updatedStats.effectSizeX,updatedStats.effectSizeY);
        effectSprite[1] = uTool.setup("/attacks/flameCircle_5",updatedStats.effectSizeX,updatedStats.effectSizeY);
    }

    public void projectileLogic(){
        travelPathStraight();
        if(projectileFrameCounter == updatedStats.range){
            trigger = true;
        }
    }

    public void effectLogic(){
        if(effectFrameCounter >= 120){
            //do things/spawn children
            spawnChildren();
            done = true;
        }else{
            //Effect logic

        }
    }
    //There are 3 types - circle, cone, horizontal
    public void spawnChildren(){
        childrenCoord = getChildCoords("circle",childrenAmount,baseStats.range/2);
        for(int i = 0; i < childrenAmount; i++){
            child = new ChildFireBall(gp, caster, childrenCoord[i]);
            gp.spellM.addSpell(child);
        }
    }


}
