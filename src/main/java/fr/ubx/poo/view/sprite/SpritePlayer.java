/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.view.sprite;

import fr.ubx.poo.model.InvulnerabilityTimer;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Pane;

import java.util.Timer;
import java.util.TimerTask;

public class SpritePlayer extends SpriteGameObject {

    //private final ColorAdjust effect = new ColorAdjust();
    private Timer blinkinTimer;
    private InvulnerabilityTimer blinkinTask;

    public SpritePlayer(Pane layer, Player player) {
        super(layer, null, player);
        setBrightness(0);
        updateImage();
    }
    public double getBrightness(){
        return this.effect.getBrightness();
    }
    public void setBrightness(double brightness){
        this.effect.setBrightness(brightness);
    }

    @Override
    public void updateImage() {
        Player player = (Player) go;

        if(player.getVulnerabilityStatus() && blinkinTimer == null){
            blinkinTimer = new Timer();
            blinkinTask = new InvulnerabilityTimer(this);
            blinkinTimer.scheduleAtFixedRate(blinkinTask, 0, 100);
        }else if(!player.getVulnerabilityStatus() && blinkinTimer != null){
            this.destroyTimer();
            this.effect.setBrightness(0);
        }
        setImage(ImageFactory.getInstance().getPlayer(player.getDirection()));
    }

    public void destroyTimer(){
        this.blinkinTimer.cancel();
        this.blinkinTimer.purge();
        this.blinkinTimer = null;
    }
}
