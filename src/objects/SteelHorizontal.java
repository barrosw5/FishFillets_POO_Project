package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class SteelHorizontal extends NonMovableObject implements Interactable {

	public SteelHorizontal(Room room) {
		super(room);
	}

	@Override
	public String getName() {
		return "steelHorizontal";
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public boolean interactWithFish(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
		return false;
	}

}
