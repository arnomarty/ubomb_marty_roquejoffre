/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.GeneralTimer;
import fr.ubx.poo.model.InvulnerabilityTimer;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.decor.consumables.*;
import fr.ubx.poo.model.decor.doors.DoorNextOpened;
import fr.ubx.poo.model.decor.doors.DoorPrevOpened;
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.view.sprite.SpriteFactory;

import java.util.Timer;

public class Player extends GameObject implements Movable {

    private boolean alive = true;
    Direction direction;
    private boolean moveRequested = false;
    private int lives = 1;
    private int inventory = 3;
    private int range = 1;
    private int keys=0;
    private boolean winner;
    private boolean isInvulnerable;
    private Timer invulnerabilityCountdown;
    private InvulnerabilityTimer invulnerabilityTask;




    // ------------------ CONSTRUCTEUR ------------------ //

    public Player(Game game, Position position) {
        super(game, position);
        this.direction = Direction.S;
        this.lives = game.getInitPlayerLives();
        this.isInvulnerable = false;
    }





    // ------------------ ACCESSEURS ------------------ //

    public int getLives() {
        return lives;
    }
    public void setLives(int n){ this.lives = this.lives+n; }
    public Direction getDirection() {
        return direction;
    }
    public int getInventory(){ return inventory; }
    public void setInventory(int n){ this.inventory = this.inventory + n; }
    public int getRange(){ return range; }
    public void setRange(int n){ this.range = this.range + n; }
    public int getKeys(){ return keys; }
    public void setKeys(int n){ this.keys = this.keys+n; }
    public boolean getVulnerabilityStatus(){
        return this.isInvulnerable;
    }
    public void setVulnerabilityStatus(boolean status){
        this.isInvulnerable = status;
    }
    public boolean isWinner() { return winner; }
    public boolean isAlive() { return alive; }





    // ------------------ METHODES PUBLIQUES ------------------ //

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
        }
        moveRequested = true;
    }

    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Decor nextDecor = game.getWorld().get(nextPos);

        if(game.getWorld().isEmpty(nextPos) || nextDecor.consumable())
            return game.getWorld().isInside(nextPos);
        else if(nextDecor.movable()) {
            Position nextNextPos = direction.nextPosition(nextPos);
            if(game.getWorld().isEmpty(nextNextPos) && game.getWorld().isInside(nextNextPos)
                    && !game.monsterThere(nextNextPos)){
                game.getWorld().clear(nextPos);
                game.getWorld().set(nextNextPos, nextDecor);
                game.getWorld().setChanges(true);
                return true;
            }
        }
        return false;
    }

    @Override
    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Decor nextDecor = game.getWorld().get(nextPos);

        if( !game.getWorld().isEmpty(nextPos) && nextDecor.consumable()){
            switch(nextDecor.getID()){
                case 0:     // Key
                    this.setKeys(1);
                    break;
                case 1:     // Heart
                    this.setLives(1);
                    break;
                case 2:     // BombRangeInc
                    setRange(1);
                    break;
                case 3:     // BombRangeDec
                    setRange(-1);
                    break;
                case 4:     // BombNumberInc
                    setInventory(1);
                    break;
                case 5:     // BombNumberDec
                    setInventory(-1);
                    break;
                case 6:     // DoorNextOpened
                    game.changeFloor(1);
                    break;
                case 7:     // DoorPrevOpened
                    game.changeFloor(-1);
                    break;
                case 8:     // Princess
                    winner = true;
                    break;
            }

            this.game.getWorld().clear(nextPos);
            game.getWorld().setChanges(true);
        }
        setPosition(nextPos);
    }

    public void getHit(){
        if(!isInvulnerable) {
            lives = lives - 1;
            this.alive = lives > 0;
            if(alive) {
                isInvulnerable = true;
                invulnerabilityCountdown = new Timer();
                invulnerabilityTask = new InvulnerabilityTimer(this);
                invulnerabilityCountdown.schedule(invulnerabilityTask, 1000);
            }
        }
    }

    public void update(long now) {
        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);
            }
        }
        moveRequested = false;
    }

    public void endTimer(){
        invulnerabilityCountdown.cancel();
        invulnerabilityCountdown.purge();
    }

}
