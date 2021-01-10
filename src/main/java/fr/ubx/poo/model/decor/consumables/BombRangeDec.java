package fr.ubx.poo.model.decor.consumables;

import fr.ubx.poo.model.decor.Decor;

public class BombRangeDec extends Decor {
    @Override
    public int getID(){
        return 3;
    }

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
        return "BombRangeDecrease";
    }
}
