package objects;

import pt.iscte.poo.game.Room;

public class HoledWall extends NonMovable {

	public HoledWall(Room room) {
		super(room);
	}

	@Override
	public String getName() {
		return "holedWall";
	}

	@Override
	public int getLayer() {
		return 1;
	}

}