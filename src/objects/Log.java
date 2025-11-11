package objects;

import pt.iscte.poo.game.Room;

public class Log extends NonMovable {

	public Log(Room room) {
		super(room);
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