package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Spinach extends LightObject implements Interactable{
    
    public Spinach(Room r){
        super(r);
    }

    @Override
    public String getName() {
        return "spinach";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    @Override
    public boolean interactWithFish(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
        if(fish instanceof SmallFish){
            ((SmallFish) fish).activateSuperPower();
            getRoom().removeObject(this);
        }
        return true;
    }
}
