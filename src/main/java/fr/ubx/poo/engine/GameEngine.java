/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.engine;

import fr.ubx.poo.game.Direction;
import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.doors.DoorNextClosed;
import fr.ubx.poo.model.decor.doors.DoorNextOpened;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.Explosion;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.view.sprite.Sprite;
import fr.ubx.poo.view.sprite.SpriteFactory;
import fr.ubx.poo.game.Game;
import fr.ubx.poo.model.go.character.Player;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static fr.ubx.poo.game.Direction.random;


public final class GameEngine {

    private static AnimationTimer gameLoop;
    private final String windowTitle;
    private final Game game;
    private final Player player;
    private final List<Sprite> sprites = new ArrayList<>();
    private StatusBar statusBar;
    private Pane layer;
    private Input input;
    private Stage stage;
    private Sprite spritePlayer;
    private List<Sprite> spriteMonsters = new ArrayList<>();
    private List<Sprite> spriteBombs = new ArrayList<>();
    private List<Sprite> spriteExplosion = new ArrayList<>();




    // ------------------ CONSTRUCTEUR ------------------ //

    public GameEngine(final String windowTitle, Game game, final Stage stage) {
        this.windowTitle = windowTitle;
        this.game = game;
        this.player = game.getPlayer();
        game.getWorld().monsters = game.getMonsters();
        initialize(stage, game);
        buildAndSetGameLoop();
    }





    // ------------------ METHODES PUBLIQUES ------------------ //

    public void start() { gameLoop.start(); }

    public void checkBombs(){
        Iterator<Bomb> iter = this.game.getWorld().bombs.iterator();
        while(iter.hasNext()){
            Bomb b = iter.next();
            if(b.explosionStatus()){
                iter.remove();
                b.stop();
                this.game.getWorld().setChanges(true);
            }
        }
    }

    public void checkExplosions(){
        Iterator<Explosion> iter = this.game.getExplosions().iterator();
        while(iter.hasNext()){
            Explosion e = iter.next();
            if(!e.getStatus()) {
                e.stop();
                iter.remove();
                game.getWorld().setChanges(true);
            }
        }
    }





    // ------------------ METHODES INTERNES ------------------ //

    private void initialize(Stage stage, Game game) {
        this.stage = stage;
        Group root = new Group();
        layer = new Pane();
        int height = game.getWorld().dimension.height;
        int width = game.getWorld().dimension.width;
        int sceneWidth = width * Sprite.size;
        int sceneHeight = height * Sprite.size;
        Scene scene = new Scene(root, sceneWidth, sceneHeight + StatusBar.height);
        scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());

        stage.setTitle(windowTitle);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        input = new Input(scene);
        root.getChildren().add(layer);
        statusBar = new StatusBar(root, sceneWidth, sceneHeight, game);
        // Create decor sprites
        game.getWorld().forEach( (pos,d) -> sprites.add(SpriteFactory.createDecor(layer, pos, d)));
        spritePlayer = SpriteFactory.createPlayer(layer, player);
        game.getWorld().monsters.forEach( m -> spriteMonsters.add(SpriteFactory.createMonster(layer, m)) );
    }

    protected final void buildAndSetGameLoop() {
        gameLoop = new AnimationTimer() {
            public void handle(long now) {
                // Check keyboard actions
                processInput(now);

                // Do actions
                update(now);

                // Graphic update
                render();
                statusBar.update(game);
            }
        };
    }

    private void processInput(long now) {
        if (input.isExit()) {
            gameLoop.stop();
            Platform.exit();
            System.exit(0);
        }
        if (input.isMoveDown()) {
            player.requestMove(Direction.S);
        }
        if (input.isMoveLeft()) {
            player.requestMove(Direction.W);
        }
        if (input.isMoveRight()) {
            player.requestMove(Direction.E);
        }
        if (input.isMoveUp()) {
            player.requestMove(Direction.N);
        }
        if(input.isBomb() && player.getInventory() > 0 && !game.bombThere(player.getPosition())){
            game.getWorld().bombs.add(new Bomb(game, player.getPosition()));
            this.player.setInventory(-1);
            statusBar.update(game);
            game.getWorld().bombs.forEach( b -> spriteBombs.add(SpriteFactory.createBomb(layer, b)) );
        }
        if(input.isKey() && game.getPlayer().getKeys() > 0){
            System.out.println("Trying to use key!");
            Position facing = player.getDirection().nextPosition(player.getPosition());
            if(game.getWorld().get(facing) instanceof DoorNextClosed){
                System.out.println("Opened the door!");
                game.getWorld().clear(facing);
                game.getWorld().set(facing, new DoorNextOpened());
                player.setKeys(-1);
                game.getWorld().setChanges(true);
            }
        }
        input.clear();
    }

    private void showMessage(String msg, Color color) {
        Text waitingForKey = new Text(msg);
        waitingForKey.setTextAlignment(TextAlignment.CENTER);
        waitingForKey.setFont(new Font(60));
        waitingForKey.setFill(color);
        StackPane root = new StackPane();
        root.getChildren().add(waitingForKey);
        Scene scene = new Scene(root, 400, 200, Color.WHITE);
        stage.setTitle(windowTitle);
        stage.setScene(scene);
        input = new Input(scene);
        stage.show();
        new AnimationTimer() {
            public void handle(long now) {
                processInput(now);
            }
        }.start();
    }


    private void update(long now) {

        if(game.switchedFloor != 0){
            System.out.println("switched floor: " + game.switchedFloor);
            player.setPosition(game.getWorld().findEntry(game.switchedFloor));
            initialize(stage, game);
            game.getWorld().setChanges(true);
            game.switchedFloor = 0;
        }
        player.update(now);
        game.getWorld().monsters.forEach( m -> m.update(now));
        checkBombs();
        checkExplosions();

        if (player.isAlive() == false) {
            gameLoop.stop();
            showMessage("Perdu!", Color.RED);
        }
        if (player.isWinner()) {
            gameLoop.stop();
            showMessage("Gagné", Color.BLUE);
        }
        game.getWorld().monsters.removeIf( m -> m.isAlive() == false);

        if(game.getWorld().getChanges()){
            sprites.forEach(Sprite::remove);
            sprites.clear();
            spriteBombs.forEach(Sprite::remove);
            spriteBombs.clear();
            spriteMonsters.forEach(Sprite::remove);
            spriteMonsters.clear();
            spriteExplosion.forEach(Sprite::remove);
            spriteExplosion.clear();

            game.getWorld().forEach( (pos,d) -> sprites.add(SpriteFactory.createDecor(this.layer, pos, d)));
            game.getWorld().monsters.forEach( m -> spriteMonsters.add(SpriteFactory.createMonster(layer, m)) );
            game.getWorld().bombs.forEach(b -> spriteBombs.add(SpriteFactory.createBomb(layer, b)));
            game.getWorld().explosions.forEach(e -> spriteExplosion.add(SpriteFactory.createExplosion(layer, e)));
            render();
            game.getWorld().setChanges(false);
        }
    }

    private void render() {
        sprites.forEach(Sprite::render);
        spriteMonsters.forEach( sm -> sm.render() );
        spriteBombs.forEach( sb -> sb.render());

        // last rendering to have player in the foreground
        spritePlayer.render();
        spriteExplosion.forEach( se -> se.render());
    }


}
