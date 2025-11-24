package objects;

import java.util.List;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Bomb extends LightObject {

	public Bomb(Room room) {
		super(room);
	}

	@Override
	public String getName() {
		return "bomb";
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public boolean canMoveLightObject(Point2D from, Point2D to, Vector2D dir, GameObject cla) {
		for ( GameObject obj1: cla.getRoom().getObjects()) {
			if ( obj1.getPosition().equals(to)) {
				if ( (obj1 instanceof NonMovableObject || obj1 instanceof MovableObject || obj1 instanceof GameCharacter)) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void specialAbillity() {

		Point2D currentlyPos = this.getPosition();
		List<Point2D> nearObjects = getAdjacentPositions(currentlyPos);
		nearObjects.add(currentlyPos);

		Point2D beloPos = getBelow(currentlyPos);

		GameObject below = findObject(beloPos, getRoom());

		if ( below instanceof GameCharacter) {
			return;
		}

		for (Point2D pos: nearObjects) {
			GameObject remove = findObject(pos, this.getRoom());

			if (remove instanceof GameCharacter) {
			((GameCharacter) remove).setDeadState(true);
			}

			if (remove != null) {
				this.getRoom().removeObject(remove);
			}
		}
		

	}

}

 