/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.view.sprite;

import static fr.ubx.poo.view.image.ImageResource.*;

import fr.ubx.poo.game.Position;
import fr.ubx.poo.model.decor.*;
import fr.ubx.poo.model.decor.consumables.*;
import fr.ubx.poo.model.decor.doors.DoorNextClosed;
import fr.ubx.poo.model.decor.doors.DoorNextOpened;
import fr.ubx.poo.model.decor.doors.DoorPrevOpened;
import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.Explosion;
import fr.ubx.poo.model.go.character.Monster;
import fr.ubx.poo.model.go.character.Player;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.layout.Pane;


public final class SpriteFactory {

    public static Sprite createDecor(Pane layer, Position position, Decor decor) {
        ImageFactory factory = ImageFactory.getInstance();
        if (decor instanceof Stone)
            return new SpriteDecor(layer, factory.get(STONE), position);
        if (decor instanceof Tree)
            return new SpriteDecor(layer, factory.get(TREE), position);
        if (decor instanceof Box)
            return new SpriteDecor(layer, factory.get(BOX), position);
        if (decor instanceof Heart)
            return new SpriteDecor(layer, factory.get(HEART), position);
        if (decor instanceof Key)
            return new SpriteDecor(layer, factory.get(KEY), position);
        if (decor instanceof DoorPrevOpened)
            return new SpriteDecor(layer, factory.get(DOORPREVOPENED), position);
        if (decor instanceof DoorNextOpened)
            return new SpriteDecor(layer, factory.get(DOORNEXTOPENED), position);
        if (decor instanceof DoorNextClosed)
            return new SpriteDecor(layer, factory.get(DOORNEXTCLOSED), position);
        if (decor instanceof BombRangeInc)
            return new SpriteDecor(layer, factory.get(BOMBRANGEINC), position);
        if (decor instanceof BombRangeDec)
            return new SpriteDecor(layer, factory.get(BOMBRANGEDEC), position);
        if (decor instanceof BombNumberInc)
            return new SpriteDecor(layer, factory.get(BOMBNUMBERINC), position);
        if (decor instanceof BombNumberDec)
            return new SpriteDecor(layer, factory.get(BOMBNUMBERDEC), position);
        if (decor instanceof Princess)
            return new SpriteDecor(layer, factory.get(PRINCESS), position);
        return null;
    }

    public static Sprite createPlayer(Pane layer, Player player) {
        return new SpritePlayer(layer, player);
    }

    public static Sprite createMonster(Pane layer, Monster monster){
        return new SpriteMonster(layer, monster);
    }

    public static Sprite createBomb(Pane layer, Bomb bomb) { return new SpriteBomb(layer, bomb); }

    public static Sprite createExplosion(Pane layer, Explosion explosion){ return new SpriteExplosion(layer, explosion); }
}
