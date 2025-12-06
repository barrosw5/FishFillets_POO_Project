package objects;

import pt.iscte.poo.game.Room;

public abstract class LightObject extends MovableObject{

    public LightObject(Room room) {
        super(room);
    }

    // Classe base para objetos leves empurráveis por qualquer peixe (salvo regras próprias)
}
