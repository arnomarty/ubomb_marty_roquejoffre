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
import fr.ubx.poo.model.go.GameObject;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.view.sprite.SpriteFactory;

import java.util.Timer;

public class Player extends GameObject implements Movable {

    private boolean alive = true;
    Direction direction;
    private boolean moveRequested = false;
    private int lives = 1;
    private boolean winner;

    private boolean isInvulnerable;
    private Timer invulnerabilityCountdown;
    private InvulnerabilityTimer invulnerabilityTask;

    public Player(Game game, Position position) {
        super(game, position);
        this.direction = Direction.S;
        this.lives = game.getInitPlayerLives();
        this.isInvulnerable = false;
    }

    public int getLives() {
        return lives;
    }
    public boolean getVulnerabilityStatus(){
        return this.isInvulnerable;
    }

    public void setVulnerabilityStatus(boolean status){
        this.isInvulnerable = status;
    }

    public void getHit(){
        if(!isInvulnerable) {
            lives = lives - 1;
            this.alive = lives > 0;
            isInvulnerable = true;
            invulnerabilityCountdown = new Timer();
            invulnerabilityTask = new InvulnerabilityTimer(this);
            invulnerabilityCountdown.schedule(invulnerabilityTask, 1000);
        }
    }

    public Direction getDirection() {
        return direction;
    }

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

        if( !(nextDecor instanceof Box) ){
            return (game.getWorld()).isInside(nextPos)
                 && !(nextDecor instanceof Stone)
                 && !(nextDecor instanceof Tree);
        }
        else{
            Position nextNextPos = direction.nextPosition(nextPos);
            Decor nextNextDecor = game.getWorld().get(nextNextPos);

            if(nextNextDecor == null && game.getWorld().isInside(nextNextPos)
                && !game.monsterThere(nextNextPos)){

                game.getWorld().clear(nextPos);
                game.getWorld().set(nextNextPos, nextDecor);
                game.getWorld().setChanges(true);
                return true;
            }
        }
        return false;
    }

    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
    }

    public void update(long now) {
        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);
            }
        }
        moveRequested = false;
    }

    public boolean isWinner() {
        return winner;
    }

    public boolean isAlive() {
        return alive;
    }

    public void endTimer(){
        invulnerabilityCountdown.cancel();
        invulnerabilityCountdown.purge();
    }

}
