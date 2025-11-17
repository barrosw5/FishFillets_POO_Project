package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Anchor extends HeavyObject implements Gravity {

    private boolean MovedOnce = false;
    private boolean controlDown = false; 

    public Anchor(Room room) {
        super(room);
    }

    @Override
    public String getName() {
        return "anchor";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    @Override
    public void reset(){
        if (getStartingPosition() != null)
            MovedOnce = false;
        super.reset();
    }
    
    @Override
    public boolean canMoveHeavyObject(Point2D from, Point2D to, Vector2D dir, GameObject cla) {

        if (MovedOnce) {
            return false;
        }
        if (!Point2D.sameDirectionHorzontal(from, to)) {
            return false;
        }
        for (GameObject obj1 : getRoom().getObjects()) {
            if (obj1.getPosition().equals(to)) {
                if ((obj1 instanceof NonMovableObject 
				|| obj1 instanceof MovableObject
				|| obj1 instanceof GameCharacter)) {
                    return false;
                }
            }
        }
        MovedOnce = true;
        return true;
    }

    @Override
    public boolean canFall() {
        Point2D pos = this.getPosition();
        Point2D below = new Point2D(pos.getX(), pos.getY() + 1);

        if (below.getY() >= 10) { 
            return false;
        }

        for (GameObject obj : this.getRoom().getObjects()) {
            if (obj.getPosition().equals(below) && !(obj instanceof Water)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void fall() {
        if (canFall()) {
            Point2D pos = this.getPosition();
            Point2D below = getBelow(pos);
            pushObject(this, pos, below);
            controlDown = true;
        }

        if ( controlDown = true && !canFall()) {
            specialAbillity();
        }
    }

    @Override
    public void specialAbillity() {
        Point2D currenbtlyPos = this.getPosition();
        Point2D belowPos = getBelow(currenbtlyPos);

        Point2D [] area = {belowPos};

        for ( Point2D pos: area) {
            GameObject remove = GameObject.findObject(pos, getRoom());

            if ( remove instanceof Water) {
                continue;
            }

            if ( remove instanceof SmallFish) {
                this.getRoom().removeObject(remove);
            }
        }
    }
}
