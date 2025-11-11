package objects;

import pt.iscte.poo.game.Room;

public class WallWithHole extends NonMovable {

	public WallWithHole(Room room) {
		super(room);
	}

	@Override
	public String getName() {
		return "wallWithHole";
	}

	@Override
	public int getLayer() {
		return 1;
	}

}