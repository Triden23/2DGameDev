package main;

import Magic.Coord;
import Magic.SpellManager;
import entity.Entity;
import entity.Player;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class GamePanel extends JPanel implements Runnable{

    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile
    public final int scale = 3;
    public final int tileSize = originalTileSize * scale; // 48x48 tile
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    public boolean showCollisionArea = true; //enable to show Collision areas

    // WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;

    // FPS
    public final int fps = 60;

    // SYSTEM
    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler(this);
    public SpellManager spellM = new SpellManager(this);

    Sound music = new Sound();
    Sound se = new Sound();

    //COLLISION
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);

    //DRAW UI
    public UI ui = new UI(this);
    //EVENT
    public EventHandler eHandler = new EventHandler(this);
    //GAME THREAD
    Thread gameThread;

    // ENTITY AND OBJECT
    public Player player = new Player(this, keyH);
    public Entity[] obj = new Entity[10];
    public Entity[] npc = new Entity[10];
    public Entity[] monster = new Entity[20];

    //All entities will be put into this list and it will be sorted by the entity's world.y (helps the draw method)
    ArrayList<Entity> entityList = new ArrayList<>();
    public ArrayList<Entity> currentEntities = new ArrayList<>();//USED BY SPELL MANAGER TO TRACK ENTITIES DOESN'T DO ANYTHING ELSE

    // GAME STATE -- allows keys to different things depending on game state.
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogState = 3;
    public final int characterState = 4;


    public GamePanel(){

        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); //allows for better rendering
        this.addKeyListener(keyH);
        this.setFocusable(true);

    }

    public void setupGame(){

        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
        //playMusic(0); // 0 is default sound track
        gameState = titleState;

    }

    public void startGameThread(){

        gameThread = new Thread(this);
        gameThread.start();

    }

    @Override
    public void run(){

        double drawInterval = 1000000000/fps;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while(gameThread != null){

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime)/ drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if(delta>=1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }
            if(timer >= 1000000000){
                //System.out.println("FPS: " +drawCount);
                drawCount = 0;
                timer = 0;
            }

        }
    }

    public void update(){
        //UPDATE LAYERS -> PLAYER -> ENTITIES -> Projectiles
        if(gameState == playState){
            // Player
            player.update();
            // ENTITIES
            for(int i = 0; i < npc.length; i++){

                if(npc[i] != null){
                    npc[i].update();
                }

            }
            //Monsters
            for(int i = 0; i < monster.length; i++){
                if(monster[i] != null){
                    //Check to see if the monster is dead or dying before updated its logic.
                    if(monster[i].alive && !monster[i].dying){
                        monster[i].update();
                    }
                    if(!monster[i].alive){
                        monster[i] = null;
                    }
                }
            }

            //SPELL UPDATE
            if(!spellM.spells.isEmpty()) {
                spellM.update();
            }

        }
        if(gameState == pauseState){
            // DO NOTHING
        }

    }

    public void paintComponent(Graphics g){

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        // DEBUG
        long drawStart = 0;
        if(keyH.showDebugText) {
            drawStart = System.nanoTime();
        }

        // - Draw order - Bottom to top - Tiles, Objects, Entities, Player, UI

        //TITLE SCREEN
        if(gameState == titleState){

            ui.draw(g2);

        }else{ // Any other state that is not at the main menu
            // TILES
            tileM.draw(g2);

            //Spell update
            //SPELL UPDATE
            if(!spellM.spells.isEmpty()) {
                spellM.draw(g2);
            }

            //ADDS ENTITIES TO THE LIST
            //adds player to list
            entityList.add(player);
            //adds npcs to list
            for(int i = 0; i < npc.length; i++){ // can switch to an advanced for loop later. test performance changes
                if(npc[i] != null){
                    entityList.add(npc[i]);
                }
            }
            //adds objects to list
            for(int i = 0; i < obj.length; i++){
                if(obj[i] != null){
                    entityList.add(obj[i]);
                }
            }

            //adds monsters to list
            for(int i = 0; i < monster.length; i++){
                if(monster[i] != null){
                    entityList.add(monster[i]);
                }
            }

            // SORT BY Y
            Collections.sort(entityList, new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2) {

                    int result = Integer.compare(e1.worldY, e2.worldY);
                    return result;

                }
            });

            //DRAW ENTITIES
            for(int i = 0; i < entityList.size(); i++){
                entityList.get(i).draw(g2);
            }
            //EMPTY ENTITY LIST
            entityList.clear();

            // UI
            ui.draw(g2);

            // DEBUG
            if(keyH.showDebugText) {
                long drawEnd = System.nanoTime();
                long passed = drawEnd - drawStart;


                g2.setFont(new Font("Arial", Font.PLAIN, 20));
                g2.setColor(Color.white);
                int x = 10;
                int y = 400;
                int lineHeight = 20;

                g2.drawString("WorldX: " + player.worldX, x, y); y += lineHeight;
                g2.drawString("WorldX: " + player.worldY, x, y); y += lineHeight;
                g2.drawString("Col: " + (player.worldX + player.solidArea.x)/tileSize, x, y);y += lineHeight;
                g2.drawString("Row: " + (player.worldY + player.solidArea.y)/tileSize, x, y);y += lineHeight;


                g2.drawString("Draw Time: " + passed, x, y);
                System.out.println("Draw Time: " + passed);
            }
        }



        g2.dispose();
    }

    public void updateEntityList(){
        //We will assume there is data in the entityList for spell manager and clear it
        currentEntities.clear();
        //Populate it with all the entities that can be targeted
        currentEntities.add(player);//Player needs to be in this list too
        //adds monsters to list
        for(int i = 0; i < monster.length; i++){
            if(monster[i] != null){
                entityList.add(monster[i]);
            }
        }
    }

    public ArrayList<Entity> getCurrentEntities(){
        updateEntityList();
        return currentEntities;
    }



    //Plays music
    public void playMusic(int i){

        music.setFile(i);
        music.play();
        music.loop();

    }

    //Stops music
    public void stopMusic(){

        music.stop();

    }

    //Plays short sound effect
    public void playSE(int i) {

        se.setFile(i);
        se.play();

    }

}
