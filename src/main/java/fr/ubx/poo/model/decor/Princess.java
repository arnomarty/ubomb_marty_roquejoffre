package fr.ubx.poo.model.decor;

public class Princess extends Decor{

    @Override
    public  int getID(){ return 8; }

    // Cannot be moved nor destroyed (oddly)

    @Override
    public boolean consumable(){
        return true;
    }

    @Override
    public String toString() {
        return "Princess";
    }
}
