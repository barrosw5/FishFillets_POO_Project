package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public class Cup extends MovableObject {

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
	public boolean isLight() {
		return true;
	}

	@Override
	public boolean canMove(Point2D to, GameObject cla) {

		for (GameObject obj1 : cla.getRoom().getObjects()) {
			if (obj1.getPosition().equals(to)) {
				if ((obj1 instanceof MovableObject || obj1 instanceof NonMovable || obj1 instanceof GameCharacter)
						&& !(obj1 instanceof HoledWall)) {
					return false;
				}
			}
		}

		return true;
	}

    
	}

	
