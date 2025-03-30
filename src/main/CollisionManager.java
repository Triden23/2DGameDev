package main;

import Magic.NewSpell;
import entity.Entity;
import tools.Circle;

import java.awt.*;
import java.util.List;

public class CollisionManager {

    GamePanel gp;

    public CollisionManager(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {

        int entityLeftWorldX = entity.location.getWorldX() + entity.solidArea.x;
        int entityRightWorldX = entity.location.getWorldX() + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.location.getWorldY() + entity.solidArea.y;
        int entityBottomWorldY = entity.location.getWorldY() + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    //player is hitting a solid tile
                    entity.collisionOn = true;
                }
                break;

            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    //player is hitting a solid tile
                    entity.collisionOn = true;
                }
                break;

            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    //player is hitting a solid tile
                    entity.collisionOn = true;
                }
                break;

            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    //player is hitting a solid tile
                    entity.collisionOn = true;
                }
                break;

        }

    }

    public Entity checkObject(Entity entity, boolean player) {
        /*uses different collision detection because there will be a
         smaller amount of objects than tiles. can be reworked should this change */

        Entity e = null;

        List<Entity> gameObjects = gp.chunkM.getThreeByThreeType(3, entity.currentChunkX, entity.currentChunkY);//gp.chunkM.getEntitiesInChunkByType(3,entity.currentChunkX,entity.currentChunkY);

        if (!gameObjects.isEmpty()) {
            for (Entity gameObject : gameObjects) {

                // Get entity's solid area position
                entity.solidArea.x = entity.location.getWorldX() + entity.solidArea.x;
                entity.solidArea.y = entity.location.getWorldY() + entity.solidArea.y;


                // Get object's solid area position
                gameObject.solidArea.x = gameObject.location.getWorldX() + gameObject.solidArea.x;
                gameObject.solidArea.y = gameObject.location.getWorldY() + gameObject.solidArea.y;

                //Direction of movement
                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        break;
                }

                //Checks for collision
                if (entity.solidArea.intersects(gameObject.solidArea)) {

                    //System.out.println("Right collision");
                    if (gameObject.collision) {
                        entity.collisionOn = true;
                    }

                    if (player) {
                        entity = gameObject;
                    }

                }

                //Reset variables to default value so it doesn't keep adding the world value to the obj position
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gameObject.solidArea.x = gameObject.solidAreaDefaultX;
                gameObject.solidArea.y = gameObject.solidAreaDefaultY;

            }
        }

        return entity;
    }


    //Entity Collision
    public Entity checkEntity(Entity entity) {

        List<Entity> target = gp.chunkM.getThreeByThree(entity.currentChunkX, entity.currentChunkY);
        Entity e = null;

        for (int i = 0; i < target.size(); i++) {
            if (target.get(i) != entity) {
                if (target.get(i) != null) {

                    // Get entity's solid area position
                    entity.solidArea.x = entity.location.getWorldX() + entity.solidArea.x;
                    entity.solidArea.y = entity.location.getWorldY() + entity.solidArea.y;


                    // Get object's solid area position
                    target.get(i).solidArea.x = target.get(i).worldX + target.get(i).solidArea.x;
                    target.get(i).solidArea.y = target.get(i).worldY + target.get(i).solidArea.y;

                    //Direction of movement
                    switch (entity.direction) {
                        case "up":
                            entity.solidArea.y -= entity.speed;
                            break;
                        case "down":
                            entity.solidArea.y += entity.speed;
                            break;
                        case "left":
                            entity.solidArea.x -= entity.speed;
                            break;
                        case "right":
                            entity.solidArea.x += entity.speed;
                            break;
                    }
                    //detection of collision
                    if (entity.solidArea.intersects(target.get(i).solidArea)) {
                        if (target.get(i) != entity) {
                            entity.collisionOn = true;
                            e = target.get(i);
                        }
                    }

                    //Reset variables to default value so it doesn't keep adding the world value to the obj position
                    entity.solidArea.x = entity.solidAreaDefaultX;
                    entity.solidArea.y = entity.solidAreaDefaultY;
                    target.get(i).solidArea.x = target.get(i).solidAreaDefaultX;
                    target.get(i).solidArea.y = target.get(i).solidAreaDefaultY;

                }
            }
        }

        return e;
    }

    public Entity checkEntityByType(int type, Entity entity) {

        List<Entity> target = gp.chunkM.getEntitiesInChunkByType(type, entity.currentChunkX, entity.currentChunkY);
        Entity e = null;

        for (int i = 0; i < target.size(); i++) {
            if (target.get(i) != entity) {
                if (target.get(i) != null) {

                    // Get entity's solid area position
                    entity.solidArea.x = entity.location.getWorldX() + entity.solidArea.x;
                    entity.solidArea.y = entity.location.getWorldY() + entity.solidArea.y;


                    // Get object's solid area position
                    target.get(i).solidArea.x = target.get(i).worldX + target.get(i).solidArea.x;
                    target.get(i).solidArea.y = target.get(i).worldY + target.get(i).solidArea.y;

                    //Direction of movement
                    switch (entity.direction) {
                        case "up":
                            entity.solidArea.y -= entity.speed;
                            break;
                        case "down":
                            entity.solidArea.y += entity.speed;
                            break;
                        case "left":
                            entity.solidArea.x -= entity.speed;
                            break;
                        case "right":
                            entity.solidArea.x += entity.speed;
                            break;
                    }
                    //detection of collision
                    if (entity.solidArea.intersects(target.get(i).solidArea)) {
                        if (target.get(i) != entity) {
                            entity.collisionOn = true;
                            e = target.get(i);
                        }
                    }

                    //Reset variables to default value so it doesn't keep adding the world value to the obj position
                    entity.solidArea.x = entity.solidAreaDefaultX;
                    entity.solidArea.y = entity.solidAreaDefaultY;
                    target.get(i).solidArea.x = target.get(i).solidAreaDefaultX;
                    target.get(i).solidArea.y = target.get(i).solidAreaDefaultY;

                }
            }
        }

        return e;
    }

    public boolean checkPlayer(Entity entity) {

        boolean contactPlayer = false;
        List<Entity> area = gp.chunkM.getThreeByThreeType(0, entity.currentChunkX, entity.currentChunkY);
        if (!area.isEmpty()) {
            // Get entity's solid area position
            entity.solidArea.x = entity.location.getWorldX() + entity.solidArea.x;
            entity.solidArea.y = entity.location.getWorldY() + entity.solidArea.y;


            // Get object's solid area position
            gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
            gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

            //Direction of movement
            switch (entity.direction) {
                case "up":
                    entity.solidArea.y -= entity.speed;
                    break;
                case "down":
                    entity.solidArea.y += entity.speed;
                    break;
                case "left":
                    entity.solidArea.x -= entity.speed;
                    break;
                case "right":
                    entity.solidArea.x += entity.speed;
                    break;
            }

            //Detects collision
            if (entity.solidArea.intersects(gp.player.solidArea)) {
                entity.collisionOn = true;
                contactPlayer = true;
            }

            //Reset variables to default value so it doesn't keep adding the world value to the obj position
            entity.solidArea.x = entity.solidAreaDefaultX;
            entity.solidArea.y = entity.solidAreaDefaultY;
            gp.player.solidArea.x = gp.player.solidAreaDefaultX;
            gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        }

        return contactPlayer;
    }

    public void checkSpellRectangle(NewSpell spell, Rectangle rect) {
        java.util.List<Entity> target = gp.chunkM.getThreeByThree(spell.currentChunkX,spell.currentChunkY);//gp.chunkM.getEntitiesInChunk(spell.currentChunkX, spell.currentChunkY);
        int x = 0;
        int y = 0;
        for (int i = 0; i < target.size(); i++) {

            if (target.get(i) != null) {
                x = rect.x;
                y = rect.y;


                // Get object's solid area position
                target.get(i).solidArea.x = target.get(i).worldX + target.get(i).solidArea.x;
                target.get(i).solidArea.y = target.get(i).worldY + target.get(i).solidArea.y;

                //Direction of movement
                switch (target.get(i).direction) {
                    case "up":
                        target.get(i).solidArea.y -= target.get(i).speed;
                        break;
                    case "down":
                        target.get(i).solidArea.y += target.get(i).speed;
                        break;
                    case "left":
                        target.get(i).solidArea.x -= target.get(i).speed;
                        break;
                    case "right":
                        target.get(i).solidArea.x += target.get(i).speed;
                        break;
                }
                //detection of collision
                if (rect.intersects(target.get(i).solidArea)) {
                    //Assures the caster wont off themselves
                    if (target.get(i) != spell.caster) {
                        //Checks to see if its monster on monster violence
                        if (target.get(i).type == 2 && spell.caster.type == 2) {

                        } else {
                            //Add the hit to the register
                            spell.addAffected(target.get(i));
                        }
                    }
                }

                System.out.println("Spell Hitbox: X=" + rect.x + " Y=" + rect.y +
                        " W=" + rect.width + " H=" + rect.height);
                System.out.println("Target: X=" + target.get(i).worldX + " Y=" + target.get(i).worldY +
                        " W=" + target.get(i).solidArea.width + " H=" + target.get(i).solidArea.height);

                //Reset variables to default value so it doesn't keep adding the world value to the obj position
                rect.x = x;
                rect.y = y;
                target.get(i).solidArea.x = target.get(i).solidAreaDefaultX;
                target.get(i).solidArea.y = target.get(i).solidAreaDefaultY;
            }
        }
    }

    //Used by navigation mesh to detect if its taking up space
    public boolean navigationCubeCheck(Entity e, Rectangle r){
        boolean value = false;
        // Get entity's solid area position
        e.solidArea.x = e.location.getWorldX() + e.solidArea.x;
        e.solidArea.y = e.location.getWorldY() + e.solidArea.y;

        //detection of collision
        if (e.solidArea.intersects(r)) {
            value = true;
        }

        //Reset variables to default value so it doesn't keep adding the world value to the obj position
        e.solidArea.x = e.solidAreaDefaultX;
        e.solidArea.y = e.solidAreaDefaultY;

        return value;
    }

    public void checkSpellCircle(NewSpell spell, Circle circle) {
        List<Entity> target = gp.chunkM.getEntitiesInChunk(spell.currentChunkX, spell.currentChunkY);

        for (int i = 0; i < target.size(); i++) {
            if (target.get(i) != null) {
                // Get entity's solid area position
                target.get(i).solidArea.x = target.get(i).worldX + target.get(i).solidArea.x;
                target.get(i).solidArea.y = target.get(i).worldY + target.get(i).solidArea.y;

                // Direction of movement
                switch (target.get(i).direction) {
                    case "up":
                        target.get(i).solidArea.y -= target.get(i).speed;
                        break;
                    case "down":
                        target.get(i).solidArea.y += target.get(i).speed;
                        break;
                    case "left":
                        target.get(i).solidArea.x -= target.get(i).speed;
                        break;
                    case "right":
                        target.get(i).solidArea.x += target.get(i).speed;
                        break;
                }

                // Detection of collision
                if (circle.intersects(target.get(i).solidArea)) {
                    // Assures the caster won't hit themselves
                    if (target.get(i) != spell.caster) {
                        // Checks to see if it's monster-on-monster violence
                        if (target.get(i).type == 2 && spell.caster.type == 2) {

                        } else {
                            spell.addAffected(target.get(i)); // Add the hit to the register
                        }
                    }
                }

                // Reset variables to default value
                target.get(i).solidArea.x = target.get(i).solidAreaDefaultX;
                target.get(i).solidArea.y = target.get(i).solidAreaDefaultY;
            }
        }
    }

}
