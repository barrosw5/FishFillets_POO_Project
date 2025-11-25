package objects;

import java.util.ArrayList;
import java.util.List;
import pt.iscte.poo.game.Room;
import static pt.iscte.poo.utils.Direction.DOWN;
import static pt.iscte.poo.utils.Direction.LEFT;
import static pt.iscte.poo.utils.Direction.RIGHT;
import static pt.iscte.poo.utils.Direction.UP;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class SmallFish extends GameCharacter {

	private static SmallFish sf = new SmallFish(null);
	private static final String sfNameLeft = "smallFishLeft";
	private static final String sfNameRight = "smallFishRight";
	private static final String sfNameUp = "smallFishUp";
	private static final String sfNameDown = "smallFishDown";
	
	private SmallFish(Room room) {
		super(room);
	}

	public static SmallFish getInstance() {
		return sf;
	}
	
	@Override
	public String getName() {
		if(getDirection() == LEFT)
			return sfNameLeft;
		if(getDirection() == RIGHT)
			return sfNameRight;
		if(getDirection() == UP)
			return sfNameUp;
		if(getDirection() == DOWN)
			return sfNameDown;
		return null;
	}
	
	@Override  
	public boolean fishCanMove(Point2D pos, Vector2D dir) {
		if(hasWon())
			return false;
	
		Point2D finalPos = pos.plus(dir);
		GameObject obj = findObject(finalPos, getRoom());
		Point2D finalObjPos = finalPos.plus(dir);

		if ( obj == null) {
			return true;
		}

		if ( obj instanceof Interactable) {
			return ((Interactable) obj).interagirPeixe(this, finalPos, finalObjPos, dir);
		}

		return false;
	}

	@Override
	public boolean fishSupport() {
		Point2D CurrentlyPos = getPosition();
		int lengthPos = CurrentlyPos.getY();

		List<Point2D> posUp = new ArrayList<>(); 

		for ( int i = lengthPos -1; i > 0; i--) {
			posUp.add( new Point2D(CurrentlyPos.getX(), i));
		}

		int count = 0;

		for ( Point2D objPos: posUp) {
			GameObject obj = findObject(objPos, getRoom());

			if ( !( obj instanceof MovableObject) && count == 0) {
				return true;
			}

			if ( obj instanceof HeavyObject) {
				return false;
			}

			if ( obj instanceof MovableObject) {
				count++;
			}
		}
		
		if ( count > 1) {
			return false;
		}
		return true;
	}

	

	
}

