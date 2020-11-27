package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.GeneralTimer;

import java.util.Timer;
import java.util.TimerTask;

public class Bomb extends GameObject {

    private int state;
    private Timer timer;
    private TimerTask countdown;
    private boolean explode;

    public Bomb(Game game, Position position) {
        super(game, position);
        int state = 0;
        explode = false;

        timer = new Timer();
        countdown= new GeneralTimer(this);
        timer.scheduleAtFixedRate(countdown, 1000, 1000 );
    }

    public int getState() {
        return state;
    }

    public void increaseState(int inc) {
        this.state = this.state + inc;
    }

    public boolean explosionStatus(){
        return explode;
    }
    public void setExplode(boolean b){
        explode = b;
    }

    public void stop(){
        timer.cancel();
        timer.purge();
    }

}
