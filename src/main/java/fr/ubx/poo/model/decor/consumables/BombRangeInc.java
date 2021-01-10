package fr.ubx.poo.model.decor.consumables;

import fr.ubx.poo.model.decor.Decor;

public class BombRangeInc extends Decor {
    @Override
    public int getID(){
        return 2;
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
        return "BombRangeIncrease";
    }
}
