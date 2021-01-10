package fr.ubx.poo.model.decor.consumables;

import fr.ubx.poo.model.decor.Decor;

public class BombNumberInc extends Decor {

    @Override
    public int getID(){
        return 4;
    }

    // Cannot be moved

    @Override
    public boolean consumable(){
        return true;
    }

    @Override
    public boolean destroyable(){ return true; }

    @Override
    public String toString() {
        return "BombNumberIncrease";
    }
}
