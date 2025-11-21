package objects;

import pt.iscte.poo.game.Room;

public class Trap extends NonMovableObject implements KillBigFish,FishCanPass{

	public Trap(Room room) {
		super(room);
	}

	@Override
	public String getName() {
		return "trap";
	}

	@Override
	public int getLayer() {
		return 1;
	}	

}