package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Coin extends NonMovableObject implements Interactable {

    public Coin(Room room) {
        super(room);
    }
    @Override
    public String getName() {
        return "coin";
    }

    @Override
    public int getLayer() {
        return 1;
    }


    // se a moeda puder ser removida do jogo (sempre, praticamente), ele irá reduzir o tempo do player em 10s sempre q o fish comer uma. 
    @Override
    public boolean interactWithFish(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
        Point2D before = from.minus(dir);
        GameObject obj = findObject(before, getRoom());
        if ( obj instanceof GameCharacter ) {
            GameObject.removeObject(this, getRoom());
            getRoom().reduceRealTime(10);
            pushObject(fish, from, to);
            return true;  
        }
        
        if (!(obj instanceof GameCharacter)) {
            pushObject(fish, from, to);
            return true; 
        }
        return false;
    }

}
