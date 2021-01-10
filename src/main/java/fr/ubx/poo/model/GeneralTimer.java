package fr.ubx.poo.model;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.Explosion;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.view.sprite.SpritePlayer;

import java.util.Timer;
import java.util.TimerTask;

public class GeneralTimer extends TimerTask {

    Monster linkedMonster = null;
    Bomb linkedBomb = null;
    Explosion linkedExplosion = null;

    boolean interruptor = false;




    // ------------------ CONSTRUCTEUR ------------------ //

    public GeneralTimer(Monster m){
        this.linkedMonster = m;
    }

    public GeneralTimer(Bomb b){
        this.linkedBomb = b;
    }

    public GeneralTimer(Explosion e){
        this.linkedExplosion = e;
    }





    // ------------------ METHODES PUBLIQUES ------------------ //

    @Override
    public void run() {
        if (linkedMonster != null) {
            linkedMonster.requestMove();
        }
        else if(linkedExplosion != null){
            linkedExplosion.setStatus();
        }
        else if(linkedBomb != null && interruptor){
            System.out.println(linkedBomb.getState() + "\n");
            if (linkedBomb.getState() < 3) {
                linkedBomb.increaseState(1);
            } else {
                linkedBomb.setExplode(true);
            }
            interruptor = (linkedBomb.getState() == 4);
        }
        else{
            interruptor = true;
        }
    }
}
