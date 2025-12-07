package objects;

import pt.iscte.poo.game.Room;

public abstract class MovableObject extends GameObject {
    private static boolean isMovable = true;
    
    public MovableObject(Room room) {
        super(room, isMovable);
    }

    @Override
    public void reset() {
        super.reset();
    }

}
