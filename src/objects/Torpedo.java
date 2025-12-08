package objects;

import java.util.List;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Torpedo extends LightObject implements Interactable, Gravity{

    public Torpedo(Room room) {
        super(room);
    }

    @Override
    public String getName() {
        return "torpedo";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    @Override
    public boolean canSpecialMov() {
        Point2D right = new Point2D(getPosition().getX() + 1, getPosition().getY());
        if(findObject(right, getRoom()) == null || findObject(right, getRoom()) instanceof Coin)
            return true;
        return false;
    }

    @Override
    public void specialmov() {
        if(canSpecialMov()){
            pushObject(this, getPosition(), new Point2D(getPosition().getX() + 1, getPosition().getY()));
        }
        else{
            specialAbillity();
        }
    }

    @Override
    public void specialAbillity() {
        List<Point2D> adj = getAdjacentPositions(this.getPosition());
        adj.add(this.getPosition());

        for(Point2D p : adj){
            GameObject obj = findObject(p, getRoom());
            if(obj instanceof GameCharacter)
                ((GameCharacter) obj).die(obj);
            if(obj != null)
                getRoom().removeObject(obj);
            getRoom().addObject(new Explosion(p, getRoom()));
        }
    }

    @Override
    public boolean interactWithFish(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
        specialmov();
        return true;
    }
    
}
