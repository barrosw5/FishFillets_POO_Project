package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class ToxicBubble extends LightObject implements Interactable, Gravity{

    public ToxicBubble(Room room) {
        super(room);
    }

    @Override
    public String getName() {
        return "toxicBubble";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    @Override
    public boolean canSpecialMov() {
        Point2D above = getAbove(getPosition());
        GameObject obj = findObject(above, getRoom());
        if(obj == null || obj instanceof Coin)
            return true;
        return false;
    }

    @Override
    public void specialmov() {
        if(canSpecialMov()){
            pushObject(this, getPosition(), getAbove(getPosition()));
        }
        else{
            specialAbillity();
        }
    }

    @Override
    public void specialAbillity() {
        Point2D above = getAbove(getPosition());
        GameObject obj = findObject(above, getRoom());

        if(obj instanceof GameCharacter){
            ((GameCharacter) obj).die(obj);
        }
        getRoom().removeObject(this);
    }

    @Override
    public boolean interactWithFish(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
        specialAbillity();
        return true;
    }
    
}
