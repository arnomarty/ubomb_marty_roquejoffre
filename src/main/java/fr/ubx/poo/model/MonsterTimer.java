package fr.ubx.poo.model;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.model.go.character.Monster;

import java.util.Timer;
import java.util.TimerTask;

public class MonsterTimer extends TimerTask {

    Monster linkedMonster;

    public MonsterTimer(Monster m){
        this.linkedMonster = m;
    }

    @Override
    public void run() {
        linkedMonster.requestMove(Direction.random());


    }

}
