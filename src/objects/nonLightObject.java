package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public abstract class nonLightObject extends MovableObject{

    public nonLightObject(Room room) {
        super(room);
        
    }

    public boolean isLightObject() {
        return false;
    }

    public abstract boolean canMoveNonLight(Point2D from, Point2D to, Vector2D dir, GameObject cla);
    
}
