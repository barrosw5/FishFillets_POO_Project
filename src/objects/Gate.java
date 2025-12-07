package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Gate extends NonMovableObject implements Interactable{

    public Gate(Room r){
        super(r);
    }

    @Override
    public String getName() {
        return "gate";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    // nao é movivel por nada
    @Override
    public boolean interactWithFish(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
        return false;
    }
    
}
