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
        if ( CoinPushBy(fish, from, to, dir)) {
            getRoom().reduceRealTime(10);
            pushObject(fish, from, to);
            return true;  
        }
        return false;
    }

    // Basicamente, se for qql um dos fishes a tocar na coin, a moeda é removida do jogo 
    
    public boolean CoinPushBy(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
        if ( fish instanceof BigFish || fish instanceof SmallFish) {
            GameObject.removeObject(this, getRoom());
            return true;
        }
       return false;
    }
}
