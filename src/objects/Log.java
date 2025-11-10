package objects;

import pt.iscte.poo.game.Room;

public class Log extends GameObject {

	public Log(Room room) {
		super(room, false);
	}

	@Override
	public String getName() {
		return "log";
	}

	@Override
	public int getLayer() {
		return 1;
	}

}