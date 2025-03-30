package grid;

import Assets.TagTracker;
import Magic.NewSpell;
import entity.Entity;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class Chunk {
    //Need to pass it TileSize*the size you want

    int size;
    HashSet<TagTracker> tagSet = new HashSet<>();
    List<Entity> entities = new ArrayList<>();
    List<NewSpell> spells = new ArrayList<>();

    public Chunk(int size) {
        this.size = size;
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }


    public void clearEntities() {
        entities.clear();
    }

    public void addSpell(NewSpell spell) {
        spells.add(spell);
    }

    public void removeSpell(NewSpell spell) {
        spells.remove(spell);
        if (spells.contains(spell)) {
            System.out.println("RUHH ROHH RAGGY");
        }
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public void clearSpells() {
        spells.clear();
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<NewSpell> getSpells() {
        return spells;
    }

    public void updateEntity() {

        entities.sort(Comparator.comparingInt((Entity e) -> e.location.getWorldX())
                .thenComparingInt(e -> e.location.getWorldY()));
        for (Entity e : entities) {
            if (e != null) {
                if (e.type != 3 && !e.updated) {
                    if (!e.isDone) {
                        if (tagSet.contains(e.tagTracker)) {
                            e.PrintDebug();
                        }
                        e.update();
                        e.updated = true;
                    }
                }
            }
        }
    }

    public void drawEntity(Graphics2D g2) {
        for (Entity e : entities) {
            e.draw(g2);
            e.drawn = true;
        }
    }

    public void drawSpell(Graphics2D g2) {
        for (NewSpell s : spells) {
            s.draw(g2);
            s.drawn = true;
        }
    }

    public void updateSpell() {
        spells.sort(Comparator.comparingInt((NewSpell s) -> s.current.getWorldX())
                .thenComparingInt(s -> s.current.getWorldY()));
        for (NewSpell s : spells) {
            if (s != null) {
                if (!s.updated) {
                    if (!s.getDone()) {
                        if (tagSet.contains(s.tagTracker)) {
                            s.PrintDebug();
                        }
                        s.update();
                        s.updated = true;
                    }
                }
            }
        }
    }

    public List<Entity> getEntitiesByType(int type) {
        List<Entity> entity = new ArrayList<>();
        for (Entity e : entities) {
            if (e.type == type) {
                entity.add(e);
            }
        }
        return entity;
    }

    public void setDebugList(List<TagTracker> tags) {
        tagSet.addAll(tags);
    }

}
