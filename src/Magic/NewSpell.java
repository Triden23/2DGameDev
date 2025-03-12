package Magic;

import entity.Entity;
import main.GamePanel;
import tools.Transform;

public class NewSpell {
    String tag;

    SpellAttributes attributes;

    public String state = "Projectile";

    Projectile projectile;
    Effect effect;

    boolean isDone;

    public NewSpell(GamePanel gp, Entity caster){
        isDone = false;
    }
    public void setAttributes(SpellAttributes attributes){this.attributes = attributes;}
    public void setProjectile(Projectile projectile){this.projectile = projectile;}
    public void setEffect(Effect effect){this.effect = effect;}
    public void setTag(String tag){this.tag = tag;};
    public void markDone(){
        isDone = true;
        effect.markDone();
        projectile.markDone();
    }
    public void reUse(Projectile projectile,Effect effect, Entity caster){
        isDone = false;
        this.projectile = projectile;
        this.effect = effect;
    }
    public void update(){
        if(effect.isDone && projectile.isDone && !isDone){ markDone(); }
        if(!isDone){
            if(!projectile.isDone){
                projectile.update();
            }else{
                effect.update();
            }
        }
    }

}
