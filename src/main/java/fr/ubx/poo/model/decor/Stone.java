/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.decor;

public class Stone extends Decor {

    // Cannot be moved, crossed nor destroyed.
    // No need for an ID

    @Override
    public String toString() {
        return "Stone";
    }
}
