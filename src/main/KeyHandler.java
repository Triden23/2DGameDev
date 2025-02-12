package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed;

    public boolean spellOnePressed, spellTwoPressed;

    // DEBUG
    public boolean showDebugText = false;
    public KeyHandler(GamePanel gp){
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //NOT USED
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        //Title State
        if(gp.gameState == gp.titleState){
            titleState(code);
        }
        //Play State
        else if(gp.gameState == gp.playState){
            playState(code);
        }
        //PAUSE STATE
        else if(gp.gameState == gp.pauseState){
            pauseState(code);
        }
        //DIALOG STATE
        else if(gp.gameState == gp.dialogState){
            dialogState(code);
        }
        //CHARACTER STATE
        else if(gp.gameState == gp.characterState){
            characterState(code);
        }


    }

    public void titleState(int code){

        if(gp.ui.titleScreenState == 0){
            mainMenu(code);
        }
        else if(gp.ui.titleScreenState == 1){
            characterSelect(code);
        }
        else if(gp.ui.titleScreenState == 2) {
            confirmCharacterSelect(code);
        }

    }

    public void playState(int code){
        if(code == KeyEvent.VK_W){
            upPressed = true;
        }
        if(code == KeyEvent.VK_S){
            downPressed = true;
        }
        if(code == KeyEvent.VK_A){
            leftPressed = true;
        }
        if(code == KeyEvent.VK_D){
            rightPressed = true;
        }
        if(code == KeyEvent.VK_ESCAPE){
            gp.gameState = gp.pauseState;
        }
        if(code == KeyEvent.VK_C){
            gp.gameState = gp.characterState;
        }
        if(code == KeyEvent.VK_ENTER){
            enterPressed = true;
        }
        if(code == KeyEvent.VK_Q){
            spellOnePressed = true;
        }
        if(code == KeyEvent.VK_E){
            spellTwoPressed = true;
        }

        // DEBUG
        if(code == KeyEvent.VK_T){
            if(!showDebugText){
                showDebugText = true;
            }else{
                if(showDebugText){
                    showDebugText = false;
                }
            }
        }

        if(code == KeyEvent.VK_R){
            String debug_Map = "/maps/world01.txt";
            gp.tileM.loadMap(debug_Map);
        }
    }

    public void pauseState(int code){
        if(code == KeyEvent.VK_ESCAPE){
            gp.gameState = gp.playState;
        }
    }

    public void dialogState(int code){
        if(code == KeyEvent.VK_ENTER){
            gp.gameState = gp.playState;
        }
    }

    public void characterState(int code){
        if(code == KeyEvent.VK_C){
            gp.gameState = gp.playState;
        }
        if(code == KeyEvent.VK_W){
            gp.ui.slotRow--;
            gp.playSE(9);
            if(gp.ui.slotRowMin>gp.ui.slotRow){
                gp.ui.slotRow = gp.ui.slotRowMax;
            }
            gp.ui.showItem = false;
        }
        if(code == KeyEvent.VK_A){
            gp.ui.slotCol--;
            gp.playSE(9);
            if(gp.ui.slotColMin>gp.ui.slotCol){
                gp.ui.slotCol = gp.ui.slotColMax;
            }
            gp.ui.showItem = false;
        }
        if(code == KeyEvent.VK_S){
            gp.ui.slotRow++;
            gp.playSE(9);
            if(gp.ui.slotRow > gp.ui.slotRowMax){
                gp.ui.slotRow = gp.ui.slotRowMin;
            }
            gp.ui.showItem = false;
        }
        if(code == KeyEvent.VK_D){
            gp.ui.slotCol++;
            gp.playSE(9);
            if(gp.ui.slotCol > gp.ui.slotColMax){
                gp.ui.slotCol = gp.ui.slotColMin;
            }
            gp.ui.showItem = false;
        }
        if(code == KeyEvent.VK_ENTER){
            gp.ui.showItem = true;
        }
    }

    public void mainMenu(int code){
        gp.ui.commandMax=2;

        if(code == KeyEvent.VK_W){
            if(gp.ui.commandNum > gp.ui.commandMin) {
                gp.ui.commandNum--;
            }else{
                gp.ui.commandNum = gp.ui.commandMax;
            }
        }
        if(code == KeyEvent.VK_S){
            if(gp.ui.commandNum < gp.ui.commandMax) {
                gp.ui.commandNum++;
            }else{
                gp.ui.commandNum = gp.ui.commandMin;
            }
        }
        if(code == KeyEvent.VK_ENTER){

            //New game
            if(gp.ui.commandNum == 0){
                gp.ui.titleScreenState = 1;
                gp.playMusic(0);
            }

            //Load game
            if(gp.ui.commandNum == 1){
                //Add later
            }

            //Quit game
            if(gp.ui.commandNum == 2){
                System.exit(0);
            }

        }
    }

    public void characterSelect(int code){
        //Change this to be the max amount of menu options
        gp.ui.commandMax = 4;
        //Do things for a sub menu or something. maybe character creation
        if(code == KeyEvent.VK_W){
            if(gp.ui.commandNum > gp.ui.commandMin) {
                gp.ui.commandNum--;
            }else{
                //Loops the selection around
                gp.ui.commandNum = gp.ui.commandMax;
            }
        }
        if(code == KeyEvent.VK_S){

            if(gp.ui.commandNum < gp.ui.commandMax) {
                gp.ui.commandNum++;
            }else{
                //loops the selection around
                gp.ui.commandNum = gp.ui.commandMin;
            }
        }
        if(code == KeyEvent.VK_ENTER) {

            //Fighter
            if (gp.ui.commandNum == 0) {
                //Set the class to fighter
                gp.ui.setClass = "Fighter";
                gp.player.setUpClass(gp.ui.setClass);
                gp.ui.commandNum = 0;
                gp.ui.titleScreenState = 2;
            }
            //Mage
            if (gp.ui.commandNum == 1) {
                //Set the class to mage
                gp.ui.setClass = "Mage";
                gp.player.setUpClass(gp.ui.setClass);
                gp.ui.commandNum = 0;
                gp.ui.titleScreenState = 2;
            }

            //Archer
            if (gp.ui.commandNum == 2) {
                //Set the class to archer
                gp.ui.setClass = "Archer";
                gp.player.setUpClass(gp.ui.setClass);
                gp.ui.commandNum = 0;
                gp.ui.titleScreenState = 2;
            }

            //Tornado....
            if (gp.ui.commandNum == 3) {
                //Switch the character model to the tornado, only gets one spell that cast mini tornado's and does aoe around them. Cannot die
                gp.ui.setClass = "Tornado";
                gp.player.setUpClass(gp.ui.setClass);
                gp.ui.commandNum = 0;
                gp.ui.titleScreenState = 2;
            }

            //Back
            if(gp.ui.commandNum == 4){
                //Returns you to the previous menu
                gp.ui.titleScreenState = 0;
                gp.ui.commandNum = 0;
            }
        }
    }

    public void confirmCharacterSelect(int code){
        //Change this to be the max amount of menu options
        gp.ui.commandMax = 1;
        //Do things for a sub menu or something. maybe character creation
        if (code == KeyEvent.VK_W) {
            if (gp.ui.commandNum > gp.ui.commandMin) {
                gp.ui.commandNum--;
            } else {
                //Loops the selection around
                gp.ui.commandNum = gp.ui.commandMax;
            }
        }
        if (code == KeyEvent.VK_S) {

            if (gp.ui.commandNum < gp.ui.commandMax) {
                gp.ui.commandNum++;
            } else {
                //loops the selection around
                gp.ui.commandNum = gp.ui.commandMin;
            }
        }
        if (code == KeyEvent.VK_ENTER) {

            //Confirm
            if (gp.ui.commandNum == 0) {
                //Set the class to fighter
                gp.gameState = gp.playState;
            }
            //Back
            if (gp.ui.commandNum == 1) {
                //Set the class to mage
                gp.ui.titleScreenState = 1;
                gp.ui.commandNum = 0;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();

        if(code == KeyEvent.VK_W){
            upPressed = false;
        }
        if(code == KeyEvent.VK_S){
            downPressed = false;
        }
        if(code == KeyEvent.VK_A){
            leftPressed = false;
        }
        if(code == KeyEvent.VK_D){
            rightPressed = false;
        }

    }



}
