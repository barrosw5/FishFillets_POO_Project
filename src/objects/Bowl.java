package objects;

import pt.iscte.poo.game.Room;

public class Bowl extends MovableObject {

	public Bowl(Room room) {
		super(room);
	}

	@Override
	public String getName() {
		return "bowl";
	}

	@Override
	public int getLayer() {
		return 1;
	}

}