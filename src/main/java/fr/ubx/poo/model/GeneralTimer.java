package fr.ubx.poo.model;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.character.Monster;

import java.util.Timer;
import java.util.TimerTask;

public class GeneralTimer extends TimerTask {

    Monster linkedMonster;
    Bomb linkedBomb;

    public GeneralTimer(Monster m){
        this.linkedMonster = m;
        this.linkedBomb = null;
    }
    public GeneralTimer(Bomb b){
        this.linkedBomb = b;
        this.linkedMonster = null;
    }

    @Override
    public void run() {
        if (linkedBomb == null) {
            linkedMonster.requestMove(Direction.random());
        } else {
            if (linkedBomb.getState() < 3) {
                System.out.println(linkedBomb.getState()+"\n");
                linkedBomb.increaseState(1);

            } else {
                linkedBomb.setExplode(true);
            }

        }
    }
}
