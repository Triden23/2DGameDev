package main;

import entity.NPC_Oldman;
import monster.MON_BlueSlime;

import java.util.Random;


public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setObject() {

        //gp.obj[0] = new OBJ_Door(gp);
        //gp.obj[0].worldX = gp.tileSize*22;
        //gp.obj[0].worldY = gp.tileSize*23;

    }

    public void setNPC(){

        gp.npc[0] = new NPC_Oldman(gp);
        gp.npc[0].worldX = gp.tileSize*8;
        gp.npc[0].worldY = gp.tileSize*7;

    }

    public void setMonster(){
        Random random = new Random();
        int i = 0;

        gp.monster[i] = new MON_BlueSlime(gp);
        gp.monster[i].worldX = gp.tileSize*8;
        gp.monster[i].worldY = gp.tileSize*8;
        gp.monster[i].tag =gp.monster[i].name + "_" + i;
        i++;
        gp.monster[i] = new MON_BlueSlime(gp);
        gp.monster[i].worldX = gp.tileSize*8;
        gp.monster[i].worldY = gp.tileSize*9;
        gp.monster[i].tag =gp.monster[i].name + "_" + i;

        i++;
        gp.monster[i] = new MON_BlueSlime(gp);
        gp.monster[i].worldX = gp.tileSize*8;
        gp.monster[i].worldY = gp.tileSize*10;
        gp.monster[i].tag =gp.monster[i].name + "_" + i;
        i++;
        gp.monster[i] = new MON_BlueSlime(gp);
        gp.monster[i].worldX = gp.tileSize*9;
        gp.monster[i].worldY = gp.tileSize*8;
        gp.monster[i].tag =gp.monster[i].name + "_" + i;
        i++;
        gp.monster[i] = new MON_BlueSlime(gp);
        gp.monster[i].worldX = gp.tileSize*9;
        gp.monster[i].worldY = gp.tileSize*9;
        gp.monster[i].tag =gp.monster[i].name + "_" + i;
        i++;
        gp.monster[i] = new MON_BlueSlime(gp);
        gp.monster[i].worldX = gp.tileSize*9;
        gp.monster[i].worldY = gp.tileSize*10;
        gp.monster[i].tag =gp.monster[i].name + "_" + i;
        i++;




    }

}
