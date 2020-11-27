package fr.ubx.poo.model.go;

import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.MonsterTimer;

import java.util.Timer;
import java.util.TimerTask;

public class Bomb extends GameObject {

    int state;
    Timer timer;
    TimerTask countdown;

    public Bomb(Game game, Position position) {
        super(game, position);
        timer = new Timer();
        //*moveHandler = new MonsterTimer(this);
        //timer.scheduleAtFixedRate(() -> requestMove(Direction.random()), 1000, 1000);
        //timer.scheduleAtFixedRate(moveHandler, 1000, 400 );
    }
}
