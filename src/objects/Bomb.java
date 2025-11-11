package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public class Bomb extends MovableObject {

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
	public boolean isLight() {
		return true;
	}


	@Override
	public boolean canMove(Point2D to, GameObject cla) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'canMove'");
	}


}