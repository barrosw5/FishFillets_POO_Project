package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public class Anchor extends MovableObject {

	public Anchor(Room room) {
		super(room);
	}

	@Override
	public String getName() {
		return "anchor";
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
	public boolean canMove(Point2D to, GameObject cla) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'canMove'");
	}

	

	

}