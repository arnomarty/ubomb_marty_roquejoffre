package fr.ubx.poo.model.decor.consumables;

import fr.ubx.poo.model.decor.Decor;

public class BombNumberDec extends Decor {
    @Override
    public int getID(){
        return 5;
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
        return "BombNumberDecrease";
    }
}
