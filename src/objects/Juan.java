package objects;

import pt.iscte.poo.game.Room;

public class Juan extends LightObject{

    public Juan(Room room){
        super(room);
    }

    @Override
    public String getName() {
        return "juan";
    }

    @Override
    public int getLayer() {
        return 1;
    }
    
    @Override
    public void specialAbillity() {
        
    }

    //testar polimorfism a partir daqui
    
}
