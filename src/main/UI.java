package main;

import entity.Entity;
import object.OBJ_Heart;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font maruMonica, purisaBold;

    //UI health Icons
    BufferedImage heart_full,heart_quarter_missing,heart_half,heart_quarter,heart_empty;

    public boolean messageOn = false;

    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();

    public boolean gameFinished = false;
    public String currentDialog = "";

    //Inventory management variables
    public int slotCol = 0;
    public int slotRow = 0;
    //Used for rolling over once it hits max value;
    public final int slotColMax = 4;
    public final int slotRowMax = 3;
    public final int slotColMin = 0;
    public final int slotRowMin = 0;
    //Show specific item
    public boolean showItem = false;


    //Menu variables. If you add more options, Update these values
    public int commandNum = 0;
    public int commandMax = 2;
    public int commandMin = 0;


    //move to new location
    public String setClass = "";


    public int titleScreenState = 0; //0: is the first screen and 1: is the second screen


    public UI(GamePanel gp){
        this.gp = gp;


        try {
            InputStream is = getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf");
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);
            is = getClass().getResourceAsStream("/font/Purisa Bold.ttf");
            purisaBold = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //CREATE HUD OBJECTS
        Entity heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_quarter_missing = heart.image2;
        heart_half = heart.image3;
        heart_quarter = heart.image4;
        heart_empty = heart.image5;

    }

    public void addMessage(String text){

        message.add(text);
        messageCounter.add(0);

    }

    public void draw(Graphics2D g2){

        this.g2 = g2;

        g2.setFont((purisaBold));
        //g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        //g2.setFont((maruMonica));
        g2.setColor(Color.white);

        //Title state
        if(gp.gameState == gp.titleState){
            drawTitleScreen();
            if(titleScreenState == 2){

            }
        }

        //Play state
        if(gp.gameState == gp.playState){
            // Do play state things
            drawPlayerLife();
            drawMessage();
        }

        //Pause state
        if(gp.gameState == gp.pauseState){
            drawPauseScreen();
            drawPlayerLife();
        }

        //Dialog state
        if(gp.gameState == gp.dialogState){
            drawPlayerLife();
            drawDialogScreen();
        }
        //Character state
        if(gp.gameState == gp.characterState){
            drawCharacterScreen();
            drawInventory();
        }

    }

    public void drawInventory(){
        //Frame
        int frameX = gp.tileSize*9;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize*6;
        int frameHeight = gp.tileSize*5;
        int arc = 35;
        int alpha = 220;

        drawSubWindow(frameX,frameY,frameWidth,frameHeight,arc,alpha);

        //Slot
        final int padding = 20;
        final int slotXStart =  frameX + padding;
        final int slotYStart = frameY + padding;
        int slotX = slotXStart;
        int slotY = slotYStart;

        int slotSize = gp.tileSize+3;

        int perRow = slotColMax+1;


        // DRAW PLAYERS ITEMS
        for(int i = 0; i < gp.player.inventory.size(); i++){
            g2.drawImage(gp.player.inventory.get(i).down1, slotX, slotY, null);

            slotX += slotSize;
            if( (i+1) % perRow == 0){
                slotX = slotXStart;
                slotY += slotSize;
            }
        }

        //Cursor
        int cursorSize = 0;
        int cursorX = slotXStart+(slotSize*slotCol);
        int cursorY = slotYStart+(slotSize*slotRow);
        int cursorWidth = slotSize;
        int cursorHeight = slotSize;



        //Draw cursor

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(cursorSize));
        g2.drawRoundRect(cursorX,cursorY,cursorWidth,cursorHeight, 10, 10);

        if(showItem && gp.player.inventory.get(getIndex(slotRow,slotCol,slotColMax))!=null){
            drawShowItem(cursorX,cursorY);
        }

    }

    public void drawShowItem(int cursorX,int cursorY){
        //Draw frame
        int padding = 10;
        int showSize = 3;
        int showX = cursorX + gp.tileSize + padding;
        int showY = cursorY;
        int showWidth = gp.tileSize*5;
        int showHeight = gp.tileSize*5;
        int arc = 25;
        int alpha = 220;

        int max = slotRowMax;

        g2.setStroke(new BasicStroke(showSize));
        if((showX+showWidth)>(gp.tileSize*gp.maxScreenCol)){
            showX -= (showWidth+ gp.tileSize + padding*2);
        }
        drawSubWindow(showX,showY,showWidth,showHeight,arc,alpha);
        //GET ITEM INFO
        int index = getIndex(slotRow, slotCol, slotRowMax);

        //Populate text
        int textXPadding = 10;
        int textYPadding = 20;
        int textX = showX+textXPadding;
        int textY = showY+textYPadding;
        int textHeight = 22;
        String stickyText = "Name: ";

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20));
        g2.drawString(stickyText,textX,textY);

        textX = getXForCenteredText(stickyText,showX+padding,showWidth-textXPadding);

        g2.drawString(gp.player.inventory.get(index).name,textX,textY);

        stickyText = gp.player.inventory.get(index).description;
        textY += textHeight*2;
        textX = showX+textXPadding;
        textX = getXForCenteredText(stickyText,showX+padding,showWidth-textXPadding);

        g2.drawString(stickyText,textX,textY);
    }

    public static int getIndex(int row, int col, int maxCol) {
        return (row * maxCol) + col;
    }

    public void drawPlayerLife() {

        int x = gp.tileSize/2;
        int y = gp.tileSize/2;
        int i = 0;

        //Draw max life
        while(i < gp.player.maxLife/4){
            g2.drawImage(heart_empty, x, y, null);
            i++;
            x += gp.tileSize;
        }
        if(gp.player.maxLife%4 != 0){
            g2.drawImage(heart_empty, x, y, null);
        }

        x = gp.tileSize/2;
        y = gp.tileSize/2;
        i = 0;

        //Draw current life
        while(i < gp.player.life) {
            g2.drawImage(heart_quarter,x,y,null);
            i++;
            if(i < gp.player.life) {
                g2.drawImage(heart_half,x,y,null);
                i++;
                if(i < gp.player.life) {
                    g2.drawImage(heart_quarter_missing,x,y,null);
                    i++;
                    if(i < gp.player.life) {
                        g2.drawImage(heart_full,x,y,null);
                        i++;
                    }
                }
            }
            x += gp.tileSize;
        }
    }

    public void drawMessage(){

        int messageX = gp.tileSize;
        int messageY = gp.tileSize*4;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,32F));

        for(int i = 0; i < message.size(); i++){

            if(message.get(i) != null){
                g2.setColor(Color.black);
                g2.drawString(message.get(i),messageX+2,messageY+2);

                g2.setColor(Color.white);
                g2.drawString(message.get(i),messageX,messageY);

                int counter = messageCounter.get(i) + 1; // message counter ++
                messageCounter.set(i, counter); //Set the counter to the array
                messageY += 50;

                if(messageCounter.get(i) > 180){
                    message.remove(i);
                    messageCounter.remove(i);
                }
            }

        }

    }

    private void drawTitleScreen() {

        //Maybe split into 2 methods

        if(titleScreenState == 0){

            drawMainMenu();

        } else if(titleScreenState == 1){
            drawCharacterSelect();
        } else if(titleScreenState == 2){
            drawStatsWindow();
        }

    }

    public void drawMainMenu(){
        g2.setFont(maruMonica);
        g2.setColor(new Color(0,0,0));
        g2.fillRect(0,0 ,gp.screenWidth, gp.screenHeight);

        //Title Name
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,80f));
        String text = "Nah Bro, its a game";
        int x = getXForCenteredText(text);
        int y = gp.tileSize*3;

        //Shadow of text color
        g2.setColor(Color.WHITE);
        g2.drawString(text,x + 5,y + 5);

        //Main Text Color
        g2.setColor(Color.red);
        g2.drawString(text,x,y);

        // Background image
        x = gp.screenWidth/2 - (gp.tileSize*2)/2;
        y +=gp.tileSize*2;
        g2.drawImage(gp.player.down1, x, y, gp.tileSize*2, gp.tileSize*2, null);

        //MENU
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,48F));

        text = "NEW GAME";
        x = getXForCenteredText(text);
        y += gp.tileSize*4;
        g2.drawString(text, x, y);

        if(commandNum == 0){
            g2.drawString(">",x-gp.tileSize,y);
        }

        text = "LOAD GAME";
        x = getXForCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);

        if(commandNum == 1){
            g2.drawString(">",x-gp.tileSize,y);
        }

        text = "QUIT";
        x = getXForCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);

        if(commandNum == 2){
            g2.drawString(">",x-gp.tileSize,y);
        }
    }

    public void drawCharacterSelect(){
        g2.setColor(Color.red);
        g2.setFont(g2.getFont().deriveFont(42F));

        String text = "Select your class!";
        int x = getXForCenteredText(text);
        int y = gp.tileSize*3;
        g2.drawString(text,x,y);

        text = "Fighter";
        x = getXForCenteredText(text);
        y = gp.tileSize*4;
        g2.drawString(text,x,y);

        if(commandNum == 0){
            g2.drawString(">", x - gp.tileSize, y);
        }

        text = "Mage";
        x = getXForCenteredText(text);
        y = gp.tileSize*5;
        g2.drawString(text,x,y);

        if(commandNum == 1){
            g2.drawString(">", x - gp.tileSize, y);
        }

        text = "Archer";
        x = getXForCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text,x,y);

        if(commandNum == 2){
            g2.drawString(">", x - gp.tileSize, y);
        }

        text = "Tornado.....";//Joke, it's going to be a spell I couldn't think of a fourth class off the top of my head and 3 is enough to start with
        x = getXForCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text,x,y);

        if(commandNum == 3){
            g2.drawString(">", x - gp.tileSize, y);
        }

        text = "Back";//Joke, it's going to be a spell I couldn't think of a fourth class off the top of my head and 3 is enough to start with
        x = getXForCenteredText(text);
        y += gp.tileSize*2;
        g2.drawString(text,x,y);

        if(commandNum == 4){
            g2.drawString(">", x - gp.tileSize, y);
        }
    }
    //Implement more stats to affect. rn we only have health
    public void drawStatsWindow(){

        g2.setColor(Color.red);
        g2.setFont(g2.getFont().deriveFont(42F));

        String text = "stats:";
        int x = getXForCenteredText(text);
        int y = gp.tileSize*3;
        g2.drawString(text,x,y);

        text = "Health: ";
        x = (int)(getXForCenteredText(text)*.75);
        y = gp.tileSize*4;
        g2.drawString(text,x,y);

        x += (int)gp.tileSize*3.5;
        y -= (int)gp.tileSize*.75;

        int i = 0;

        //Draw current life
        while(i < gp.player.life) {
            g2.drawImage(heart_quarter,x,y,null);
            i++;
            if(i < gp.player.life) {
                g2.drawImage(heart_half,x,y,null);
                i++;
                if(i < gp.player.life) {
                    g2.drawImage(heart_quarter_missing,x,y,null);
                    i++;
                    if(i < gp.player.life) {
                        g2.drawImage(heart_full,x,y,null);
                        i++;
                    }
                }
            }
            x += gp.tileSize;
        }

        text = "Confirm";
        x = getXForCenteredText(text);
        y = gp.tileSize*6;
        g2.drawString(text,x,y);

        if(commandNum == 0){
            g2.drawString(">", x - gp.tileSize, y);
        }

        text = "Back";
        x = getXForCenteredText(text);
        y = gp.tileSize*7;
        g2.drawString(text,x,y);

        if(commandNum == 1){
            g2.drawString(">", x - gp.tileSize, y);
        }




    }

    private void drawDialogScreen() {

        // Window
        int x = gp.tileSize*2;
        int y = gp.tileSize/2;
        int width = gp.screenWidth - (gp.tileSize*4);
        int height = gp.tileSize*4;
        int arc = 35;
        int alpha = 220;


        drawSubWindow(x,y,width,height,arc,alpha);

        x += gp.tileSize;
        y += gp.tileSize;
        g2.setFont(purisaBold);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20));
        for(String line : currentDialog.split("\n")){
            g2.drawString(line, x, y);
            y+=40;
        }

    }

    public void drawCharacterScreen(){
        //Create a frame
        final int frameX = gp.tileSize;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.tileSize*5;
        final int frameHeight = gp.tileSize*10;
        final int arc = 35;
        final int alpha = 220;

        drawSubWindow(frameX,frameY,frameWidth,frameHeight,arc,alpha);

        //TEXT
        g2.setColor(Color.white);
        g2.setFont(maruMonica);
        g2.setFont(g2.getFont().deriveFont(32F));

        int textX = frameX + (gp.tileSize/2);
        int textY = frameY + gp.tileSize;
        final int lineHeight = 35;

        //Names
        g2.drawString("Level",textX,textY);
        textY += lineHeight;
        g2.drawString("Life",textX,textY);
        textY += lineHeight;
        g2.drawString("Strength",textX,textY);
        textY += lineHeight;
        g2.drawString("Dexterity",textX,textY);
        textY += lineHeight;
        g2.drawString("Attack",textX,textY);
        textY += lineHeight;
        g2.drawString("Defence",textX,textY);
        textY += lineHeight;
        g2.drawString("EXP",textX,textY);
        textY += lineHeight;
        g2.drawString("Next Level",textX,textY);
        textY += lineHeight;
        g2.drawString("Coin",textX,textY);
        textY += lineHeight+20;
        g2.drawString("Weapon",textX,textY);
        textY += lineHeight + 15;
        g2.drawString("Shield",textX,textY);

        //VALUES
        int tailX = (frameX + frameWidth) - 30;
        //reset text y
        textY = frameY + gp.tileSize;
        String value;

        value = String.valueOf(gp.player.level);
        textX = getXForAlignToRight(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        value = gp.player.life + "/" + gp.player.maxLife;
        textX = getXForAlignToRight(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.strength);
        textX = getXForAlignToRight(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.dexterity);
        textX = getXForAlignToRight(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.attack);
        textX = getXForAlignToRight(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.defense);
        textX = getXForAlignToRight(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.exp);
        textX = getXForAlignToRight(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.nextLevelExp);
        textX = getXForAlignToRight(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.coin);
        textX = getXForAlignToRight(value,tailX);
        g2.drawString(value,textX,textY);
        textY += lineHeight;

        g2.drawImage(gp.player.currentWeapon.down1,tailX - gp.tileSize, textY-14, null);
        textY += gp.tileSize;
        g2.drawImage(gp.player.currentShield.down1, tailX - gp.tileSize, textY-14, null);

    }

    public void drawSubWindow(int x, int y, int width, int height,int arc,int alpha){

        Color c = new Color(0,0,0, alpha);
        g2.setColor(c);
        g2.fillRoundRect(x,y,width,height, arc,arc);

        c = new Color(255,255,255, alpha);

        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x,y,width,height,arc,arc);
    }

    public void drawPauseScreen(){

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,80F));
        String text = "PAUSED";
        int x = getXForCenteredText(text);
        int y = gp.screenHeight/2;

        g2.drawString(text,x,y);
    }

    public int getXForCenteredText(String text){
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth/2 - length/2;
        return x;
    }

    public int getXForCenteredText(String text, int x, int xWidth){
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int r = x + (xWidth/2) - (length/2);
        return r;
    }

    public int getXForAlignToRight(String text, int tailX){
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = tailX-length;
        return x;
    }

}
