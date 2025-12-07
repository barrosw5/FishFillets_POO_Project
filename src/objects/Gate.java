package objects;

import pt.iscte.poo.game.Room;

public class Gate extends NonMovableObject{

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
}
