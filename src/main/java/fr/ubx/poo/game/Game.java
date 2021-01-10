/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;


import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.Explosion;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.model.go.character.Player;

public class Game {

    private final List<World> worlds = new ArrayList<>();
    private final Player player;
    private List<Bomb> bombs = new ArrayList<>();
    private final String worldPath;
    private String prefix;
    private int numberOfLevels;
    private int currentFloor;
    public int switchedFloor;
    public int initPlayerLives;




    // ------------------ CONSTRUCTEUR ------------------ //

    public Game(String worldPath) {
        this.worldPath = worldPath;
        currentFloor = 0;
        switchedFloor = 0;

        loadConfig(worldPath);
        loadWorld(worldPath);
        Position positionPlayer = null;
        List<Position> positionMonsters = new ArrayList<>();

        try {
            positionPlayer = worlds.get(currentFloor).findPlayer();
            player = new Player(this, positionPlayer);

            for(int i=0; i<numberOfLevels; i++) {
                positionMonsters = worlds.get(i).findMonsters();
                for( Position pm : positionMonsters){
                    worlds.get(i).monsters.add(new Monster(this, pm));
                }
            }

        } catch (PositionNotFoundException e) {
            System.err.println("Position not found : " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }





    // ------------------ ACCESSEURS ------------------ //

    public int getInitPlayerLives() { return initPlayerLives; }
    public World getWorld() { return worlds.get(currentFloor); }
    public Player getPlayer() { return this.player; }
    public List<Monster> getMonsters(){ return this.getWorld().monsters; };
    public int getFloor(){ return this.currentFloor+1; }
    public List<Explosion> getExplosions(){return this.getWorld().explosions; }





    // ------------------ METHODES PUBLIQUES ------------------ //

    public void changeFloor(int n){
        currentFloor= currentFloor+n;
        System.out.println("Direction: " + n);
        switchedFloor = n;
    }

    public boolean monsterThere(Position position){
        for( Monster m : getWorld().monsters){
            if(m.getPosition().equals(position)){
                return true;
            }
        }
        return false;
    }

    public boolean bombThere(Position position){
        for(Bomb b : getWorld().bombs){
            if(b.getPosition().equals(position)){
                return true;
            }
        }
        return false;
    }





    // ------------------ METHODES INTERNES ------------------ //

    private void loadConfig(String path) {
        try (InputStream input = new FileInputStream(new File(path, "config.properties"))) {
            // Load the configuration file
            Properties prop = new Properties();
            prop.load(input);

            // Save parameters
            initPlayerLives = Integer.parseInt(prop.getProperty("lives", "3"));
            prefix = prop.getProperty("prefix", "default");
            numberOfLevels = Integer.parseInt(prop.getProperty("levels", "1"));

        } catch (IOException ex) {
            System.err.println("Error loading configuration");
        }
    }


    private World loadWorld(String path){
        try{
            for(int k=0; k<numberOfLevels; k++) {
                List<String> raw = Files.readAllLines(Paths.get(path + "/" + prefix + (k+1) + ".txt"));
                WorldEntity[][] grid = new WorldEntity[raw.size()][raw.get(0).length()];
                for (int j = 0; j < raw.size(); j++) {
                    for (int i = 0; i < raw.get(0).length(); i++) {
                        grid[j][i] = WorldEntity.fromCode(raw.get(j).toCharArray()[i]).orElse(WorldEntity.Empty);
                    }
                }
                worlds.add(new World(grid));
                System.out.println(prefix + getFloor() + ".txt loaded!");
            }

        }catch(IOException ex){
            System.out.println("oupsi");
        }
        return new WorldStatic();
    }
}
