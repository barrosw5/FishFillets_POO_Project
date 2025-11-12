package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public abstract class MovableObject extends GameObject {
    private static boolean isMovable = true;
    
    public MovableObject(Room room) {
        super(room, isMovable);
    }

    public void changeMovability(){
        isMovable = !isMovable;
    }

    public abstract boolean isLight();

    public  abstract boolean canMove(Point2D form, Point2D to, Vector2D dir,GameObject cla);
}
