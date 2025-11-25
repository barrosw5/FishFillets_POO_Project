package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public class Explosion extends GameObject{

    public Explosion(Point2D position,Room room){
        super(position, room);
    }

    @Override
    public String getName() {
        return "explosion";
    }

    @Override
    public int getLayer() {
        return 2;
    }

    
    
}
