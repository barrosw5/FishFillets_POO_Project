package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public abstract class GameCharacter extends GameObject {
	private static final boolean isMovel = true;

	public GameCharacter(Room room) {
		super(room, isMovel);
	}
	
	public void move(Vector2D dir) {
		Point2D startPosition = getPosition();
		setPosition(startPosition.plus(dir));		
	}

	@Override
	public int getLayer() {
		return 2;
	}
	
}