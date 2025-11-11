package objects;

import pt.iscte.poo.game.Room;

public class Rock extends MovableObject {

	public Rock(Room room) {
		super(room);
	}

	@Override
	public String getName() {
		return "rock";
	}

	@Override
	public int getLayer() {
		return 1;
	}

}
