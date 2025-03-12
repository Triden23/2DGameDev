package Magic;

import entity.Entity;
import main.GamePanel;
import tools.Circle;
import tools.Transform;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Projectile {

    String tag;
    boolean isDone;
    Entity caster;
    SpellAttributes spellA;

    BufferedImage[] images;
    int frameCounter;
    int animationInterval;
    int currentSprite;

    int screenX,screenY;

    public String hitBoxType = "rect";
    public String travelType = "Straight";

    Rectangle rectHitBox;
    Circle circleHitBox;

    GamePanel gp;

    boolean homing;
    boolean complete;
    boolean hit;

    Transform origin,current,target;
    int distance,travelTime;

    public Projectile(GamePanel gp, Entity caster, SpellAttributes spellA){
        this.spellA = spellA;
        this.gp = gp;
        this.caster = caster;
        setValues();
        setBehaviors();
        loadImages(gp);
        setUpHitBox();
    }

    public void setBehaviors(){
        homing = false;
    }

    public void setUpTransforms(){
        setUpOrigin();
        setUpCurrent();
        if(!homing){
            setUpTarget();
        }else{
            //Call a method to find the nearest applicable target in a spell manager(Searches for the nearest player or monster or pet)
        }

    }

    public void setUpOrigin(){
        int xOffset,yOffset;
        xOffset = caster.location.getWorldX();
        yOffset = caster.location.getWorldY();
        switch(caster.direction){
            case "up":
                yOffset-=(spellA.getProjectileSizeY()/2);
                break;
            case "down":
                yOffset+=(spellA.getProjectileSizeY()/2);
                break;
            case "left":
                xOffset-=(spellA.getEffectSizeX()/2);
                break;
            case "right":
                xOffset+=(spellA.getEffectSizeX()/2);
        }
        origin = gp.objectP.getTransform(xOffset,yOffset);
    }

    public void setUpCurrent(){
        origin = gp.objectP.getTransform(origin.getWorldX(), origin.getWorldY());
    }

    public void setUpTarget(){
        int xOffset,yOffset,distance;
        xOffset = origin.getWorldX();
        yOffset = origin.getWorldY();
        distance = spellA.getRange()*gp.tileSize;
        switch(caster.direction){
            case "up":
                yOffset-=distance;
                break;
            case "down":
                yOffset+=distance;
                break;
            case "left":
                xOffset-=distance;
                break;
            case "right":
                xOffset+=distance;
        }
        target = gp.objectP.getTransform(xOffset,yOffset);
    }

    public void loadImages(GamePanel gp){
        //Needs to be written by child
    }

    public void setHomingTarget(Entity target){
        this.target = target.location;
    }

    public void setUpHitBox(){
        switch(hitBoxType){
            case "Rect":
                rectHitBox = new Rectangle(current.getWorldX(),current.getWorldY(),spellA.getProjectileSizeX(),spellA.getProjectileSizeY());
                break;
            case "Circle":
                circleHitBox = new Circle(current.getWorldX(),current.getWorldY(),spellA.getProjectileSizeX());
                circleHitBox.radius = spellA.getProjectileSizeX();
                break;
        }


    }

    public void markDone(){
        isDone = true;
        origin.markDone();
        if(!homing){
            target.markDone();
        }
    }

    public boolean getDone(){
        return isDone;
    }

    public void reUse(Entity caster,SpellAttributes spellA){
        this.caster = caster;
        this.spellA = spellA;
        reSetValues();
    }

    public void update(){
        if(!isDone){
            if(frameCounter%animationInterval==0){
                currentSprite++;
            }
            frameCounter++;
            if(hit){
                projectilHitLogic();
            }else{
                travel();
            }
            if(complete){
                markDone();
            }
        }
    }

    private void projectilHitLogic() {
        //Does nothing in base class, this will handle once its done its thing use complete to mark it done to begin markdone Process
    }

    public void travel(){

        switch(travelType){
            case "Straight":
                travelStraight();
                break;
        }
        updateHitBox();
    }

    public void updateHitBox(){
        switch(hitBoxType){
            case "Rect":
                updateRectHitBox();
                break;
            case "Circle":
                updateCircleHitBox();
                break;
        }
    }

    public void updateRectHitBox(){
        rectHitBox.x = current.getWorldX() - (spellA.getProjectileSizeX()/2);
        rectHitBox.y = current.getWorldY() - (spellA.getProjectileSizeY()/2);
    }

    public void updateCircleHitBox(){
        circleHitBox.x = current.getWorldX();
        circleHitBox.y = current.getWorldY();
    }

    public void travelStraight(){
        distance = gp.tool.distanceCalc(current,target);
        int dx = target.getWorldX() - current.getWorldX();
        int dy = target.getWorldY() - current.getWorldY();

        // Normalize direction and move
        int moveX = (dx == 0) ? 0 : (dx / Math.abs(dx)) * Math.min(spellA.getProjectileSpeed(), Math.abs(dx));
        int moveY = (dy == 0) ? 0 : (dy / Math.abs(dy)) * Math.min(spellA.getProjectileSpeed(), Math.abs(dy));

        current.addWorldX(moveX);
        current.addWorldY(moveY);
        if (distance == 0) {
            hit = true; // Target reached
        }
    }

    public void setValues(){
        animationInterval = 0;
        frameCounter = 0;
        currentSprite = 0;
        isDone = false;
        complete = false;
    }

    public void reSetValues(){
        frameCounter = 0;
        currentSprite = 0;
        setUpTransforms();
    }

    public void draw(Graphics2D g2){

        if(!isDone){

        }

    }
}
