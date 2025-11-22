package objects;

import java.util.ArrayList;
import java.util.List;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class SmallFish extends GameCharacter {

	private static SmallFish sf = new SmallFish(null);
	private static final String sfNameLeft = "smallFishLeft";
	private static final String sfNameRight = "smallFishRight";
	
	private SmallFish(Room room) {
		super(room);
	}

	public static SmallFish getInstance() {
		return sf;
	}
	
	@Override
	public String getName() {
		return getDirection() ? sfNameLeft : sfNameRight;
	}

	@Override
	public int getLayer() {
		return 2;
	}

	// O peixe pequeno também, em princípio está finalizado, a sua canMove 

	@Override  
	public boolean fishCanMove(Point2D pos, Vector2D dir) {
		if(hasWon())
			return false;
	
		Point2D finalPos = pos.plus(dir);

		for (GameObject obj : getRoom().getObjects()) {
			if (obj.getPosition().equals(finalPos)) {

				// O SmallFish é bloqueado por tudo, com exceção da Trap e do HoledWall
				// Can move é usado para mover Objetos que NÃO SÃO peixes
				if (obj instanceof MovableObject && obj.canMoveLightObject(finalPos,finalPos.plus(dir), dir,this)) {
					Point2D finalObPos = finalPos.plus(dir);
					pushObject(obj, finalPos, finalObPos);
					return true;
				}

				if ((obj instanceof MovableObject || obj instanceof NonMovableObject || obj instanceof GameCharacter)		// VERIFICAR LOGICA
						&& !( obj instanceof FishCanPass)) {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public boolean fishSupport() {
		Point2D CurrentlyPos = getPosition();
		int lengthPos = CurrentlyPos.getY();

		List<Point2D> posUp = new ArrayList<>(); 

		for ( int i = lengthPos; i > 0; i--) {
			posUp.add( new Point2D(CurrentlyPos.getX(), i--));
		}

		int count = 0;

		for ( Point2D objPos: posUp) {
			GameObject obj = findObject(objPos, getRoom());

			if ( obj instanceof HeavyObject) {
				return false;
			}

			if ( obj instanceof MovableObject) {
				count++;
			}
		}
		
		if ( count != 1) {
			return false;
		}
		return true;
	}

	

	
}

