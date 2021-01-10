/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.Entity;

/***
 * A decor is an element that does not know its own position in the grid.
 */
public class Decor extends Entity {
    public boolean movable(){
        return false;
    }
    public boolean consumable(){
        return false;
    }

    public boolean destroyable(){
        return false;
    }
    public int getID(){
        return -1;
    }
}
