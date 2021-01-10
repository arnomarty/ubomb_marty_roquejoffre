package fr.ubx.poo.model.decor;

public class Box extends Decor {

    @Override
    public boolean movable(){ return true; }

    @Override
    public boolean destroyable(){
        return true;
    }

    @Override
    public String toString() {
        return "Box";
    }
}
