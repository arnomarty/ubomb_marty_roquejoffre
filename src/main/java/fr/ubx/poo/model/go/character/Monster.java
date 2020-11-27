package fr.ubx.poo.model.go.character;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.GeneralTimer;
import fr.ubx.poo.model.Movable;
import fr.ubx.poo.model.decor.Decor;
import fr.ubx.poo.model.go.GameObject;

import java.util.Timer;
import java.util.TimerTask;

public class Monster extends GameObject implements Movable {

    private boolean alive = true;
    Direction direction;
    private boolean moveRequested = false;
    private int lives = 1;
    Timer timer;
    TimerTask moveHandler;

    public Monster(Game game, Position position) {
        super(game, position);
        this.direction = Direction.S;
        this.lives = game.getInitPlayerLives();

        timer = new Timer();
        moveHandler = new GeneralTimer(this);
        //timer.scheduleAtFixedRate(() -> requestMove(Direction.random()), 1000, 1000);
        timer.scheduleAtFixedRate(moveHandler, 1000, 1500 );
    }

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
        }
        moveRequested = true;
    }

    @Override
    public boolean canMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        Decor nextDecor = game.getWorld().get(nextPos);

        return (game.getWorld()).isInside(nextPos)
                && !(nextDecor instanceof Decor)
                && !(game.monsterThere(nextPos));

    }

    @Override
    public void doMove(Direction direction) {
            Position nextPos = direction.nextPosition(getPosition());
            setPosition(nextPos);
    }

    public int getLives() {
        return lives;
    }

    public void update(long now) {
        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);
            }
        }
        if(game.getPlayer().getPosition().equals(this.getPosition())){
            game.getPlayer().getHit();
        }
        moveRequested = false;
    }

    public boolean isAlive() { return alive; }
    public Direction getDirection() {
        return direction;
    }

}
