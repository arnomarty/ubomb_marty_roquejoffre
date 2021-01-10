package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.GeneralTimer;
import fr.ubx.poo.model.decor.Box;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.decor.consumables.*;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.view.sprite.SpriteFactory;

import java.util.Timer;
import java.util.TimerTask;

public class Bomb extends GameObject {

    private int state;      // Vary between 0 and 3. Represent the bomb's current state before explosion
    private int range;
    private Timer timer;             // Timer used for bomb's burning
    private TimerTask countdown;    //  ^
    private boolean explode;




    // ------------------ CONSTRUCTEUR ------------------ //

    public Bomb(Game game, Position position) {
        super(game, position);
        state = 0;
        range = game.getPlayer().getRange();
        explode = false;
        timer = new Timer();
        countdown= new GeneralTimer(this);
        timer.scheduleAtFixedRate(countdown, 500, 500 );    // Timer task runs every 0,5sec
    }





    // ------------------ ACCESSEURS ------------------ //

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





    // ------------------ METHODES PUBLIQUES ------------------ //


      // Handles bomb's explosion in a *single* direction. It will destroy every destroyable
     // entity on (range) cells from the bomb, and initiate the adequate explosion effect.
    public void explode(Direction direction){
        int i=0;
        Position pos= getPosition();
        Decor decor;

        do{
            decor = game.getWorld().get(pos);
              // If cell is out of frame, not empty and not destroyable, quit loop
            if(!game.getWorld().isInside(pos) || !game.getWorld().isEmpty(pos) && !decor.destroyable()){
                return;
            }
              // Creates explosion animation
            game.getWorld().explosions.add(new Explosion(game,pos));

            if(game.getPlayer().getPosition().equals(pos))      // If player on the explosion's path...
                game.getPlayer().getHit();
            if( !game.getWorld().isEmpty(pos) && decor.destroyable() ){     // If cell is a destroyable entity...
                game.getWorld().clear(pos);
                game.getWorld().setChanges(true);
                return;     // Leave the loop!
            }
            for (Monster m : game.getMonsters()) {      // If a monster is on the explosion's path...
                if (m.getPosition().equals(pos))
                    m.kill();
            }
            i++;
            pos = direction.nextPosition(pos);      // Next cell
        }while(i <= range);
    }


    // Destroys timer and launch explosion sequence in the four cardinal directions
    public void stop(){
        explode(Direction.N);
        explode(Direction.S);
        explode(Direction.E);
        explode(Direction.W);
        game.getPlayer().setInventory(1);
        timer.cancel();
        timer.purge();
    }

}
