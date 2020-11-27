/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.model.go.character.Player;

public class Game {

    private final World world;
    private final Player player;
//MONSTERSREQ
    private List<Monster> monsters= new ArrayList<>();

    private final String worldPath;
    public int initPlayerLives;

    public Game(String worldPath) {
        world = new WorldStatic();
        this.worldPath = worldPath;
        loadConfig(worldPath);
        Position positionPlayer = null;
//MONSTERSREQ
        List<Position> positionMonsters = new ArrayList<>();
        try {
            positionPlayer = world.findPlayer();
            player = new Player(this, positionPlayer);

            positionMonsters = world.findMonsters();
            positionMonsters.forEach( pm -> monsters.add( new Monster(this, pm)) );
//END
        } catch (PositionNotFoundException e) {
            System.err.println("Position not found : " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    public int getInitPlayerLives() {
        return initPlayerLives;
    }

    private void loadConfig(String path) {
        try (InputStream input = new FileInputStream(new File(path, "config.properties"))) {
            Properties prop = new Properties();
            // load the configuration file
            prop.load(input);
            initPlayerLives = Integer.parseInt(prop.getProperty("lives", "3"));
        } catch (IOException ex) {
            System.err.println("Error loading configuration");
        }
    }

    public World getWorld() {
        return world;
    }

    public Player getPlayer() {
        return this.player;
    }

    public List<Monster> getMonsters(){ return this.monsters; };

    public boolean monsterThere(Position position){
        for( Monster m : monsters){
            if(m.getPosition().equals(position)){
                return true;
            }
        }
        return false;
    }


}
