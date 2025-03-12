package Magic;

import entity.Entity;
import main.GamePanel;
import tools.Circle;
import tools.Transform;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Effect {
    String tag;
    boolean isDone;

    Entity caster;
    SpellAttributes spellA;

    BufferedImage[] images;
    int frameCounter;
    int animationInterval;
    int currentSprite;
    int spriteMaxCounter;

    int screenX,screenY;

    public String hitBoxType = "rect";
    public String travelType = "Straight";

    Rectangle rectHitBox;
    Circle circleHitBox;

    GamePanel gp;

    boolean complete;

    Transform current;

    public Effect(GamePanel gp, Entity caster, SpellAttributes spellA, Transform current){
        isDone = false;
        this.gp = gp;
        this.caster = caster;
        this.current = current;
        setUpHitBox();
    }

    public void updateCurrent(Transform current){
        this.current = current;
    }

    public void setUpHitBox(){
        switch(hitBoxType){
            case "Rect":
                rectHitBox = new Rectangle(current.getWorldX(),current.getWorldY(),spellA.getEffectSizeX(),spellA.getEffectSizeY());
                break;
            case "Circle":
                circleHitBox = new Circle(current.getWorldX(),current.getWorldY(),spellA.getEffectSizeX());
                circleHitBox.radius = spellA.getEffectSizeX();
                break;
        }
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
        rectHitBox.x = current.getWorldX() - (spellA.getEffectSizeX()/2);
        rectHitBox.y = current.getWorldY() - (spellA.getEffectSizeX()/2);
    }

    public void updateCircleHitBox(){
        circleHitBox.x = current.getWorldX();
        circleHitBox.y = current.getWorldY();
    }


    public void getCurrent(Transform current){
        this.current = current;
    }

    public boolean getDone(){
        return isDone;
    }

    public void markDone(){
        isDone = true;
        current.markDone();
        frameCounter = 0;
        currentSprite = 0;
    }

    public void reUse(Entity caster, SpellAttributes spellA, Transform current){
        this.caster = caster;
        this.spellA = spellA;
        this.current = current;
    }

    public void effectLogic(){
        //Do per effect child
    }

    public void update(){
        if(!isDone){
            if(frameCounter%animationInterval==0){
                currentSprite++;
            }
            frameCounter++;
            effectLogic();
            if(complete){
                markDone();
            }
        }
    }

    public void draw(Graphics2D g2){

    }


}
