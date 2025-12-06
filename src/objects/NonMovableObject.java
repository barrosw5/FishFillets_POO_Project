package objects;

import pt.iscte.poo.game.Room;

public abstract class NonMovableObject extends GameObject{
    private static final boolean isMovable = false;

    public NonMovableObject( Room room) {
        super(room, isMovable);
    }

    // Base para obstáculos fixos (paredes, troncos, aço, etc.)
}
