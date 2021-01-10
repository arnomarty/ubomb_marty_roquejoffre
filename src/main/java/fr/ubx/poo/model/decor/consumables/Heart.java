package fr.ubx.poo.model.decor.consumables;

import fr.ubx.poo.model.decor.Decor;

public class Heart extends Decor {
    @Override
    public int getID(){
        return 1;
    }

    // Cannot be moved

    @Override
    public boolean consumable(){
        return true;
    }

    @Override
    public boolean destroyable(){
        return true;
    }

    @Override
    public String toString() {
        return "Heart";
    }
}
