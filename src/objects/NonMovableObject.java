package objects;

import pt.iscte.poo.game.Room;

public abstract class NonMovableObject extends GameObject{

    public NonMovableObject( Room room) {
        super(room, false);
    }

    // Base para obstáculos fixos (paredes, troncos, aço, etc.)
}
