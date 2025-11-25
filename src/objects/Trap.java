package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Trap extends NonMovableObject implements Interactable {

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

	@Override
	public boolean interagirPeixe(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
		if ( fish instanceof SmallFish) {
			return true;
		}
		fish.setDeadState(true);
		fish.getRoom().removeObject(fish);
		return false;
	}	

}