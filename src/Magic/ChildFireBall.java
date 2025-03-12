package Magic;

import entity.Entity;
import main.GamePanel;

import java.awt.image.BufferedImage;

public class ChildFireBall extends Spell{


    public ChildFireBall(GamePanel gp, Entity caster, Coord coord){
        super(gp,false,caster);
        this.coord = coord;
        //Boom done easy peezy will have alot of work to do.
    }

    public void setVariables(){
        //Attributes
        name = "fireball"; //name of the spell
        currentHitBox = "projectile"; //How it handles logic
        type = "basic"; //Damage type
        childrenAmount = 5;
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

        baseStats.projectileSizeX = 15;
        baseStats.projectileSizeY = 15;



        baseStats.effectSizeX = 40;
        baseStats.effectSizeY = 40;

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
        if(effectFrameCounter >= 20){
            //do things/spawn children
            done = true;
        }else{
            //Effect logic

        }
    }


}
