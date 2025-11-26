package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Anchor extends HeavyObject implements Interactable {

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
                ((SmallFish) remove).dead(remove);
            }
            if ( remove instanceof Trunk) {
                this.getRoom().removeObject(remove);
            }
        }
    }

    @Override
    public boolean interactWithFish(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
        if ( canPushby(fish, from, to, dir)) {
            pushObject(this, from, to);
            return true;
        }
        return false;
    }


    private boolean canPushby(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
        if (MovedOnce) {
            return false;
        }
        if (!Point2D.sameDirectionHorzontal(from, to)) {
            return false;
        }

        if ( ! (fish instanceof BigFish )) {
            return false;
        }

        GameObject obj = findObject(to, fish.getRoom());

        if ((obj instanceof NonMovableObject || obj instanceof MovableObject || obj instanceof GameCharacter)) {
            return false;
        }
    
        return true;
    } 
}
