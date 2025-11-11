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
		if ( canMove(startPosition, dir)) {
			setPosition(startPosition.plus(dir));		
		}
	}

	public void pushObject(GameObject obj, Point2D from, Point2D to) {
		obj.setPosition(to);
	}

	@Override
	public int getLayer() {
		return 2;
	}
	
	// O CanMove garante que o peixe não vai em direção ou passe algo que não é suposto
	// Está implementado como abstrato no GameCharacter 
	//cada peixe tem o seu visto que os dois têm regras movimentação diferentes
	public abstract boolean canMove(Point2D pos, Vector2D dir);
}