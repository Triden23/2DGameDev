package Magic;

import Assets.TagTracker;
import entity.Entity;
import main.GamePanel;
import tools.Circle;
import tools.Transform;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Effect {
    public String tag;
    public Hitbox hitbox;
    public TagTracker tagTracker;
    boolean isDone;
    NewSpell spe;
    Entity caster;
    SpellAttributes spellA;
    BufferedImage[] images;
    int frameCounter;
    int animationInterval;
    int currentSprite;
    int maxSprite;
    int screenX, screenY;
    //location adjustments
    boolean setOffSet = false;
    Rectangle rectHitBox;
    Circle circleHitBox;
    GamePanel gp;
    boolean complete;
    Transform current;

    public Effect(GamePanel gp, Entity caster, NewSpell spe, SpellAttributes spellA) {
        isDone = false;
        this.gp = gp;
        this.caster = caster;
        this.spellA = spellA;
        this.spe = spe;
        fetchTranforms();
        setValues();
        setBehaviors();
        loadImages(gp);
        setUpHitBox();
    }

    private void fetchTranforms() {
        current = spe.current;
    }

    public void setValues() {
    }

    public void setBehaviors() {
        hitbox = Hitbox.RECTANGLE;
    }

    public void setUpHitBox() {
        switch (hitbox) {
            case Hitbox.RECTANGLE:
                rectHitBox = new Rectangle(current.getWorldX(), current.getWorldY(), spellA.getEffectSizeX(), spellA.getEffectSizeY());
                break;
            case Hitbox.CIRCLE:
                circleHitBox = new Circle(current.getWorldX(), current.getWorldY(), spellA.getEffectSizeX());
                circleHitBox.radius = spellA.getEffectSizeX();
                break;
        }
    }

    //Override in child
    public void loadImages(GamePanel gp) {
    }//Override in child

    public void effectLogic() {
        //Do per effect child
    }//Setup in child

    public void updateHitBox() {
        switch (hitbox) {
            case Hitbox.RECTANGLE:
                updateRectHitBox();
                break;
            case Hitbox.CIRCLE:
                updateCircleHitBox();
                break;
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
        circleHitBox.x = current.getWorldX() - circleHitBox.radius;
        circleHitBox.y = current.getWorldY() - circleHitBox.radius;
    }

    public boolean getDone() {
        return isDone;
    }

    public void markDone() {
        isDone = true;
        current.markDone();
        frameCounter = 0;
        currentSprite = 0;
    }

    public void reUse(Entity caster, NewSpell spe, SpellAttributes spellA) {
        isDone = false;
        complete = false;
        this.caster = caster;
        this.spellA = spellA;
        this.spe = spe;
        fetchTranforms();
        setValues();
        setBehaviors();
    }

    public void update() {

        if (!isDone) {
            if (frameCounter % animationInterval == 0) {
                currentSprite++;
            }
            if (currentSprite >= maxSprite) currentSprite = 0;
            frameCounter++;
            effectLogic();
            updateHitBox();
            if (frameCounter > spellA.getDuration()) {
                complete = true;
            }
            if (complete) {
                markDone();
            }
        }
    }

    private int getCurrentHitBoxX() {
        if (hitbox == Hitbox.RECTANGLE) return rectHitBox.x;
        if (hitbox == Hitbox.CIRCLE) return circleHitBox.x;
        return 0;
    }

    public void applyDamage(Entity e, String name) {
        String msg = e.name + " took " + spellA.getBaseDamage() + " damage from " + name + " cast by " + caster.name;
        e.takeDamage(spellA.getBaseDamage(), msg);
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

    }

    public enum Hitbox {RECTANGLE, CIRCLE}

}
