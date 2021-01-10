/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.decor;

import fr.ubx.poo.model.Entity;

/***
 * A decor is an element that does not know its own position in the grid.
 * It can either be movable (Like the Box object), crossable (Bonuses or princess)
 * or destroyable (bonuses or boxes).
 *
 * By default, all parameters set to false.
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
