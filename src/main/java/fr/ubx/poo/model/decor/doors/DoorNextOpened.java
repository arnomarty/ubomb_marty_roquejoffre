package fr.ubx.poo.model.decor.doors;

import fr.ubx.poo.model.decor.Decor;

public class DoorNextOpened extends Decor {

    @Override
    public int getID(){
        return 6;
    }

    @Override
    public boolean consumable(){
        return true;
    }

    @Override
    public String toString() {
        return "DoorNextOpened";
    }

}
