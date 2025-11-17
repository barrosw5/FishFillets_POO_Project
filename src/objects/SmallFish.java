package objects;

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
						&& !(obj instanceof Trap)
						&& !(obj instanceof HoledWall)) {
					return false;
				}
			}
		}

		return true;
	}

	
}

