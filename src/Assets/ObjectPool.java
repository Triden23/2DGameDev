package Assets;

import Magic.*;
import entity.Entity;
import entity.NPC_Oldman;
import main.GamePanel;
import monster.MON_BlueSlime;
import object.*;
import tools.Transform;

import java.util.LinkedList;

public class ObjectPool {
    private final LinkedList<Transform> TransformPool;
    private final LinkedList<Effect> effectPool;
    private final LinkedList<Projectile> projectilePool;
    private final LinkedList<Entity> entityPool;
    private final LinkedList<NewSpell> spellPool;
    private final LinkedList<Entity> objectPool;

    GamePanel gp;
    // You can add more pools for other types here (e.g., Entity, Projectile, etc.)
    // private final LinkedList<Entity> entityPool;

    // Constructor to initialize the pools
    public ObjectPool(GamePanel gp) {
        this.gp = gp;
        TransformPool = new LinkedList<>();
        effectPool = new LinkedList<>();
        projectilePool = new LinkedList<>();
        entityPool = new LinkedList<>();
        spellPool = new LinkedList<>();
        objectPool = new LinkedList<>();
        // Initialize other pools if needed:
        // entityPool = new LinkedList<>();
    }

    // Get a Transform object from the pool (reused or newly created)
    public Transform getTransform(int x, int y) {
        Transform transform = null;
        System.out.println("X: " + x + " Y: " + y);
        // Check if there's a reusable Transform object
        for (Transform t : TransformPool) {
            if (t.getDone()) {
                System.out.println("Found a re-usable one");
                transform = t;  // Found one that is done, so we can reuse it
                break;
            }
        }

        if (transform == null) {
            // If no reusable object is found, create a new one
            System.out.println("Creating a new one");
            transform = new Transform(x, y);
            TransformPool.add(transform);  // Add the new object to the pool
        } else {
            // Reinitialize the reusable object with new data
            System.out.println("Re-using it");
            transform.reUse(x, y);
        }
        System.out.println("Returning transform state- Null Check: " + (transform == null) + " Current X: " + transform.getWorldX() + " Current Y: " + transform.getWorldY());
        return transform;
    }

    public Effect getEffect(TagTracker t, Entity caster, NewSpell spe, SpellAttributes spellA) {
        Effect effect = null;
        for (Effect e : effectPool) {
            if (e.getDone() && e.tagTracker == t) {
                effect = e;
            }
        }

        if (effect == null) {
            switch (t) {
                case TagTracker.EFF_FireBall:
                    System.out.println("selected " + t);
                    effect = new FireBallEffect(gp, caster, spe, spellA);
                    break;
            }

            effectPool.add(effect);
        } else {
            effect.reUse(caster, spe, spellA);
        }

        return effect;
    }

    public Projectile getProjectile(TagTracker t, Entity caster, NewSpell spe, SpellAttributes spellA) {
        Projectile projectile = null;

        for (Projectile p : projectilePool) {
            if (p.getDone() && p.tagTracker == t) {
                projectile = p;
            }
        }

        if (projectile == null) {
            switch (t) {
                case TagTracker.PRO_FireBall:
                    System.out.println("selected " + t);
                    projectile = new FireBallProjectile(gp, caster, spe, spellA);
                    break;
            }
            projectilePool.add(projectile);
        } else {
            projectile.reUse(caster, spe, spellA);
        }
        System.out.println("Null check" + projectile != null);
        return projectile;
    }

    public void addEntity(TagTracker t, int health, int x, int y) {
        Entity entity = null;

        for (Entity e : entityPool) {
            if (e.getDone() && e.tag.equals(t.getTag())) {
                entity = e;
            }
        }
        if (entity == null) {
            switch (t) {
                case TagTracker.MON_BlueSlime:
                    entity = new MON_BlueSlime(gp);
                    break;
                case TagTracker.NPC_OldMan:
                    entity = new NPC_Oldman(gp);

            }
            entity.requestTransform(x * gp.tileSize, y * gp.tileSize);
            entity.maxLife = health;
            entity.life = health;
            gp.chunkM.addEntity(entity);
            gp.chunkM.updateEntityChunk(entity);
            entityPool.add(entity);
        } else {
            entity.maxLife = health;
            entity.life = health;
            entity.reUse(x * gp.tileSize, y * gp.tileSize);
        }
    }

    public void addSpell(TagTracker t, Entity caster) {
        NewSpell spell = null;
        for (NewSpell s : spellPool) {
            if (s.getDone() && s.tagTracker == t) {
                spell = s;
            }
        }
        if (spell == null) {
            switch (t) {
                case TagTracker.SPE_FireBall:
                    System.out.println("selected " + t);
                    spell = new newFireBall(gp, caster);
                    break;
                default:
                    //TODO make default spell that fizzles immediately
                    System.out.println("default selected");
                    spell = new newFireBall(gp, caster);
            }

            spellPool.add(spell);
        } else {
            spell.reUse(caster);
        }
        caster.setCooldown(spell.spellA.getCooldown());
        gp.chunkM.addSpell(spell);
    }

    public void addObject(TagTracker t, int x, int y) {
        Entity object = null;
        for (Entity o : objectPool) {
            if (o.getDone() && o.tagTracker == t) {
                object = o;
                object.reUse(x, y);
            }
        }
        if (object == null) {
            switch (t) {
                case TagTracker.OBJ_Chest:
                    object = new OBJ_Chest(gp);
                    break;
                case TagTracker.OBJ_Boots:
                    object = new OBJ_Boots(gp);
                    break;
                case TagTracker.OBJ_Shield_Wood:
                    object = new OBJ_Shield_Wood(gp);
                    break;
                case TagTracker.OBJ_Door:
                    object = new OBJ_Door(gp);
                    break;
                case TagTracker.OBJ_Heart:
                    object = new OBJ_Heart(gp);
                    break;
                case TagTracker.OBJ_Key:
                    object = new OBJ_Key(gp);
                    break;
                case TagTracker.OBJ_Sword_Normal:
                    object = new OBJ_Sword_Normal(gp);
                    break;
                default:
                    object = new OBJ_Key(gp);
                    break;
            }
            object.requestTransform(x, y);
            objectPool.add(object);
        }
        gp.chunkM.updateEntityChunk(object);

    }


}
