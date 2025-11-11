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
		return 1;
	}

<<<<<<< HEAD
<<<<<<< HEAD
	@Override  
=======
	@Override
>>>>>>> 233a844dcdc1b537bf55db5ca24f355ab2fa2c46
	public boolean canMove(Point2D pos, Vector2D dir) {
		Point2D finalPos = pos.plus(dir);

<<<<<<< HEAD
    for (GameObject obj : this.getRoom().getObjects()) {
       if ( obj.getPosition().equals(finalPos)) {
		// O SmallFish é bloqueado por tudo, com exeção da Trap e do HoledWall 
				if ( obj instanceof Cup) {
					Point2D finalObPos = finalPos.plus(dir);
					//if ( )
					pushObject(obj, finalPos, finalObPos);
					return true;
				}
				if ( (obj instanceof MovableObject || obj instanceof NonMovable) && ! (obj instanceof Trap) && !( obj instanceof HoledWall)) {
					return false;
=======
=======
	@Override
	public boolean canMove(Point2D pos, Vector2D dir) {
		Point2D finalPos = pos.plus(dir);

>>>>>>> cae078089436d9ae4c33686f1c9794c1390ff495
		for (GameObject obj : this.getRoom().getObjects()) {
		if ( obj.getPosition().equals(finalPos)) {
					if ( (obj instanceof MovableObject || obj instanceof NonMovable) && ! (obj instanceof Trap) && !( obj instanceof HoledWall)) {
						return false;
					}
<<<<<<< HEAD
>>>>>>> 233a844dcdc1b537bf55db5ca24f355ab2fa2c46
=======
>>>>>>> cae078089436d9ae4c33686f1c9794c1390ff495
				}
		}

    	return true;
	}

}
