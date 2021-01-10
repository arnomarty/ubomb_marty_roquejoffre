package fr.ubx.poo.model.decor.doors;

import fr.ubx.poo.model.decor.Decor;

public class DoorPrevOpened extends Decor {

    @Override
    public int getID(){
        return 7;
    }

    // Cannot get moved nor destroyed

    @Override
    public boolean consumable(){
        return true;
    }

    @Override
    public String toString() {
        return "DoorPrevOpened";
    }
}
