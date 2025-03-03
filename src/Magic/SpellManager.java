package Magic;

import entity.Entity;
import main.GamePanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class SpellManager {

    public ArrayList<Spell> spells = new ArrayList<>();
    Spell[] spell;
    int randomPointDistanceMax = 30;
    Random rand;

    int randomTrackingCounter = 120;
    int randomTrackingLimit = 120;

    GamePanel gp;

    ArrayList<Entity> currentEntities;
    public SpellManager(GamePanel gp){
        this.gp = gp;
        rand = new Random();
    }


    //---------------------------------------------------------------------------------------------------------------
    //                  Used in casting/updated spells
    //---------------------------------------------------------------------------------------------------------------

    public void addSpell(Spell spell){
        //Check to see if it's a multicast maybe have a buffer of sorts to check - figure out logic for multi casting later - probably handle at entity level;
        spells.add(spell);
    }

    public Spell getSpell(String name,Entity caster){
        switch(name){
            case "fireball":
                return new FireBall(gp,caster);
            case "icespike":
                return null;
        }
        return null;
    }

    //---------------------------------------------------------------------------------------------------------------
    //                  Draw Related
    //---------------------------------------------------------------------------------------------------------------

    public void draw(Graphics2D g2){
        for(int i = 0; i < spells.size(); i++){
            if(spells.get(i)!=null){
                //System.out.println("Drawing " + spells.get(i).name);
                spells.get(i).draw(g2);
            }
        }
    }

    //---------------------------------------------------------------------------------------------------------------
    //                  Update related
    //---------------------------------------------------------------------------------------------------------------

    public void update(){//update spells, then check for collision -> applyDamage
        currentEntities = gp.getCurrentEntities();
        for(int i = 0; i < spells.size(); i++){
            if(spells.get(i)!=null){
                if(spells.get(i).done){
                    spells.remove(i);
                }else{
                    if(spells.get(i).isHoming){
                        homingUpdate(i);
                    }
                    spells.get(i).update();
                    checkForCollision();
                }
            }
        }
        currentEntities.clear();
    }

    public void checkForCollision(){
        ArrayList<Integer> entitiesHit = new ArrayList<>();

        for(int i = 0; i < spells.size(); i++){

            if(gp.cChecker.checkSpellPlayer(spells.get(i))){

                hit(999,i,true);//The only instance we pass true is if the player was hit

            }

            entitiesHit = gp.cChecker.checkSpellEntity(spells.get(i),gp.monster);

            if(!entitiesHit.isEmpty()){

                for(int index = 0; index < entitiesHit.size(); index++){

                    if(entitiesHit.get(index) == 999){

                        //Do nothing
                    }else if(!spells.get(i).hittags.contains(gp.monster[entitiesHit.get(index)].tag)) {

                        hit(i, entitiesHit.get(index), false);

                        //Add the entity's tag to an array list stored in the spell so it wont hit the same target more than once

                        spells.get(i).hittags.add(gp.monster[entitiesHit.get(index)].tag);

                    }else{

                        if(gp.monster[entitiesHit.get(index)] != null){

                            System.out.println(gp.monster[entitiesHit.get(index)].tag);

                        }
                    }
                }
            }
        }
    }

    //Used to apply damage for a spell, in the future will detect effects of a spell and apply them too
    public void hit(int index,int entityIndex,boolean isPlayer){
        if(!isPlayer) {
            if(entityIndex!=999){
                if(!gp.monster[entityIndex].invincible){
                    if (spells.get(index).baseStats.amountOfPierce > 0 && spells.get(index).canPeirce) {
                        spells.get(index).baseStats.amountOfPierce--;
                        applyPierceDamage(index, entityIndex);
                    }else {
                        if(!spells.get(index).trigger){
                            spells.get(index).trigger = true;
                        }else {
                            applyDamage(index, entityIndex);
                        }
                    }
                }
            }
        }else if(isPlayer){
            if(!gp.player.invincible){
                if (spells.get(index).baseStats.amountOfPierce > 0 && spells.get(index).canPeirce) {
                    spells.get(index).baseStats.amountOfPierce--;
                    applyPierceDamageToPlayer(index);
                }else {
                    if(!spells.get(index).trigger){
                        spells.get(index).trigger = true;
                    }else {
                        applyDamageToPlayer(index);
                    }
                }
            }
        }
    }

    //used to update the targetX and targetY of a spells coord if it is a homing spell
    public void homingUpdate(int index){
        Coord coord;
        //set a tag if its null then update tracking
        if(spells.get(index).coord.tag == null || spells.get(index).coord.tag.equals("999")){
            Coord newTarget = findNearestEntity(spells.get(index));
            spells.get(index).updateTracking(newTarget);
        }
        if(!spells.get(index).coord.tag.equals("999")){
            coord = trackByTag(spells.get(index));
        }else{
            coord = generateRandomPoint(spells.get(index));
        }

        spells.get(index).coord = coord;
    }

    //---------------------------------------------------------------------------------------------------------------
    //                  Damage related
    //---------------------------------------------------------------------------------------------------------------

    public void applyPierceDamageToPlayer(int index){
        String message = "You take " + spells.get(index).getDamage() + " From " + spells.get(index).name + " cast by " + spells.get(index).caster.name;
        gp.player.takeDamage(spells.get(index).getDamage(),message);
    }

    public void applyDamageToPlayer(int index){
        String message = "You take " + spells.get(index).getDamage() + " From " + spells.get(index).name + " cast by " + spells.get(index).caster.name;
        gp.player.takeDamage(spells.get(index).pierceDamage(),message);
    }

    public void applyPierceDamage(int index, int entityIndex){
        String message;
        if(gp.monster[entityIndex] != null){
            message = gp.monster[entityIndex].name + " Takes " + spells.get(index).pierceDamage() + " From " + spells.get(index).name;
            gp.monster[entityIndex].takeDamage(spells.get(index).pierceDamage(), message);
        }
    }

    public void applyDamage(int index, int entityIndex){
        String message;
        if(gp.monster[entityIndex] != null){
            message = gp.monster[entityIndex].name + " Takes " + spells.get(index).pierceDamage() + " From " + spells.get(index).name;
            gp.monster[entityIndex].takeDamage(spells.get(index).getDamage(),message);
        }
    }

    //---------------------------------------------------------------------------------------------------------------
    //                  Tracking related
    //---------------------------------------------------------------------------------------------------------------

    //used by some spells for tacking
    public Coord trackByTag(Spell spell){//used to track entities for spells and maybe can be used to set a lock, so it will target the entity closest to mouse as well
        Coord c;
        Entity e = getByTagEntities(spell.coord.tag);
        c = spell.coord;
        if(e!=null){
            c.targetX = e.worldX;
            c.targetY = e.worldY;
        }else{
            c.targetX = c.x + rand.nextInt(randomPointDistanceMax);
            c.targetY = c.y + rand.nextInt(randomPointDistanceMax);
        }
        return c;
    }

    //Gets the tag of the nearest entity
    public Coord findNearestEntity(Spell spell){
        Coord c;
        c = spell.coord;
        c.tag = getClosestEntityTag(spell.coord, spell);
        return c;
    }

    public String getClosestEntityTag(Coord coord,Spell spell) {
        Entity closestEntity = null;
        double closestDistance = Double.MAX_VALUE;

        for (Entity entity : currentEntities) {
            int dx = coord.x - entity.worldX;
            int dy = coord.y - entity.worldY;
            double distance = Math.sqrt(dx * dx + dy * dy); // Euclidean Distance
            if(!entity.tag.equals(spell.caster.tag)){
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestEntity = entity;
                }
            }
        }
        currentEntities.clear();
        if(closestEntity == null){
            return "999";
        }
        return closestEntity.tag;
    }

    public Entity getByTagEntities(String tag){
        Entity entity = null;
        for (Entity list : currentEntities){
            if(list.tag.equals(tag)){
                return list;
            }
        }
        currentEntities.clear();
        return entity;
    }

    //Gets the closest entity class
    public Entity getClosestEntity(Coord coord) {
        Entity closestEntity = null;
        double closestDistance = Double.MAX_VALUE;

        for (Entity entity : currentEntities) {
            int dx = coord.x - entity.worldX;
            int dy = coord.y - entity.worldY;
            double distance = Math.sqrt(dx * dx + dy * dy); // Euclidean Distance

            if (distance < closestDistance) {
                closestDistance = distance;
                closestEntity = entity;
            }
        }
        currentEntities.clear();

        return closestEntity;
    }

    public Coord generateRandomPoint(Spell spell){
        int x = 0;
        int y = 0;
        x = spell.coord.x; // 1-100 range
        y = spell.coord.y; //1-100 range
        if(randomTrackingCounter == randomTrackingLimit){

            if(x%2==0){
                x += rand.nextInt((500-300))+300;
            }else{
                x -= rand.nextInt((500-300))+300;
            }
            System.out.println("current target x: " + spell.coord.targetX + " new target x: " + x);
            if(y%2==0){
                y -= rand.nextInt((500-300))+300;
            }else{
                y += rand.nextInt((500-300))+300;
            }
            System.out.println("current target y: " + spell.coord.targetY + " new target y: " + x);
            System.out.println(y);
            randomTrackingCounter = 0;
        }
        randomTrackingCounter++;
        return new Coord(spell.coord.x,spell.coord.y,x,y);
    }

    //used to get the players coords(used in monster homing spells)
    public Coord getPlayerCoord(Spell spell){
        Coord c = spell.coord;
        c.targetX = gp.player.worldX;
        c.targetY = gp.player.worldY;
        return c;
    }


}
