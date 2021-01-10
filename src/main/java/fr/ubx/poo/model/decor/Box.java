package fr.ubx.poo.model.decor;

public class Box extends Decor {

    // Cannot be crossed. No need for an ID.

    @Override
    public boolean movable(){ return true; }

    @Override
    public boolean destroyable(){ return true; }

    @Override
    public String toString() {
        return "Box";
    }
}
