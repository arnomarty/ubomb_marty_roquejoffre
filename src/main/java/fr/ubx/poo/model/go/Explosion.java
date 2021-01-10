package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.GeneralTimer;

import java.util.Timer;
import java.util.TimerTask;

public class Explosion extends GameObject{

    private boolean show;
    private Timer timer;
    private TimerTask countdown;

    public Explosion(Game game, Position position){
        super(game,position);
        show=true;
        timer = new Timer();
        countdown= new GeneralTimer(this);
        timer.schedule(countdown, 500);
    }

    public boolean getStatus(){ return show; }
    public void setStatus(){ show = !show; }

    public void stop() {
        timer.cancel();
        timer.purge();
    }
}
