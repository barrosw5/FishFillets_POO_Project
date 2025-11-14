package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public abstract class MovableObject extends GameObject{
    private static boolean isMovable = true;
    private Point2D startingPosition;
    
    public MovableObject(Room room) {
        super(room, isMovable);
    }

    public void changeMovability(){
        isMovable = !isMovable;
    }

    public abstract boolean isLight();
}
