package fr.ubx.poo.model;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.view.sprite.SpritePlayer;

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
        if (linkedMonster != null) {
            linkedMonster.requestMove(Direction.random());
        }
        else if(linkedBomb != null){
            if (linkedBomb.getState() < 3) {
                System.out.println(linkedBomb.getState()+"\n");
                linkedBomb.increaseState(1);

            } else {
                linkedBomb.setExplode(true);
            }
        }
    }
}
