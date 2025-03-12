package grid;

import Magic.Spell;
import entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class Chunk {
    //Need to pass it TileSize*the size you want

    int size;

    List<Entity> entities = new ArrayList<>();
    List<Spell> spells = new ArrayList<>();

    public Chunk(int size){
        this.size = size;
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public void addSpell(Spell spell){
        spells.add(spell);
    }

    public void removeSpell(Spell spell){
        spells.remove(spell);
    }

    public void clearEntities() {
        entities.clear();
    }

    public void clearSpells(){
        spells.clear();
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<Spell> getSpells() {
        return spells;
    }

}
