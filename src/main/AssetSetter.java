package main;

import Assets.TagTracker;


public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {

        //gp.obj[0] = new OBJ_Door(gp);
        //gp.obj[0].worldX = gp.tileSize*22;
        //gp.obj[0].worldY = gp.tileSize*23;
        //gp.objectP.addObject(TagTracker.OBJ_Boots,22,23);

    }

//    public void setNPC(){
//
//        gp.npc[0] = new NPC_Oldman(gp);
//        gp.npc[0].requestTransform(gp.tileSize*8,gp.tileSize*7);
//        gp.npc[0].tag = gp.npc[0].name + "0";
//
//    }

    public void addNPC() {
        int x = 8;
        int y = 7;
        int health = 999;

        //gp.objectP.addEntity(TagTracker.NPC_OldMan,health,x,y);
    }


//    public void setMonster(){
//        int x,y;
//        Random random = new Random();
//        int i = 0;

//        gp.monster[i] = new MON_BlueSlime(gp);
//        x = gp.tileSize*8;
//        y = gp.tileSize*8;
//        gp.monster[i].requestTransform(x,y);
//        gp.monster[i].tag =gp.monster[i].name + "_" + i;
//        i++;
//        gp.monster[i] = new MON_BlueSlime(gp);
//        x = gp.tileSize*8;
//        y = gp.tileSize*9;
//        gp.monster[i].requestTransform(x,y);
//        gp.monster[i].tag =gp.monster[i].name + "_" + i;

//        i++;
//        gp.monster[i] = new MON_BlueSlime(gp);
//        x = gp.tileSize*8;
//        y = gp.tileSize*10;
//        gp.monster[i].requestTransform(x,y);
//        gp.monster[i].tag =gp.monster[i].name + "_" + i;
//        i++;
//        gp.monster[i] = new MON_BlueSlime(gp);
//        x = gp.tileSize*9;
//        y = gp.tileSize*8;
//        gp.monster[i].requestTransform(x,y);
//        gp.monster[i].tag =gp.monster[i].name + "_" + i;
//        i++;
//        gp.monster[i] = new MON_BlueSlime(gp);
//        x = gp.tileSize*9;
//        y = gp.tileSize*9;
//        gp.monster[i].requestTransform(x,y);
//        gp.monster[i].tag =gp.monster[i].name + "_" + i;
//        i++;
//        gp.monster[i] = new MON_BlueSlime(gp);
//        x = gp.tileSize*9;
//        y = gp.tileSize*10;
//        gp.monster[i].requestTransform(x,y);
//        gp.monster[i].tag =gp.monster[i].name + "_" + i;
//        i++;
//    }

    public void addMonsters() {
        int x, y;
        TagTracker tag = TagTracker.MON_BlueSlime;
        int health = 10;
        x = 8;
        y = 8;
        gp.objectP.addEntity(tag, health, x, y);

        y = 9;
        gp.objectP.addEntity(tag, health, x, y);

        y = 10;
        gp.objectP.addEntity(tag, health, x, y);
        x = 9;
        y = 8;
        gp.objectP.addEntity(tag, health, x, y);

        y = 9;
        gp.objectP.addEntity(tag, health, x, y);

        y = 10;
        gp.objectP.addEntity(tag, health, x, y);
    }

}
