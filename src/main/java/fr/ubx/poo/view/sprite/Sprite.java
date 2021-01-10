/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.view.sprite;

import fr.ubx.poo.game.Position;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public abstract class Sprite {

    public static final int size = 40;
    private final Pane layer;
    private ImageView imageView;
    private Image image;
    public ColorAdjust effect = new ColorAdjust();




    // ------------------ CONSTRUCTEUR ------------------ //

    public Sprite(Pane layer, Image image) {
        this.layer = layer;
        this.image = image;
    }





    // ------------------ ACCESSEURS ------------------ //

    public abstract Position getPosition();





    // ------------------ METHODES PUBLIQUES ------------------ //

    public abstract void updateImage();

    public final void setImage(Image image) {
        if (this.image == null || this.image != image) {
            this.image = image;
        }
    }

    public final void render() {
        if (imageView != null) {
            remove();
        }
        updateImage();
        imageView = new ImageView(this.image);
        this.imageView.setEffect(effect);
        imageView.setX(getPosition().x * size);
        imageView.setY(getPosition().y * size);
        layer.getChildren().add(imageView);
    }

    public final void remove() {
        layer.getChildren().remove(imageView);
        imageView = null;
    }
}
