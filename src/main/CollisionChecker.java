package main;

import Magic.Spell;
import entity.Entity;

import java.util.ArrayList;


public class CollisionChecker {
    GamePanel gp;
    public CollisionChecker(GamePanel gp){

        this.gp = gp;

    }

    public void checkTile(Entity entity){

        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX/gp.tileSize;
        int entityRightCol = entityRightWorldX/gp.tileSize;
        int entityTopRow = entityTopWorldY/gp.tileSize;
        int entityBottomRow = entityBottomWorldY/gp.tileSize;

        int tileNum1, tileNum2;

        switch(entity.direction){
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    //player is hitting a solid tile
                    entity.collisionOn = true;
                }
                break;

            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    //player is hitting a solid tile
                    entity.collisionOn = true;
                }
                break;

            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    //player is hitting a solid tile
                    entity.collisionOn = true;
                }
                break;

            case "right":
                entityRightCol = (entityRightWorldX + entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    //player is hitting a solid tile
                    entity.collisionOn = true;
                }
                break;

        }

    }

    public int checkObject(Entity entity, boolean player){
        /*uses different collision detection because there will be a
         smaller amount of objects than tiles. can be reworked should this change */

        int index = 999;//default return

        for(int i = 0; i < gp.obj.length; i++){
            if(gp.obj[i] != null) {

                // Get entity's solid area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;


                // Get object's solid area position
                gp.obj[i].solidArea.x = gp.obj[i].worldX + gp.obj[i].solidArea.x;
                gp.obj[i].solidArea.y = gp.obj[i].worldY + gp.obj[i].solidArea.y;

                //Direction of movement
                switch (entity.direction) {
                    case "up":      entity.solidArea.y -= entity.speed; break;
                    case "down":    entity.solidArea.y += entity.speed; break;
                    case "left":    entity.solidArea.x -= entity.speed; break;
                    case "right":   entity.solidArea.x += entity.speed; break;
                }

                //Checks for collision
                if(entity.solidArea.intersects(gp.obj[i].solidArea)) {

                    //System.out.println("Right collision");
                    if(gp.obj[i].collision){
                        entity.collisionOn = true;
                    }

                    if(player) {
                        index = i;
                    }

                }

                //Reset variables to default value so it doesn't keep adding the world value to the obj position
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.obj[i].solidArea.x = gp.obj[i].solidAreaDefaultX;
                gp.obj[i].solidArea.y = gp.obj[i].solidAreaDefaultY;

            }
        }

        return index;
    }

    //Entity Collision
    public int checkEntity(Entity entity, Entity[] target){

        int index = 999;//default return

        for(int i = 0; i < target.length; i++){

            if(target[i] != null) {

                // Get entity's solid area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;


                // Get object's solid area position
                target[i].solidArea.x = target[i].worldX + target[i].solidArea.x;
                target[i].solidArea.y = target[i].worldY + target[i].solidArea.y;

                //Direction of movement
                switch (entity.direction) {
                    case "up":      entity.solidArea.y -= entity.speed; break;
                    case "down":    entity.solidArea.y += entity.speed; break;
                    case "left":    entity.solidArea.x -= entity.speed; break;
                    case "right":   entity.solidArea.x += entity.speed; break;
                }
                //detection of collision
                if(entity.solidArea.intersects(target[i].solidArea)) {
                    if(target[i] != entity){
                        entity.collisionOn = true;
                        index = i;
                    }
                }

                //Reset variables to default value so it doesn't keep adding the world value to the obj position
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target[i].solidArea.x = target[i].solidAreaDefaultX;
                target[i].solidArea.y = target[i].solidAreaDefaultY;

            }
        }

        return index;
    }

    public boolean checkPlayer(Entity entity){

        boolean contactPlayer = false;
        // Get entity's solid area position
        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;


        // Get object's solid area position
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

        //Direction of movement
        switch (entity.direction) {
            case "up":      entity.solidArea.y -= entity.speed; break;
            case "down":    entity.solidArea.y += entity.speed; break;
            case "left":    entity.solidArea.x -= entity.speed; break;
            case "right":   entity.solidArea.x += entity.speed; break;
        }

        //Detects collision
        if(entity.solidArea.intersects(gp.player.solidArea)) {
            entity.collisionOn = true;
            contactPlayer = true;
        }

        //Reset variables to default value so it doesn't keep adding the world value to the obj position
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;

        return contactPlayer;
    }

    public ArrayList<Integer> checkSpellEntity(Spell spell, Entity[] entity){
        ArrayList<Integer> hitEntities = new ArrayList<>();
        if(spell.currentHitBox.equals("effect")){
            if(spell.effectHitBoxType.equals("circle")){
                hitEntities = checkSpellCircleEntity(spell,entity);
            }else if(spell.effectHitBoxType.equals("rectangle")){
                hitEntities = checkSpellRectangleEntity(spell, entity);
            }else {
                hitEntities.add(999);
            }
        }else if(spell.currentHitBox.equals("projectile")){
            if(spell.projectileHitBoxType.equals("circle")){
                hitEntities = checkSpellCircleEntity(spell,entity);
            }else if(spell.projectileHitBoxType.equals("rectangle")){
                hitEntities = checkSpellRectangleEntity(spell,entity);
            }else{
                hitEntities.add(999);
            }
        }else {
            hitEntities.add(999);
        }
        if(hitEntities.isEmpty()){
            hitEntities.add(999);
        }
        return hitEntities;
    }

    public ArrayList<Integer> checkSpellCircleEntity(Spell spell, Entity[] target) {
        ArrayList<Integer> hitEntities = new ArrayList<>();

        for (int i = 0; i < target.length; i++) {
            if (target[i] != null) {
                // Get entity's solid area position
                target[i].solidArea.x = target[i].worldX + target[i].solidArea.x;
                target[i].solidArea.y = target[i].worldY + target[i].solidArea.y;

                // Direction of movement
                switch (target[i].direction) {
                    case "up":      target[i].solidArea.y -= target[i].speed; break;
                    case "down":    target[i].solidArea.y += target[i].speed; break;
                    case "left":    target[i].solidArea.x -= target[i].speed; break;
                    case "right":   target[i].solidArea.x += target[i].speed; break;
                }

                // Detection of collision
                if (spell.circleHitBox.intersects(target[i].solidArea)) {
                    // Assures the caster won't hit themselves
                    if (target[i] != spell.caster) {
                        // Checks to see if it's monster-on-monster violence
                        if (target[i].type == 2 && spell.caster.type == 2) {
                            hitEntities.add(999); // AKA DO NOTHING
                        } else {
                            hitEntities.add(i); // Add the hit to the register
                        }
                    }
                }

                // Reset variables to default value
                target[i].solidArea.x = target[i].solidAreaDefaultX;
                target[i].solidArea.y = target[i].solidAreaDefaultY;
            }
        }
        return hitEntities;
    }

    public ArrayList<Integer> checkSpellRectangleEntity(Spell spell, Entity[] target){
        ArrayList<Integer> hitEntities = new ArrayList<>();
        int x = 0;
        int y = 0;
        for(int i = 0; i < target.length; i++){

            if(target[i] != null) {
                x = spell.rectangleHitBox.x;
                y = spell.rectangleHitBox.y;
                // Get entity's solid area position
                spell.rectangleHitBox.x = spell.rectangleHitBox.x - spell.rectangleHitBox.width/2;
                spell.rectangleHitBox.y = spell.rectangleHitBox.y - spell.rectangleHitBox.width/2;


                // Get object's solid area position
                target[i].solidArea.x = target[i].worldX + target[i].solidArea.x;
                target[i].solidArea.y = target[i].worldY + target[i].solidArea.y;

                //Direction of movement
                switch (target[i].direction) {
                    case "up":      target[i].solidArea.y -= target[i].speed; break;
                    case "down":    target[i].solidArea.y += target[i].speed; break;
                    case "left":    target[i].solidArea.x -= target[i].speed; break;
                    case "right":   target[i].solidArea.x += target[i].speed; break;
                }
                //detection of collision
                if(spell.rectangleHitBox.intersects(target[i].solidArea)) {
                    //Assures the caster wont off themselves
                    if(target[i] != spell.caster){
                        //Checks to see if its monster on monster violence
                        if(target[i].type == 2 && spell.caster.type == 2){
                            //AKA DO NOTHING
                            hitEntities.add(999);
                        }else{
                            //Add the hit to the register
                            hitEntities.add(i);
                        }
                    }
                }

                //Reset variables to default value so it doesn't keep adding the world value to the obj position
                spell.rectangleHitBox.x = x;
                spell.rectangleHitBox.y = y;
                target[i].solidArea.x = target[i].solidAreaDefaultX;
                target[i].solidArea.y = target[i].solidAreaDefaultY;
            }
        }
        return hitEntities;
    }

    public boolean checkSpellPlayer(Spell spell){
        boolean hit;
        if(spell.currentHitBox.equals("projectile")){
            if(spell.projectileHitBoxType.equals("circle")){
                hit = checkSpellCirclePlayer(spell);
            }else if(spell.projectileHitBoxType.equals("rectangle")){
                hit = checkSpellRectanglePlayer(spell);
            }else {
                hit = false;
            }
        }else if(spell.currentHitBox.equals("effect")){
            if(spell.effectHitBoxType.equals("circle")){
                hit = checkSpellCirclePlayer(spell);
            }else if(spell.effectHitBoxType.equals("rectangle")){
                hit = checkSpellRectanglePlayer(spell);
            }else{
                hit = false;
            }
        }else {
            hit = false;
        }
        return hit;
    }

    public boolean checkSpellRectanglePlayer(Spell spell){
        boolean hit = false;
        int x = 0;
        int y = 0;

        x = spell.rectangleHitBox.x;
        y = spell.rectangleHitBox.y;
        // Get entity's solid area position
        spell.rectangleHitBox.x = spell.rectangleHitBox.x - spell.rectangleHitBox.width/2;
        spell.rectangleHitBox.y = spell.rectangleHitBox.y - spell.rectangleHitBox.width/2;


        // Get object's solid area position
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

        //Direction of movement
        switch (gp.player.direction) {
            case "up":      gp.player.solidArea.y -= gp.player.speed; break;
            case "down":    gp.player.solidArea.y += gp.player.speed; break;
            case "left":    gp.player.solidArea.x -= gp.player.speed; break;
            case "right":   gp.player.solidArea.x += gp.player.speed; break;
        }
        //detection of collision
        if(spell.rectangleHitBox.intersects(gp.player.solidArea)) {
            //Assures the caster wont off themselves
            if(gp.player != spell.caster){
                hit = true;
            }
        }

        //Reset variables to default value so it doesn't keep adding the world value to the obj position
        spell.rectangleHitBox.x = x;
        spell.rectangleHitBox.y = y;
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;

        return hit;

    }

    public boolean checkSpellCirclePlayer(Spell spell){
        boolean hit = false;
        // Get entity's solid area position
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

        // Direction of movement
        switch (gp.player.direction) {
            case "up":      gp.player.solidArea.y -= gp.player.speed; break;
            case "down":    gp.player.solidArea.y += gp.player.speed; break;
            case "left":    gp.player.solidArea.x -= gp.player.speed; break;
            case "right":   gp.player.solidArea.x += gp.player.speed; break;
        }

        // Detection of collision
        if (spell.circleHitBox.intersects(gp.player.solidArea)) {
            // Assures the caster won't hit themselves
            if (gp.player != spell.caster) {
                hit = true;
            }
        }

        // Reset variables to default value
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;

        return hit;
    }

}