package objects;

import pt.iscte.poo.game.Room;

public abstract class NonMovable extends GameObject{
    private static final boolean isMovable = false;

    public NonMovable( Room room) {
        super(room, isMovable);
    }
    
}
