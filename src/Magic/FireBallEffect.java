package Magic;

import entity.Entity;
import main.GamePanel;

import java.awt.image.BufferedImage;

public class FireBallEffect extends Effect {
    public FireBallEffect(GamePanel gp, Entity caster, NewSpell spe, SpellAttributes spellA) {
        super(gp, caster, spe, spellA);
        System.out.println(current.getWorldX());
    }

    public void setBehaviors() {
        hitbox = Hitbox.RECTANGLE;
    }

    public void setValues() {
        animationInterval = 10;
        frameCounter = 0;
        currentSprite = 0;
        maxSprite = 2;
        images = new BufferedImage[maxSprite];
        isDone = false;
        complete = false;
    }

    public void loadImages(GamePanel gp) {
        images[0] = gp.assetM.resizeAndGetAsset("SPE_PRO_FireBall1", spellA.getEffectSizeX(), spellA.getProjectileSizeY());
        images[1] = gp.assetM.resizeAndGetAsset("SPE_PRO_FireBall2", spellA.getEffectSizeX(), spellA.getProjectileSizeY());
    }


}
