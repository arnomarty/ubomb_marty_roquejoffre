package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.GeneralTimer;
import fr.ubx.poo.model.decor.Box;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.consumables.*;

import java.util.Timer;
import java.util.TimerTask;

public class Bomb extends GameObject {

    private int state;
    private int range;
    private Timer timer;
    private TimerTask countdown;
    private boolean explode;

    public Bomb(Game game, Position position) {
        super(game, position);
        System.out.println(position);
        state = 0;
        range = 1;
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

    public void explode(Direction direction){
        Position pos = getPosition();
        for(int i=0; i<range; i++){
            pos = direction.nextPosition(pos);
            System.out.println(pos + "\n");
//A OPTIMISER!
            if(game.getWorld().get(pos) instanceof Box || game.getWorld().get(pos) instanceof Heart
             || game.getWorld().get(pos) instanceof BombNumberDec || game.getWorld().get(pos) instanceof BombNumberInc
             || game.getWorld().get(pos) instanceof BombRangeDec || game.getWorld().get(pos) instanceof BombRangeInc){
                game.getWorld().clear(pos);
                game.getWorld().setChanges(true);
                return;
            }
            else if(game.getPlayer().getPosition().equals(pos)){
                game.getPlayer().getHit();
            }
        }
    }

    public void stop(){
        explode(Direction.N);
        explode(Direction.S);
        explode(Direction.E);
        explode(Direction.W);
        timer.cancel();
        timer.purge();

    }

}
