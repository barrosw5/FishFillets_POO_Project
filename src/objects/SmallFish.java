package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class SmallFish extends GameCharacter {

	private static SmallFish sf = new SmallFish(null);
	
	private SmallFish(Room room) {
		super(room);
	}

	public static SmallFish getInstance() {
		return sf;
	}
	
	@Override
	public String getName() {
		return "smallFishLeft";
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public boolean canMove(Point2D pos, Vector2D dir) {
		Point2D finalPos = pos.plus(dir);
		
		for ( GameObject obj: BigFish.getInstance().getRoom().getObjects() ) {
			if ( obj.getPosition().equals(finalPos)) {
				if ( obj.getIsMovel() == false ) {
					return false;
				}
			}
		}

		return true;
	}

}
