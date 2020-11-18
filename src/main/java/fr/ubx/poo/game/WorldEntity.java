/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.game;

import java.util.Arrays;
import java.util.Optional;

public enum WorldEntity {
    Empty('_'),         //OK
    Box('B'),           //OK
    Heart('H'),         //OK
    Key('K'),           //OK
    Monster('M'),
    DoorPrevOpened('V'),//OK
    DoorNextOpened('N'),//OK
    DoorNextClosed('n'),//OK
    Player('P'),        //OK
    Stone('S'),         //OK
    Tree('T'),          //OK
    Princess('W'),
    BombRangeInc('>'),
    BombRangeDec('<'),
    BombNumberInc('+'),
    BombNumberDec('-')
        ;


    private char getCode() {
        return code;
    }

    private final char code;

    WorldEntity(char code) {
        this.code = code;
    }

    public static Optional<WorldEntity> fromCode(char code) {
        return Arrays.stream(values())
                .filter(e->e.acceptCode(code))
                .findFirst();
    }

    private boolean acceptCode(char code) {
        return this.code == code;
    }

    @Override
    public String toString() {
        return ""+code;
    }
}
