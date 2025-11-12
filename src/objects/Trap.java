package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Trap extends MovableObject {

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
	public boolean isLight() {
		return false;
	}



	@Override
	public boolean canMove(Point2D form, Point2D to, Vector2D dir,GameObject cla) {
		
		 for ( GameObject obj1: cla.getRoom().getObjects()) {
			if ( obj1.getPosition().equals(to)) {
                if ( (obj1 instanceof NonMovable || obj1 instanceof MovableObject || obj1 instanceof BigFish) && !(obj1 instanceof HoledWall)) {
					return false;
				}
			}
		 }
		 return true;
	}

	

}