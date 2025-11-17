package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public abstract class HeavyObject extends MovableObject{

    public HeavyObject(Room room) {
        super(room);
        
    }

    @Override
    public boolean isLightObject() {
        return false;
    }

    // para saber se um peixe pode mover objetos pesados
    public abstract boolean canMoveHeavyObject(Point2D from, Point2D to, Vector2D dir, GameObject cla);
    
}
