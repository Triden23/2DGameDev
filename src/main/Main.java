package main;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        //2000 lines of code
        //java -jar D:\java projects\Game\src\main\Main.java
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("2D Adventure");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.setupGame();
        gamePanel.startGameThread();

    }
    /*
    I have hit detection, Collision detection, Entity detection, Events , event detection. Player movement, camera control, Loadable map.
    Objects, npc, monsters, player

    What I need is lighting, Loading player progress, Game states  or other stats that effect the player

    A way to load events, monsters, objects and npc's from a file

    Projectile management system

     */

}
