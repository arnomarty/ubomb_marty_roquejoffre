/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.model.decor;


public class Tree extends Decor {

    // Cannot get moved, crossed nor destroyed.
    // No need for an ID.

    @Override
    public String toString() {
        return "Tree";
    }
}
