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

public class BigFish extends GameCharacter {

	private static BigFish bf = new BigFish(null);
	private static final String bfNameLeft = "bigFishLeft";
	private static final String bfNameRight = "bigFishRight";
	private static final String bfNameUp = "bigFishUp";
	private static final String bfNameDown = "bigFishDown";
	
	private BigFish(Room room) {
		super(room);
	}

	public static BigFish getInstance() {
		return bf;
	}
	
	@Override
	public String getName() {
		if(getDirection() == LEFT)
			return bfNameLeft;
		if(getDirection() == RIGHT)
			return bfNameRight;
		if(getDirection() == UP)
			return bfNameUp;
		if(getDirection() == DOWN)
			return bfNameDown;
		return null;
	}

	@Override
	public int getLayer() {
		return 2;
	}

	public boolean fishCanMove(Point2D pos, Vector2D dir) {
		if (hasWon())
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

		for ( int i = lengthPos - 1; i > 0; i--) {
			posUp.add( new Point2D(CurrentlyPos.getX(), i));
		}

		int countHeavy = 0;
		int counLight = 0;

		for ( Point2D objPos: posUp) {
			GameObject obj = findObject(objPos, getRoom());

			if ( !( obj instanceof MovableObject) && countHeavy == 0 && counLight == 0) {
				return true;
			}

			if ( obj instanceof HeavyObject) {
				countHeavy ++;
			}

			if ( obj instanceof LightObject) {
				counLight ++;
			}
		}
		
		if ( countHeavy > 1) {
			return false;
		}
		return true;
	}


}
