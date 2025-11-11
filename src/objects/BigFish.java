package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class BigFish extends GameCharacter {

	private static BigFish bf = new BigFish(null);
	
	private BigFish(Room room) {
		super(room);
	}

	public static BigFish getInstance() {
		return bf;
	}
	
	@Override
	public String getName() {
		return "bigFishLeft";
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public boolean canMove(Point2D pos, Vector2D dir) { // Creio que está feito 
		Point2D finalPos = pos.plus(dir);
		
		for ( GameObject obj: BigFish.getInstance().getRoom().getObjects() ) {
			if ( obj.getPosition().equals(finalPos)) {
				if ( obj instanceof MovableObject || obj instanceof NonMovable) {
					return false;
				}
			}
		}

		return true;
	
	}
}
