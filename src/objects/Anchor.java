package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Anchor extends HeavyObject {

    private boolean MovedOnce = false;

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
                ((SmallFish) remove).setDeadState(true);
            }

            if ( remove instanceof Trunk) {
                this.getRoom().removeObject(remove);
            }
        }
    }
}
