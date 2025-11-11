package objects;

import pt.iscte.poo.game.Room;

public abstract class MovableObject extends GameObject {
    private static final boolean isMovable = true;
    
    public MovableObject(Room room) {
        super(room, isMovable);
    }
}
