package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Anchor extends nonLightObject implements Gravity {

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
    public boolean isLight() {
        return false;
    }

    
    
    @Override
    public boolean canMoveNonLight(Point2D from, Point2D to, Vector2D dir, GameObject cla) {
         if (MovedOnce) {
            return false;
        }
        if (!Point2D.sameDirectionHorzontal(from, to)) {
            return false;
        }
        for (GameObject obj1 : cla.getRoom().getObjects()) {
            if (obj1.getPosition().equals(to)) {
                if ((obj1 instanceof NonMovable 
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
    public boolean canDown() {
        Point2D pos = this.getPosition();
        Point2D below = new Point2D(pos.getX(), pos.getY() + 1);

        if (below.getY() >= 10) { // grid is 0-9 on both axes
            return false;
        }

        for (GameObject obj : this.getRoom().getObjects()) {
            if (obj == this) {
                continue;
            }
            if (obj.getPosition().equals(below) && !(obj instanceof Water)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void down() {
        if (!canDown()) {
            return;
        }

        Point2D pos = this.getPosition();
        Point2D below = new Point2D(pos.getX(), pos.getY() + 1);
        pushObject(this, pos, below);
    }
}
