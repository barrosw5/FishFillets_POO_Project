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

     @Override
    public boolean interactWithFish(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
        if ( CoinPushBy(fish, from, to, dir)) {
            Room.reduceTime(10);
            pushObject(fish, from, to);
            return true;  
        }
        return false;
    }
    
    public boolean CoinPushBy(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
        GameObject.removeObject(this, getRoom());
        return true;
    }
}
