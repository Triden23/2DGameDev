package main;

import java.awt.*;

public class EventHandler {
    //x 29 y 17
    GamePanel gp;
    EventRect eventRect[][]; //Creating a unique rectangle on each tile that can trigger something. Maybe reduce this by deleting all that are not doing an event.

    //Soft cooldown to events variables
    int previousEventX, previousEventY;
    boolean canTouchEvent = true;

    public EventHandler(GamePanel gp){
        this.gp = gp;
        eventRect = new EventRect[gp.maxWorldCol][gp.maxWorldRow];


        //2x2 pixels rectangle for event. We want it this small so players have to intentionally walk into a tile to activate an event

        int col = 0;
        int row = 0;
        while(col < gp.maxWorldCol && row < gp.maxWorldRow){
            eventRect[col][row] = new EventRect();
            eventRect[col][row].x = 23;
            eventRect[col][row].y = 23;
            eventRect[col][row].width = 2;
            eventRect[col][row].height = 2;
            eventRect[col][row].eventRectDefaultX = eventRect[col][row].x;
            eventRect[col][row].eventRectDefaultY = eventRect[col][row].y;

            col++;
            if(col == gp.maxWorldCol){
                col = 0;
                row++;
            }
        }


    }

    public void checkEvent() {

        //Check to see if the player moves one tile form the last event
        //-need a positive number so if the player moves left 1 tile it will return -1 we use abs to fix that
        int xDistance = Math.abs(gp.player.worldX - previousEventX);
        int yDistance = Math.abs(gp.player.worldY - previousEventY);
        //-gets the greater of the 2 numbers and returns it
        int distance = Math.max(xDistance,yDistance);

        //-If the player is more than a tile away you can trigger the event again
        if(distance > gp.tileSize){
            previousEventX = gp.player.worldX;
            previousEventY = gp.player.worldY;
            canTouchEvent = true;
        }

        //Debug
        //System.out.println("Current X: " + gp.player.worldX + " |Previous Event X: " + previousEventX + " Expected Return: " + xDistance);
        //System.out.println("Current Y: " + gp.player.worldY + " |Previous Event Y: " + previousEventY + " Expected Return: " + yDistance);
        //System.out.println("Distance from event: " + distance + "|" + gp.tileSize);

        //format it like this since we will have multiple event that will keep this block of code clean

        if(canTouchEvent == true) {
            if(hit(29,17,"right")){     damagePit(  29, 17, gp.dialogState); }
            if(hit(23, 6, "up")){       healingPool(23, 6,  gp.dialogState); }
            if(hit(23, 40, "down")){    teleport(   23,  40,  gp.dialogState,10,12); }
            if(hit(23,19,"any")){       damagePit(  23,   19,    gp.dialogState);}
        }

    }

    //Check event collision
    public boolean hit(int col, int row, String reqDirection){
        boolean hit = false;

        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
        eventRect[col][row].x = col * gp.tileSize + eventRect[col][row].x;
        eventRect[col][row].y = row * gp.tileSize + eventRect[col][row].y;

        //Checking to see if the player collision box is overlapping the event collision box, then checking to see if it has any directional requirements to play.
        if(gp.player.solidArea.intersects(eventRect[col][row]) && !eventRect[col][row].eventDone){
            if(gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")){
                hit = true;

                previousEventX = gp.player.worldX;
                previousEventY = gp.player.worldY;

            }
        }

        //Reset these values to default
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;

        eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
        eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;
        return hit;
    }
    //Test event - maybe add a count of frames before the damage can apply again.
    public void damagePit(int col, int row, int gameState){
        //set the game state we want the event to happen in.
        gp.gameState = gameState;
        //display text if we want it to show
        gp.ui.currentDialog = "You fall into a pit";
        gp.player.attackCancled = true;
        gp.playSE(6);
        //the effect of the event - for now its health as that is the only stat in the game at the moment
        gp.player.life -= 1;
        //eventRect[col][row].eventDone = true; <- example to make it a one time event
        canTouchEvent = false; //<-- used if it is a repeatable event such as damage tick
    }

    public void healingPool(int col, int row, int gameState){
        if(gp.keyH.enterPressed){

            gp.gameState = gameState;
            gp.ui.currentDialog = "You drink from the well of mountain dew";
            gp.player.attackCancled = true;
            gp.playSE(2);
            gp.player.life = gp.player.maxLife;
            canTouchEvent = false;
            gp.aSetter.setMonster();
        }
    }

    private void teleport(int col, int row, int gameState, int locationX, int locationY) {

        gp.gameState = gameState;
        gp.ui.currentDialog = "Woosh, you have been teleported";

        gp.playSE(1);

        gp.player.worldX = gp.tileSize*locationX;
        gp.player.worldY = gp.tileSize*locationY;

        canTouchEvent = false;
    }


}
