package Magic;

import entity.Entity;
import main.GamePanel;

import java.awt.image.BufferedImage;

public class FireBallProjectile extends Projectile {
    public FireBallProjectile(GamePanel gp, Entity caster, NewSpell spe, SpellAttributes spellA) {
        super(gp, caster, spe, spellA);
        tag = "PRO_Fireball";
        System.out.println(current.getWorldX());
    }

    public void setBehaviors() {
        homing = false;
        travelType = TravelType.LINE;
        hitbox = Hitbox.RECTANGLE;
    }

    public void setValues() {
        animationInterval = 10;
        frameCounter = 0;
        currentSprite = 0;
        setMaxSprite(2);
        images = new BufferedImage[maxSprite];
        isDone = false;
        complete = false;
    }

    public void loadImages(GamePanel gp) {

        images[0] = gp.assetM.resizeAndGetAsset("SPE_PRO_FireBall1", spellA.getProjectileSizeX(), spellA.getProjectileSizeY());
        images[1] = gp.assetM.resizeAndGetAsset("SPE_PRO_FireBall2", spellA.getProjectileSizeX(), spellA.getProjectileSizeY());

    }

    public void projectileHitLogic() {

    }


}
