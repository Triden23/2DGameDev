package Magic;

import entity.Entity;
import Magic.Spell;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FireBall extends Spell{
    Spell child;

    int childrenAmount = 40;
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
        isHoming = true;
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

        System.out.println(baseStats.projectileSizeX);
        projectileSprite[0] = gp.assetM.resizeAndGetAsset("SPE_PRO_FireBall1",baseStats.projectileSizeX,baseStats.projectileSizeY);
        projectileSprite[1] = gp.assetM.resizeAndGetAsset("SPE_PRO_FireBall2",baseStats.projectileSizeX,baseStats.projectileSizeY);
        
        effectSprite[0] = gp.assetM.resizeAndGetAsset("SPE_EFF_FireBall1",baseStats.effectSizeX,baseStats.effectSizeY);
        effectSprite[1] = gp.assetM.resizeAndGetAsset("SPE_EFF_FireBall2",baseStats.effectSizeX,baseStats.effectSizeY);
    }

    public void projectileLogic(){
        travelPathStraight();
        if(projectileFrameCounter == baseStats.range){
            trigger = true;
        }
    }

    public void effectLogic(){
        if(effectFrameCounter >= 60){
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
