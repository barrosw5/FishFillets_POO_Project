package objects;

import pt.iscte.poo.game.Room;

public abstract class HeavyObject extends MovableObject{

    public HeavyObject(Room room) {
        super(room);
    }    

    // Marcador para objetos pesados (pedra, âncora, etc.) que só o BigFish consegue empurrar
}
