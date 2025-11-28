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
    public void reset() {
        super.reset();
        controlDown = false;    // para a bomba nao pensar que ainda está a cair
    }

    @Override
    public boolean canFall() {
        Point2D pos = this.getPosition();
        Point2D below = new Point2D(pos.getX(), pos.getY() + 1);
        GameObject obj = findObject(below, this.getRoom());

        if(obj == null )
            return true;

        return false;
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
}
