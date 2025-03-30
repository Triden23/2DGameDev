package Magic;

import Assets.TagTracker;
import entity.Entity;
import main.GamePanel;
import tools.Circle;
import tools.Transform;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Projectile {

    public String tag;
    public Hitbox hitbox;
    public TravelType travelType;
    public TagTracker tagTracker;
    boolean isDone;
    Entity caster;
    SpellAttributes spellA;
    NewSpell spe;
    BufferedImage[] images;
    int frameCounter;
    int animationInterval;
    int currentSprite;
    int maxSprite;
    int xOffSet, yOffSet;
    int screenX, screenY;
    Rectangle rectHitBox;
    Circle circleHitBox;
    GamePanel gp;
    boolean homing;
    boolean complete;
    boolean hit;
    Transform origin, current, target;
    int distance, travelTime;

    public Projectile(GamePanel gp, Entity caster, NewSpell spe, SpellAttributes spellA) {
        this.spellA = spellA;
        this.gp = gp;
        this.caster = caster;
        this.spe = spe;


        fetchTransforms();
        System.out.println("Projectile Null check " + (this.current == null));
        setValues();
        setBehaviors();
        loadImages(gp);
        setUpHitBox();
    }

    public void fetchTransforms() {
        System.out.println("SPE Current check + " + (spe.current == null));
        System.out.println("SPE Null check + " + (spe == null));
        current = spe.current;
        target = spe.target;
        origin = spe.origin;
    }

    public void setBehaviors() {
    }

    public void setMaxSprite(int maxSprite) {
        this.maxSprite = maxSprite;
    }

    public void loadImages(GamePanel gp) {
        //Needs to be written by child
    }

    public void setHomingTarget(Entity target) {
        homing = true;
        this.target = target.location;
    }

    public void setUpHitBox() {
        //If the hitbox is a rectangle setup rectangle

        if (hitbox == Hitbox.RECTANGLE) {
            System.out.println(current.getWorldX());
            rectHitBox = new Rectangle(current.getWorldX(), current.getWorldY(), spellA.getProjectileSizeX(), spellA.getProjectileSizeY());
        }
        //If the hitbox is a circle setup circle
        if (hitbox == Hitbox.CIRCLE) {
            circleHitBox = new Circle(current.getWorldX(), current.getWorldY(), spellA.getProjectileSizeX());
            circleHitBox.radius = spellA.getProjectileSizeX();
        }

    }

    public void markDone() {
        isDone = true;
        origin.markDone();
        if (!homing) {
            target.markDone();
        }
    }

    public boolean getDone() {
        return isDone;
    }

    public void reUse(Entity caster, NewSpell spe, SpellAttributes spellA) {
        isDone = false;
        complete = false;
        hit = false;
        this.caster = caster;
        this.spellA = spellA;
        this.spe = spe;
        fetchTransforms();
        reSetValues();
    }

    public void update() {
        if (!isDone) {
            if (frameCounter % animationInterval == 0) {
                currentSprite++;
                if (currentSprite >= maxSprite) {
                    currentSprite = 0;
                }
            }
            frameCounter++;
            if (frameCounter > spellA.getRange()) {
                complete = true;
            }
            travel();
            if (hit) {
                if (spellA.getAmountOfPierce() < 0) {
                    complete = true;
                }
            }
            if (complete) {
                markDone();
            }

        }
    }

    public void applyDamage(Entity e, String name) {
        String msg = e.name + " took " + spellA.getBaseDamage() + " damage from " + name + " cast by " + caster.name;
        e.takeDamage(spellA.getBaseDamage(), msg);
    }

    public void travel() {
        switch (travelType) {
            case TravelType.LINE:
                travelLine();
                break;
            case TravelType.TANGENT_LINE:
                travelTangentLine();
                break;
        }
        updateHitBox();
    }

    public void updateHitBox() {
        if (hitbox == Hitbox.CIRCLE) {
            updateCircleHitBox();
        }
        if (hitbox == Hitbox.RECTANGLE) {
            updateRectHitBox();
        }
    }

    public void updateRectHitBox() {
        int centerX = current.getWorldX();
        int centerY = current.getWorldY();

        switch (spe.onCastDirection) {
            case "left":
                rectHitBox.x = centerX - spellA.getProjectileSizeX() / 2;
                rectHitBox.y = centerY - spellA.getProjectileSizeY() / 2;
                break;
            case "right":
                rectHitBox.x = centerX - spellA.getProjectileSizeX() / 2;
                rectHitBox.y = centerY - spellA.getProjectileSizeY() / 2;
                break;
            case "up":
                rectHitBox.x = centerX - spellA.getProjectileSizeX() / 2;
                rectHitBox.y = centerY - spellA.getProjectileSizeY() / 2;
                break;
            case "down":
                rectHitBox.x = centerX - spellA.getProjectileSizeX() / 2;
                rectHitBox.y = centerY - spellA.getProjectileSizeY() / 2;
                break;
        }
    }

    public void updateCircleHitBox() {
        circleHitBox.x = current.getWorldX();
        circleHitBox.y = current.getWorldY();

        circleHitBox.x -= circleHitBox.radius;
        circleHitBox.y -= circleHitBox.radius;
    }

    public void travelLine() {
        distance = gp.tool.distanceCalc(current, target);
        int dx = target.getWorldX() - current.getWorldX();
        int dy = target.getWorldY() - current.getWorldY();

        // Normalize direction and move
        int moveX = (dx == 0) ? 0 : (dx / Math.abs(dx)) * Math.min(spellA.getProjectileSpeed(), Math.abs(dx));
        int moveY = (dy == 0) ? 0 : (dy / Math.abs(dy)) * Math.min(spellA.getProjectileSpeed(), Math.abs(dy));

        current.addWorldX(moveX);
        current.addWorldY(moveY);
        if (distance <= 0) {
            hit = true; // Target reached
        }
    }

    public void travelTangentLine() {
        //Calculate the direction
        int deltaX = target.getWorldX() - current.getWorldX();
        int deltaY = target.getWorldY() - current.getWorldY();

        int magnitude = gp.tool.calculateMagnitude(deltaX, deltaY);

        if (magnitude >= spellA.getProjectileSpeed()) {
            //Normalize the direction and scale by speed
            int velocityX = (int) ((deltaX / (double) magnitude) * spellA.getProjectileSpeed());
            int velocityY = (int) ((deltaY / (double) magnitude) * spellA.getProjectileSpeed());

            //update the position
            current.addWorldX(velocityX);
            current.addWorldY(velocityY);
        } else {
            //break condition
            hit = true;
        }
    }

    public void setValues() {
        animationInterval = 0;
        frameCounter = 0;
        currentSprite = 0;
        isDone = false;
        complete = false;
    }

    public void reSetValues() {
        frameCounter = 0;
        currentSprite = 0;
    }

    private int getCurrentHitBoxX() {
        if (hitbox == Hitbox.RECTANGLE) return rectHitBox.x;
        if (hitbox == Hitbox.CIRCLE) return circleHitBox.x;
        return 0;
    }

    private int getCurrentHitBoxY() {
        if (hitbox == Hitbox.RECTANGLE) return rectHitBox.y;
        if (hitbox == Hitbox.CIRCLE) return circleHitBox.y;
        return 0;
    }

    public void draw(Graphics2D g2) {
        screenX = getCurrentHitBoxX() - gp.player.worldX + gp.player.screenX;
        screenY = getCurrentHitBoxY() - gp.player.worldY + gp.player.screenY;

        if (!isDone) {
            g2.drawImage(images[currentSprite], screenX, screenY, spellA.getProjectileSizeX(), spellA.getProjectileSizeY(), null);

            // Convert hitbox position to screen coordinates
            int hitboxScreenX = rectHitBox.x - gp.player.worldX + gp.player.screenX;
            int hitboxScreenY = rectHitBox.y - gp.player.worldY + gp.player.screenY;

            g2.setColor(Color.blue);
            g2.drawRect(hitboxScreenX, hitboxScreenY, rectHitBox.width, rectHitBox.height);
        }
    }

    public void PrintDebug() {
        System.out.println("Spell frame data ****************");
        System.out.println("Frame counter - " + frameCounter);
        System.out.println("Current Sprite - " + currentSprite);
        System.out.println("Hit State - " + hit);
        System.out.println("Is Done - " + isDone);
    }

    public enum Hitbox {RECTANGLE, CIRCLE}

    public enum TravelType {LINE, TANGENT_LINE}


}
