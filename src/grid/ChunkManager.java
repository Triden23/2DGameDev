package grid;

import Assets.TagTracker;
import Magic.NewSpell;
import entity.Entity;
import main.GamePanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChunkManager {
    public int x, y;
    int worldSizeX, worldSizeY, chunkSize;
    Chunk[][] grid;
    int tickCounter = 0;
    boolean navMeshUpdate = false;
    GamePanel gp;

    public ChunkManager(GamePanel gp, int chunkSize, int worldSizeX, int worldSizeY) {
        this.worldSizeX = worldSizeX;
        this.worldSizeY = worldSizeY;
        this.chunkSize = chunkSize;
        this.gp = gp;

        x = (int) Math.ceil((double) worldSizeX / chunkSize);
        System.out.println(x);
        y = (int) Math.ceil((double) worldSizeY / chunkSize);
        grid = new Chunk[x][y];

        populate();
    }

    private void populate() {
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                grid[i][j] = new Chunk(chunkSize);
            }
        }
    }

    private boolean isValidChunk(int chunkX, int chunkY) {
        return chunkX >= 0 && chunkX < x && chunkY >= 0 && chunkY < y;
    }

    public void addEntity(Entity entity) {
        int chunkX = entity.location.getWorldX() / chunkSize;
        int chunkY = entity.location.getWorldY() / chunkSize;
        if (isValidChunk(chunkX, chunkY)) {
            grid[chunkX][chunkY].addEntity(entity);
            entity.currentChunkX = chunkX;
            entity.currentChunkY = chunkY;
        }
    }

    public void removeEntity(Entity entity) {
        int chunkX = entity.currentChunkX;
        int chunkY = entity.currentChunkY;
        if (isValidChunk(chunkX, chunkY)) {
            grid[chunkX][chunkY].removeEntity(entity);
        }
    }

    public void updateEntityChunk(Entity entity) {
        int newChunkX = entity.location.getWorldX() / chunkSize;
        int newChunkY = entity.location.getWorldY() / chunkSize;
        if (newChunkX != entity.currentChunkX || newChunkY != entity.currentChunkY) {
            removeEntity(entity);
            addEntity(entity);
        }
    }

    public void updateSpellChunk(NewSpell spell) {
        int newChunkX = spell.getCurrent().getWorldX() / chunkSize;
        int newChunkY = spell.getCurrent().getWorldY() / chunkSize;
        if (newChunkX != spell.currentChunkX || newChunkY != spell.currentChunkY) {
            removeSpell(spell);
            spell.currentChunkX = newChunkX;
            spell.currentChunkY = newChunkY;
            addSpell(spell);

        }
    }

    public void addSpell(NewSpell spell) {
        System.out.println("Adding a spell " + spell.tagTracker.toString());
        int chunkX = spell.getCurrent().getWorldX() / chunkSize;
        int chunkY = spell.getCurrent().getWorldY() / chunkSize;
        if (isValidChunk(chunkX, chunkY)) {
            grid[chunkX][chunkY].addSpell(spell);
        }
    }

    public void removeSpell(NewSpell spell) {
        int chunkX = spell.currentChunkX; // Use stored chunk position
        int chunkY = spell.currentChunkY;

        if (!isValidChunk(chunkX, chunkY)) {
            System.out.println("Error: Trying to remove spell from an invalid chunk " + chunkX + "-" + chunkY);
            return;
        }

        if (!grid[chunkX][chunkY].spells.contains(spell)) {
            System.out.println("Error: Spell not found in chunk but attempting to remove!");
            return;
        }

        grid[chunkX][chunkY].removeSpell(spell);

        if (grid[chunkX][chunkY].spells.contains(spell)) {
            System.out.println("Spell was NOT removed properly from " + chunkX + "-" + chunkY);
        }
    }


    public List<Entity> getEntitiesInChunk(int chunkX, int chunkY) {
        if (isValidChunk(chunkX, chunkY)) {
            return grid[chunkX][chunkY].getEntities();
        }
        return Collections.emptyList();
    }

    public List<Entity> getAllEntities() {
        List<Entity> e = new ArrayList<>();
        for (Chunk[] row : grid) {
            for (Chunk index : row) {
                if(!index.entities.isEmpty()){
                    e.addAll(index.getEntities());
                }
            }
        }
        return e;
    }

    public List<NewSpell> getSpellsInChunk(int chunkX, int chunkY) {
        if (isValidChunk(chunkX, chunkY)) {
            return grid[chunkX][chunkY].spells;
        }
        return Collections.emptyList();
    }

    public void update() {
        tickCounter++;
        if(tickCounter>=(gp.fps/2)){
            gp.navMesh.cleanOccupied();
            gp.navMesh.entityPass(getAllEntities());
            gp.navMesh.setCubeNeighbors();
            tickCounter=0;
        }
        for (int ax = 0; ax < x; ax++) {
            for (int ay = 0; ay < y; ay++) {
                if (!grid[ax][ay].entities.isEmpty()) {
                    grid[ax][ay].updateEntity();
                }
                if (!grid[ax][ay].spells.isEmpty()) {
                    grid[ax][ay].updateSpell();
                }
            }
        }
    }

    public void draw(Graphics2D g2) {
        for (int ax = 0; ax < x; ax++) {
            for (int ay = 0; ay < y; ay++) {
                if (!grid[ax][ay].entities.isEmpty()) {
                    grid[ax][ay].drawEntity(g2);
                }
                if (!grid[ax][ay].spells.isEmpty()) {
                    grid[ax][ay].drawSpell(g2);
                }
            }
        }
    }

    public void releaseFlags() {
        List<Entity> e = new ArrayList<>();
        List<NewSpell> s = new ArrayList<>();
        for (int ax = 0; ax < x; ax++) {
            for (int ay = 0; ay < y; ay++) {
                if (!grid[ax][ay].entities.isEmpty()) {
                    e.addAll(grid[ax][ay].getEntities());
                }
                if (!grid[ax][ay].spells.isEmpty()) {
                    s.addAll(grid[ax][ay].getSpells());
                }
            }
        }
        int tempx, tempy;
        for (Entity ex : e) {
            //System.out.println(ex.name+ " - " + ex.currentChunkX + "-" + ex.currentChunkY);
            ex.updated = false;
            ex.drawn = false;
            if (ex.getDone()) {
                tempx = ex.currentChunkX;
                tempy = ex.currentChunkY;

                System.out.println("Before Removal: " + tempx + "-" + tempy + " " + grid[tempx][tempy].entities.size());
                removeEntity(ex);
                System.out.println("After Removal: " + tempx + "-" + tempy + " " + grid[tempx][tempy].entities.size());
            }
        }

        for (NewSpell sx : s) {
            System.out.println(sx.name + " - " + sx.currentChunkX + "-" + sx.currentChunkY + "-" + sx.isDone);
            tempx = sx.currentChunkX;
            tempy = sx.currentChunkY;
            sx.updated = false;
            sx.drawn = false;
            if (sx.getDone()) {
                System.out.println("Before Removal: " + tempx + "-" + tempy + " " + grid[tempx][tempy].spells.size());
                removeSpell(sx);
                System.out.println("After Removal: " + tempx + "-" + tempy + " " + grid[tempx][tempy].spells.size());
            }
        }
    }

    public List<Entity> getThreeByThree(int cx, int cy) {
        boolean top = (cx == 0);
        boolean left = (cy == 0);
        boolean bottom = (cx == x);
        boolean right = (cy == y);

        List<Entity> entitiesInSurroundingChunks = new ArrayList<>();

        // Loop through surrounding 3x3 chunks
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int chunkX = cx + dx;
                int chunkY = cy + dy;

                // Skip out-of-bounds chunks
                if ((dx == -1 && top) || (dx == 1 && bottom) || (dy == -1 && left) || (dy == 1 && right)) {
                    continue;
                }

                List<Entity> entities = getEntitiesInChunk(chunkX, chunkY);
                if (!entities.isEmpty()) {
                    entitiesInSurroundingChunks.addAll(entities);
                }
            }
        }
        return entitiesInSurroundingChunks;
    }

    public List<Entity> getEntitiesInChunkByType(int type, int x, int y) {
        return grid[x][y].getEntitiesByType(type);
    }

    public List<Entity> getThreeByThreeType(int type, int cx, int cy) {
        boolean top = (cx == 0);
        boolean left = (cy == 0);
        boolean bottom = (cx == x);
        boolean right = (cy == y);

        List<Entity> entitiesInSurroundingChunks = new ArrayList<>();

        // Loop through surrounding 3x3 chunks
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int chunkX = cx + dx;
                int chunkY = cy + dy;

                // Skip out-of-bounds chunks
                if ((dx == -1 && top) || (dx == 1 && bottom) || (dy == -1 && left) || (dy == 1 && right)) {
                    continue;
                }

                List<Entity> entities = getEntitiesInChunkByType(type, chunkX, chunkY);
                if (!entities.isEmpty()) {
                    entitiesInSurroundingChunks.addAll(entities);
                }
            }
        }
        return entitiesInSurroundingChunks;
    }

    public void updateEntityChunks() {
        List<Entity> lists = new ArrayList<>();
        for (int i = 0; i < x; i++) {
            for (int a = 0; a < y; a++) {
                lists.addAll(grid[i][a].getEntities());
            }
        }
        for (Entity e : lists) {
            updateEntityChunk(e);
        }
    }

    public void updateSpellChunks() {
        List<NewSpell> lists = new ArrayList<>();
        for (int i = 0; i < x; i++) {
            for (int a = 0; a < y; a++) {
                lists.addAll(grid[i][a].getSpells());
            }
        }
        for (NewSpell s : lists) {
            updateSpellChunk(s);
        }
    }

    public void setDebugTags(List<TagTracker> t) {
        for (Chunk[] row : grid) {
            for (Chunk index : row) {
                index.setDebugList(t);
            }
        }
    }

    public int getChunkLocationValue(int location) {
        return (location / chunkSize);
    }
}
