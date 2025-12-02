package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Trap extends HeavyObject implements Interactable, Gravity {
	private boolean controlDown = false;

	public Trap(Room room) {
		super(room);
	}

	@Override
	public String getName() {
		return "trap";
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public boolean interactWithFish(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
		if ( fish instanceof SmallFish) {
			return true;
		}
		fish.die(fish);
		return false;
	}

	@Override
	public boolean canSpecialMov() {
		Point2D pos = this.getPosition();
        Point2D below = new Point2D(pos.getX(), pos.getY() + 1);
        GameObject obj = findObject(below, this.getRoom());

        if(obj == null )
            return true;

        return false;
	}

	@Override
	public void specialmov() {
		 if (canSpecialMov()) {
            Point2D pos = this.getPosition();
            Point2D below = getBelow(pos);
            pushObject(this, pos, below);
            controlDown = true;
        }

        else if (controlDown && !canSpecialMov()) {
            specialAbillity();
            controlDown = false;
        }
	}

	@Override
	public void specialAbillity() {
	}	

}