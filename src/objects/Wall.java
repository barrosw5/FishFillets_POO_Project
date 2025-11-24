package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Wall extends NonMovableObject implements Interactable{

	public Wall(Room room) {
		super(room);
	}

	@Override
	public String getName() {
		return "wall";
	}	

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public boolean interagirPeixe(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
		return false;
	}

}
