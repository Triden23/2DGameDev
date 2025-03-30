package Magic;

import Assets.TagTracker;
import entity.Entity;
import main.GamePanel;
import tools.Transform;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NewSpell {
    public Entity caster;
    public String tag;
    public String onCastDirection = "Up";
    public String name;
    public Boolean updated = false;
    public Boolean drawn = false;
    public Boolean locked = false;
    public TagTracker tagTracker;
    public TagTracker projectileTag;
    public TagTracker effectTag;
    public SpellAttributes spellA;
    public int currentChunkX, currentChunkY;
    public State state;
    public Transform origin, current, target;
    public boolean isDone;
    int hitamount = 0;
    GamePanel gp;
    ArrayList<Entity> affectedEntity = new ArrayList<>();
    Projectile projectile;
    Effect effect;
    boolean homing = false;

    public NewSpell(GamePanel gp, Entity caster) {
        isDone = false;
        this.gp = gp;
        this.caster = caster;

        setUpAttributes();
        setUpTransforms();
        requestParts();

    }

    public void setUpAttributes() {
    }//OVERRIDE IN CHILD

    public void setUpTransforms() {
        onCastDirection = caster.direction;
        setUpOrigin();
        setUpCurrent();
        if (!homing) {
            setUpTarget();
        } else {
            findClosestTarget();
        }
    }

    public void requestParts() {

    }

    public void setUpOrigin() {
        int xOffset, yOffset;

        // Get the center of the entity's hitbox
        xOffset = caster.location.getWorldX() + caster.solidArea.x + (caster.solidArea.width / 2);
        yOffset = caster.location.getWorldY() + caster.solidArea.y + (caster.solidArea.height / 2);

        // Adjust for direction and move it just outside the entity's hitbox
        switch (onCastDirection) {
            case "up":
                yOffset -= (caster.solidArea.height / 2) + (spellA.getProjectileSizeY() / 2);
                break;
            case "down":
                yOffset += (caster.solidArea.height / 2) + (spellA.getProjectileSizeY() / 2);
                break;
            case "left":
                xOffset -= (caster.solidArea.width / 2) + (spellA.getProjectileSizeX() / 2);
                break;
            case "right":
                xOffset += (caster.solidArea.width / 2) + (spellA.getProjectileSizeX() / 2);
                break;
        }

        System.out.println("Projectile Origin: " + xOffset + ", " + yOffset);
        origin = gp.objectP.getTransform(xOffset, yOffset);
        currentChunkX = gp.chunkM.getChunkLocationValue(origin.getWorldX());
        currentChunkY = gp.chunkM.getChunkLocationValue(origin.getWorldY());
    }

    public void setUpCurrent() {
        current = gp.objectP.getTransform(origin.getWorldX(), origin.getWorldY());
    }

    public void setUpTarget() {
        int xOffset, yOffset, distance;
        xOffset = origin.getWorldX();
        yOffset = origin.getWorldY();
        distance = spellA.getRange();
        switch (caster.direction) {
            case "up":
                yOffset -= distance;
                break;
            case "down":
                yOffset += distance;
                break;
            case "left":
                xOffset -= distance;
                break;
            case "right":
                xOffset += distance;
        }
        target = gp.objectP.getTransform(xOffset, yOffset);
    }

    public void requestProjectile(TagTracker t) {
        projectile = gp.objectP.getProjectile(t, caster, this, spellA);
        projectileTag = projectile.tagTracker;
    }

    public void requestEffect(TagTracker t) {
        effect = gp.objectP.getEffect(t, caster, this, spellA);
    }

    public Transform getCurrent() {
        return current;
    }

    public void reUse(Entity caster) {
        isDone = false;
        state = State.PROJECTILE;
        setUpTransforms();
        setUpAttributes();
        projectile = gp.objectP.getProjectile(projectileTag, caster, this, spellA);
        effect = gp.objectP.getEffect(effectTag, caster, this, spellA);
    }

    public void update() {
        hitamount = spellA.getAmountOfPierce();
        if (projectile.isDone && state != State.EFFECT) {
            state = State.EFFECT;
        }
        if (state == State.PROJECTILE) projectile.update();
        if (state == State.EFFECT) effect.update();
        hitDetection();
        if (!affectedEntity.isEmpty() && state == State.PROJECTILE) {
            System.out.println(spellA.getAmountOfPierce() + " peirce amount");
            spellA.setAmountOfPierce(spellA.getAmountOfPierce() - affectedEntity.size());
            System.out.println(spellA.getAmountOfPierce() + " peirce amount after");
        }
        applyDamage();
        if (homing && !locked && projectile.frameCounter % 10 == 0) {
            findClosestTarget();
        }
        if (projectile.isDone && effect.isDone && !isDone) {
            System.out.println("Marking done");
            markDone();
        }

    }

    public void draw(Graphics2D g2) {
        if (state == State.PROJECTILE) projectile.draw(g2);
        if (state == State.EFFECT) effect.draw(g2);
    }

    public void hitDetection() {
        if (state == State.EFFECT) {
            if (effect.hitbox == Effect.Hitbox.RECTANGLE) {
                gp.collisionManager.checkSpellRectangle(this, effect.rectHitBox);
            } else {
                gp.collisionManager.checkSpellCircle(this, effect.circleHitBox);
            }
        } else {
            if (projectile.hitbox == Projectile.Hitbox.RECTANGLE) {
                gp.collisionManager.checkSpellRectangle(this, projectile.rectHitBox);
            } else {
                gp.collisionManager.checkSpellCircle(this, projectile.circleHitBox);
            }
        }
    }

    public void applyDamage() {
        if (!affectedEntity.isEmpty()) {
            for (Entity e : affectedEntity) {
                if (state == State.EFFECT) {
                    effect.applyDamage(e, name);
                } else {
                    if(hitamount>0) {
                        projectile.applyDamage(e, name);
                        hitamount--;
                    }else{
                        projectile.complete = true;
                    }
                }
            }
            clearAffected();
        }
    }

    public void addAffected(Entity e) {
        affectedEntity.add(e);
    }

    private void clearAffected() {
        affectedEntity.clear();
    }

    public void markDone() {
        //set own done to true
        isDone = true;
        //mark transforms for re-use
        if (!homing) target.markDone();
        origin.markDone();
        current.markDone();

    }

    public boolean getDone() {
        return isDone;
    }

    public void findClosestTarget() {
        List<Entity> entitiesInSurroundingChunks = gp.chunkM.getThreeByThree(currentChunkX, currentChunkY);
        Entity closest = null;
        int minDistance = Integer.MAX_VALUE;

        for (Entity e : entitiesInSurroundingChunks) {
            if (e != caster && e.type != caster.type && e.type != 1 && e.type != 3) {
                int checkDist = gp.tool.distanceCalc(e.location, current);
                if (checkDist < minDistance) {
                    minDistance = checkDist;
                    closest = e;
                }
            }
        }

        //set target to the closest one, if none are found, use default flight, maybe check every 10 frames or so to see.
        if (closest == null) {
            setUpTarget();
        } else {
            locked = true;
            target = closest.location;
        }
    }

    public void PrintDebug() {


        if (isDone) {
            System.out.println("This Should not be printing");
        } else {
            System.out.println("Name: " + name);
            System.out.println("X: " + current.getWorldX() + " Y: " + current.getWorldY());
            System.out.println("State: " + state);
            if (state == State.PROJECTILE) {
                System.out.println("Projectile info");
                projectile.PrintDebug();
            }
            if (state == State.EFFECT) {
                System.out.println("Effect info");
                effect.PrintDebug();
            }
        }
    }

    public enum State {PROJECTILE, EFFECT}

}
