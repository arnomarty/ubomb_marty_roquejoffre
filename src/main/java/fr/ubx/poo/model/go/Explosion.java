package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.GeneralTimer;

import java.util.Timer;
import java.util.TimerTask;

public class Explosion extends GameObject{

    private boolean show;   // Determines if the animation should be visible. Otherwise, it will get destroyed.
    private Timer timer;
    private TimerTask countdown;




    public Explosion(Game game, Position position){
        super(game,position);
        show=true;
        timer = new Timer();
        countdown= new GeneralTimer(this);
        timer.schedule(countdown, 400);     // Will launch the Timer Task after 0,4sec
    }





    public boolean getStatus(){ return show; }   // Called by GameEngine::checkBombs
    public void setStatus(){ show = !show; }    // Called by the Timer Task.





    public void stop() {    // Clears the Timer
        timer.cancel();
        timer.purge();
    }
}
