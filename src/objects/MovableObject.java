package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public abstract class MovableObject extends GameObject implements Gravity{
    private static boolean isMovable = true;
    private boolean controlDown = false; 
    
    public MovableObject(Room room) {
        super(room, isMovable);
    }

    public void changeMovability(){
        isMovable = !isMovable;
    }

    @Override
    public boolean canFall() {
        Point2D pos = this.getPosition();
        Point2D below = new Point2D(pos.getX(), pos.getY() + 1);

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

        else if (controlDown && !canFall()) {
            specialAbillity();
            controlDown = false;
        }
    }


    public abstract boolean isLightObject();
}
