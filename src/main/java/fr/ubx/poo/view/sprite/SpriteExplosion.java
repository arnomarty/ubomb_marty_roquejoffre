package fr.ubx.poo.view.sprite;

import fr.ubx.poo.model.go.Bomb;
import fr.ubx.poo.model.go.Explosion;
import fr.ubx.poo.view.image.ImageFactory;
import javafx.scene.layout.Pane;

public class SpriteExplosion extends SpriteGameObject{

    public SpriteExplosion(Pane layer, Explosion explosion) {
        super(layer, null, explosion);
        updateImage();
    }

    @Override
    public void updateImage() {
        Explosion b = (Explosion) go;
        setImage(ImageFactory.getInstance().getExplosion());

    }
}
