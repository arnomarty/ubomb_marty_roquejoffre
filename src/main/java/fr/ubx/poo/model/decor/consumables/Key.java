package fr.ubx.poo.model.decor.consumables;

import fr.ubx.poo.model.decor.Decor;

public class Key extends Decor {
    @Override
    public int getID(){
        return 0;
    }

    @Override
    public boolean consumable(){
        return true;
    }

    @Override
    public String toString() {
        return "Key";
    }
}
