package main;

import Assets.AssetManager;
import Assets.ObjectPool;
import Assets.TagTracker;
import Navigation.NavMesh;
import entity.Player;
import grid.ChunkManager;
import tile.TileManager;
import tools.Utility;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class GamePanel extends JPanel implements Runnable {

    public final int scale = 3;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    // WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    // FPS
    public final int fps = 60;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogState = 3;
    public final int characterState = 4;
    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile
    public final int tileSize = originalTileSize * scale; // 48x48 tile
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels
    //Chunk Manager
    public ChunkManager chunkM = new ChunkManager(this, tileSize * 5, maxWorldCol * tileSize, maxWorldRow * tileSize);
    public boolean showCollisionArea = true; //enable to show Collision areas
    // SYSTEM
    public Utility tool = new Utility();
    //public SpellManager spellM = new SpellManager(this);
    public AssetManager assetM = new AssetManager(this);
    public ObjectPool objectP = new ObjectPool(this);
    //COLLISION
    //public CollisionChecker cChecker = new CollisionChecker(this);
    public CollisionManager collisionManager = new CollisionManager(this);
    public AssetSetter aSetter = new AssetSetter(this);
    //DRAW UI
    public UI ui = new UI(this);
    //EVENT
    public EventHandler eHandler = new EventHandler(this);
    public NavMesh navMesh = new NavMesh(maxWorldCol,maxWorldRow,3,this);
    // GAME STATE -- allows keys to different things depending on game state.
    public int gameState;
    public String scene = "Basic";

    public boolean debug = true;
    /* Chunk Update makes this obsolete

    public Entity[] npc = new Entity[10];
    public Entity[] monster = new Entity[20];
     */
    //public Entity[] obj = new Entity[10];

    //All entities will be put into this list and it will be sorted by the entity's world.y (helps the draw method)
    public String previousScene;
    public TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler(this);
    // ENTITY AND OBJECT
    public Player player = new Player(this, keyH);
    ArrayList<TagTracker> debugTags = new ArrayList<>();
    Sound music = new Sound();
    Sound se = new Sound();
    //GAME THREAD
    Thread gameThread;


    public GamePanel() {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); //allows for better rendering
        this.addKeyListener(keyH);
        this.setFocusable(true);

    }



    public void setupGame() {
        setDebugTags();
        navMesh.bakeTerrainMesh();
        chunkM.addEntity(player);
        aSetter.setObject();
        //aSetter.addNPC();
        aSetter.addMonsters();

        //playMusic(0); // 0 is default sound track
        gameState = titleState;

    }

    public void setDebugTags() {
        //Add any tags for de-bugging here for spells only do the SPE tag not the PRO or EFF
        if(debug) {
            debugTags.add(TagTracker.SPE_FireBall);
            debugTags.add(TagTracker.PLA_PLAYER);
            debugTags.add(TagTracker.DEB_NavMesh);
        }
        //Do not modify
        chunkM.setDebugTags(debugTags);
    }

    public void startGameThread() {

        gameThread = new Thread(this);
        gameThread.start();

    }

    @Override
    public void run() {

        double drawInterval = 1000000000 / fps;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }
            if (timer >= 1000000000) {
                //System.out.println("FPS: " +drawCount);
                drawCount = 0;
                timer = 0;
            }

        }
    }

    public void update() {
        if (!Objects.equals(scene, assetM.getScene())) {
            if (scene != null) {
                previousScene = scene;
            }
            scene = assetM.getScene();
            if (previousScene != null) {
                assetM.unloadSceneAssets(previousScene);
            }
            assetM.loadSceneAssets(scene);
        }
        //UPDATE LAYERS -> PLAYER -> ENTITIES -> Projectiles
        if (gameState == playState) {

            //Chunk update
            updatePlayState();

        }
        if (gameState == pauseState) {
            // DO NOTHING
        }

    }

    /*
    public void updatePlayStateOld(){
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
     */

    public void updatePlayState() {
        chunkM.update();
        chunkM.updateEntityChunks();
        chunkM.updateSpellChunks();
        chunkM.releaseFlags();
    }



    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // DEBUG
        long drawStart = 0;
        if (keyH.showDebugText) {
            drawStart = System.nanoTime();
        }

        // - Draw order - Bottom to top - Tiles, Objects, Entities, Player, UI

        //TITLE SCREEN
        if (gameState == titleState) {

            ui.draw(g2);

        } else { // Any other state that is not at the main menu
            // TILES
            tileM.draw(g2);

            chunkM.draw(g2);
            if(debug){
                if(debugTags.contains(TagTracker.DEB_NavMesh)){
                    navMesh.debugDraw(g2);
                }
            }

            // UI
            ui.draw(g2);

            // DEBUG
            if (keyH.showDebugText) {
                long drawEnd = System.nanoTime();
                long passed = drawEnd - drawStart;

                g2.setFont(new Font("Arial", Font.PLAIN, 20));
                g2.setColor(Color.white);
                int x = 10;
                int y = 400;
                int lineHeight = 20;

                g2.drawString("WorldX: " + player.location.getWorldX(), x, y);
                y += lineHeight;
                g2.drawString("WorldX: " + player.location.getWorldX(), x, y);
                y += lineHeight;
                g2.drawString("Col: " + (player.location.getWorldX() + player.solidArea.x) / tileSize, x, y);
                y += lineHeight;
                g2.drawString("Row: " + (player.location.getWorldY() + player.solidArea.y) / tileSize, x, y);
                y += lineHeight;


                g2.drawString("Draw Time: " + passed, x, y);
                System.out.println("Draw Time: " + passed);
            }
        }


        g2.dispose();
    }

    /*
    public void oldPlayStateDraw(){
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
    }


     */
    /*
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

     */
/*
    public ArrayList<Entity> getCurrentEntities(){
        updateEntityList();
        return currentEntities;
    }

 */

    //Plays music
    public void playMusic(int i) {

        music.setFile(i);
        music.play();
        music.loop();

    }

    //Stops music
    public void stopMusic() {

        music.stop();

    }

    //Plays short sound effect
    public void playSE(int i) {

        se.setFile(i);
        se.play();

    }

}
