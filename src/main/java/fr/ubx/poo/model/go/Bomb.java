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

    private int state;
    private int range;
    private Timer timer;
    private TimerTask countdown;
    private boolean explode;




    // ------------------ CONSTRUCTEUR ------------------ //

    public Bomb(Game game, Position position) {
        super(game, position);
        state = 0;
        range = game.getPlayer().getRange();
        explode = false;
        timer = new Timer();
        countdown= new GeneralTimer(this);
        timer.scheduleAtFixedRate(countdown, 500, 500 );
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

    public void explode(Direction direction){
        int i=0;
        Position pos= getPosition();

        Decor decor;
        do{
            decor = game.getWorld().get(pos);
            if(!game.getWorld().isEmpty(pos) && !decor.destroyable()){
                return;
            }
            game.getWorld().explosions.add(new Explosion(game,pos));

            if(game.getPlayer().getPosition().equals(pos))
                game.getPlayer().getHit();
            if( !game.getWorld().isEmpty(pos) && decor.destroyable() ){
                game.getWorld().clear(pos);
                game.getWorld().setChanges(true);
                return;
            }
            for (Monster m : game.getMonsters()) {
                if (m.getPosition().equals(pos))
                    m.kill();
            }
            i++;
            pos = direction.nextPosition(pos);
        }while(i <= range);
    }

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
