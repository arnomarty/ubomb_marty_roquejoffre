/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.view.image;

import fr.ubx.poo.game.Direction;
import javafx.scene.image.Image;

import static fr.ubx.poo.view.image.ImageResource.*;

public final class ImageFactory {

    private final Image[] images;
    private final ImageResource[] directions = new ImageResource[]{
            // Direction { N, E, S, W }
            PLAYER_UP, PLAYER_RIGHT, PLAYER_DOWN, PLAYER_LEFT,
    };
    private final ImageResource[] directionsMonster = new ImageResource[]{
            MONSTER_UP, MONSTER_RIGHT, MONSTER_DOWN, MONSTER_LEFT,
    };
    private final ImageResource[] bombStates = new ImageResource[]{
            BOMB4, BOMB3, BOMB2, BOMB1, EXPLOSION,
    };
    private final ImageResource[] digits = new ImageResource[]{
            DIGIT_0, DIGIT_1, DIGIT_2, DIGIT_3, DIGIT_4,
            DIGIT_5, DIGIT_6, DIGIT_7, DIGIT_8, DIGIT_9,
    };




    // ------------------ CONSTRUCTEUR ------------------ //

    private ImageFactory() {
        images = new Image[ImageResource.values().length];
    }





    // ------------------ ACCESSEURS ------------------ //

    public static ImageFactory getInstance() {
        return Holder.instance;
    }
    public Image get(ImageResource img) {
        return images[img.ordinal()];
    }
    public Image getPlayer(Direction direction) {
        return get(directions[direction.ordinal()]);
    }
    public Image getMonster(Direction direction){ return get(directionsMonster[direction.ordinal()]); }
    public Image getBombs(int state){ return get(bombStates[state]); }
    public Image getExplosion(){ return get(bombStates[4]); }





    // ------------------ METHODES PUBLIQUES ------------------ //

    public void load() {
        for (ImageResource img : ImageResource.values()) {
            images[img.ordinal()] = loadImage(img.getFileName());
        }
    }

    public Image getDigit(int i) {
        if (i < 0 || i > 9)
            throw new IllegalArgumentException();
        return get(digits[i]);
    }





    // ------------------ METHODES INTERNES ------------------ //

    private Image loadImage(String file) {
        return new Image(getClass().getResource("/images/" + file).toExternalForm());
    }

    private static class Holder {
        private final static ImageFactory instance = new ImageFactory();
    }

}
