package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Stone extends NonLightObject {

	public Stone(Room room) {
		super(room);
	}

	@Override
	public String getName() {
		return "stone";
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
			for (GameObject obj1 : cla.getRoom().getObjects()) {
			if (obj1.getPosition().equals(to)) {
				if ((obj1 instanceof MovableObject || obj1 instanceof NonMovable || obj1 instanceof GameCharacter)) {
					return false;
				}
			}
		}

		return true;
	}

	

}
