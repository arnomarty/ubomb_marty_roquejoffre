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
    private StatusBar statusBar;
    private Pane layer;
    private Input input;
    private Stage stage;
    private Sprite spritePlayer;                                  // Player Sprites
    private final List<Sprite> sprites = new ArrayList<>();      // Decor sprites
    private List<Sprite> spriteMonsters = new ArrayList<>();    // Monster Sprites
    private List<Sprite> spriteBombs = new ArrayList<>();      // Bomb Sprites
    private List<Sprite> spriteExplosion = new ArrayList<>(); // Explosion Sprites




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


     // Browse through the bombs list. If one needs to explode, removes it
    // from the list and initiate the timer destruction function (see Bomb.java)
    public void checkBombs(){
        Iterator<Bomb> iter = this.game.getWorld().bombs.iterator();
        while(iter.hasNext()){
            Bomb b = iter.next();
            if(b.explosionStatus()){
                iter.remove();
                b.stop();
                this.game.getWorld().setChanges(true);  // Something changed, please refresh
            }
        }
    }


     // Works on the same principle as the previous function, except it browses
    // through the explosion animations list. If one is over, removes and destructs.
    public void checkExplosions(){
        Iterator<Explosion> iter = this.game.getExplosions().iterator();
        while(iter.hasNext()){
            Explosion e = iter.next();
            if(!e.getStatus()) {
                e.stop();
                iter.remove();
                game.getWorld().setChanges(true);   // Something changed, please refresh
            }
        }
    }





    // ------------------ METHODES INTERNES ------------------ //

    private void initialize(Stage stage, Game game) {
        // Window's set-up
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

        // Creates sprites for, in that order: decors, the main character, and the monsters
        game.getWorld().forEach( (pos,d) -> sprites.add(SpriteFactory.createDecor(layer, pos, d)));
        spritePlayer = SpriteFactory.createPlayer(layer, player);
        game.getWorld().monsters.forEach( m -> spriteMonsters.add(SpriteFactory.createMonster(layer, m)) );
    }


    protected final void buildAndSetGameLoop() {
        gameLoop = new AnimationTimer() {
            public void handle(long now) {
                // Check keyboard actions
                processInput(now);

                // Do stuff
                update(now);

                // Graphic update
                render();
                statusBar.update(game);
            }
        };
    }


    // Handles program's behavior when a key is pressed.
    private void processInput(long now) {
        if (input.isExit()) {   // Escape key
            gameLoop.stop();
            Platform.exit();
            System.exit(0);
        }
        if (input.isMoveDown()) {   // Down key
            player.requestMove(Direction.S);
        }
        if (input.isMoveLeft()) {   // Left key
            player.requestMove(Direction.W);
        }
        if (input.isMoveRight()) {  // Right key
            player.requestMove(Direction.E);
        }
        if (input.isMoveUp()) {     // Up key
            player.requestMove(Direction.N);
        }
    // If space bar, at least one bomb in inventory, and no bomb at player's position...
        if(input.isBomb() && player.getInventory() > 0 && !game.bombThere(player.getPosition())){
            game.getWorld().bombs.add(new Bomb(game, player.getPosition()));          // Add a bomb to bombs list
            this.player.setInventory(-1);                                            // Apply changes to the inventory
            statusBar.update(game);                                                 // Update status bar accordingly
            game.getWorld().bombs.forEach( b -> spriteBombs.add(SpriteFactory.createBomb(layer, b)) );  // Updates sprites list
        }
    // If enter key, and at least one key in the inventory...
        if(input.isKey() && game.getPlayer().getKeys() > 0){
            Position facing = player.getDirection().nextPosition(player.getPosition()); // Get the position in front of player
            if(game.getWorld().get(facing) instanceof DoorNextClosed){
                game.getWorld().clear(facing);
                game.getWorld().set(facing, new DoorNextOpened());
                player.setKeys(-1);                  // Removes one key from inventory
                game.getWorld().setChanges(true);   // Something changed, please refresh
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


    // This function handles every changed relative to the game and the Sprites management.
    private void update(long now) {

        if(game.switchedFloor != 0){            // --> If the player went up (=1) or down (=-1) in the dungeon...
            player.setPosition(game.getWorld().findEntry(game.switchedFloor));  // Find which door the player comes from
            initialize(stage, game);            // (Re)initialize the game with the new grid
            game.getWorld().setChanges(true);   // Something changed, please refresh
            game.switchedFloor = 0;
        }
        player.update(now);                                       // Updates player's position
        game.getWorld().monsters.forEach( m -> m.update(now));   // Updates each monster
        checkBombs();                                           // Look at the public functions
        checkExplosions();                                     //  ^

        if (player.isAlive() == false) {
            gameLoop.stop();
            showMessage("Defeat!", Color.RED);
        }
        if (player.isWinner()) {
            gameLoop.stop();
            showMessage("Victory!", Color.BLUE);
        }
    // If a monster is dead, remove it from the list
        game.getWorld().monsters.removeIf( m -> m.isAlive() == false);

    // If something changed...
        if(game.getWorld().getChanges()){
            sprites.forEach(Sprite::remove);        // Clear every sprites list
            sprites.clear();
            spriteBombs.forEach(Sprite::remove);
            spriteBombs.clear();
            spriteMonsters.forEach(Sprite::remove);
            spriteMonsters.clear();
            spriteExplosion.forEach(Sprite::remove);
            spriteExplosion.clear();
        // Reload the new updated sprites lists
            game.getWorld().forEach( (pos,d) -> sprites.add(SpriteFactory.createDecor(this.layer, pos, d)));
            game.getWorld().monsters.forEach( m -> spriteMonsters.add(SpriteFactory.createMonster(layer, m)) );
            game.getWorld().bombs.forEach(b -> spriteBombs.add(SpriteFactory.createBomb(layer, b)));
            game.getWorld().explosions.forEach(e -> spriteExplosion.add(SpriteFactory.createExplosion(layer, e)));
            render();
            game.getWorld().setChanges(false);  // Nothing changed now, no need to refresh
        }
    }


    private void render() {
        sprites.forEach(Sprite::render);
        spriteBombs.forEach( sb -> sb.render());
        spriteMonsters.forEach( sm -> sm.render() );
        spriteExplosion.forEach( se -> se.render());

        // Player rendered last, his sprite above the others
        spritePlayer.render();
    }


}
