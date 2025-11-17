package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public abstract class HeavyObject extends MovableObject{

    public HeavyObject(Room room) {
        super(room);
        
    }

    public boolean isLightObject() {
        return false;
    }

    // para saber se o personagens pesados (peixe grande) pode mover
    public abstract boolean heavyCanMove(Point2D from, Point2D to, Vector2D dir, GameObject cla);
    
}
