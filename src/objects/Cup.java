package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Cup extends LightObject implements Interactable{

	public Cup(Room room) {
		super(room);
	}

	@Override
	public String getName() {
		return "cup";
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public boolean canMoveLightObject(Point2D from, Point2D to, Vector2D dir, GameObject cla) {
		for (GameObject obj1 : cla.getRoom().getObjects()) {
				if (obj1.getPosition().equals(to)) {
					if ((obj1 instanceof MovableObject || obj1 instanceof NonMovableObject || obj1 instanceof GameCharacter)
							&& !(obj1 instanceof HoledWall)) {
						return false;
					}
				}
			}

		return true;
	}

	@Override
	public void specialAbillity() {
	}

	@Override
	public boolean interagirPeixe(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {

		if ( CupPushBy(fish, from, to, dir)) {
			pushObject(this, from, to);
			return true;
		}
		return false;
	}

	public boolean CupPushBy(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
		GameObject obj = findObject(to, fish.getRoom());

		if ( (obj instanceof MovableObject || obj instanceof NonMovableObject || obj instanceof GameCharacter) && !(obj instanceof HoledWall)) {
			return false;
		}
		return true;
	}
    
}

	
