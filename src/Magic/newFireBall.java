package Magic;

import Assets.TagTracker;
import entity.Entity;
import main.GamePanel;

public class newFireBall extends NewSpell {

    public newFireBall(GamePanel gp, Entity caster) {
        super(gp, caster);
        name = "Fire Ball";//different use case
        tag = "Fireball";
        state = State.PROJECTILE;
        tagTracker = TagTracker.SPE_FireBall;
        System.out.println(caster.location.getWorldX() + " " + caster.location.getWorldY());
        System.out.println(current.getWorldX() + " " + current.getWorldY());
        if (current == null) {
            System.out.println("Requesting another current");
            setUpCurrent();
        }
    }

    public void requestParts() {
        requestProjectile(TagTracker.PRO_FireBall);
        requestEffect(TagTracker.EFF_FireBall);
    }

    public void setUpAttributes() {
        homing = false;

        spellA = new SpellAttributes();
        spellA.setBaseDamage(5);
        spellA.setCooldown(60);
        spellA.setDuration(30);
        spellA.setProjectileSizeX(gp.tileSize / 2);
        spellA.setProjectileSizeY(gp.tileSize / 2);
        spellA.setEffectSizeX(gp.tileSize * 3);
        spellA.setEffectSizeY(gp.tileSize * 3);
        spellA.setProjectileSpeed(1);
        spellA.setRange(5 * gp.fps);
    }




}
