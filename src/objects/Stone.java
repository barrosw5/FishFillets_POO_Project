package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Stone extends HeavyObject implements Interactable{

	public Stone(Room room) {
		super(room);
	}

	@Override
	public String getName() {
		return "stone";
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public boolean canMoveHeavyObject(Point2D from, Point2D to, Vector2D dir, GameObject cla) {
			for (GameObject obj1 : cla.getRoom().getObjects()) {
			if (obj1.getPosition().equals(to)) {
				if ((obj1 instanceof MovableObject || obj1 instanceof NonMovableObject || obj1 instanceof GameCharacter)) {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public void specialAbillity() {
		Point2D currenbtlyPos = this.getPosition();
        Point2D belowPos = getBelow(currenbtlyPos);

        Point2D [] area = {belowPos};

        for ( Point2D pos: area) {
            GameObject remove = GameObject.findObject(pos, getRoom());

            if ( remove instanceof Water) {
                continue;
            }

            if ( remove instanceof SmallFish) {
                this.getRoom().removeObject(remove);
                ((SmallFish) remove).setDeadState(true);
            }

            if ( remove instanceof Trunk) {
                this.getRoom().removeObject(remove);
            }
        }
	}

	@Override
	public boolean interagirPeixe(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
		if ( StonePushBy(fish, from, to, dir)) {
			pushObject(this, from, to);
			return true;
		}
		
		return false;
	}

	public boolean StonePushBy(GameCharacter fish, Point2D from, Point2D to, Vector2D dir ) {
		GameObject obj = findObject(to, fish.getRoom()) ;

		if ((obj instanceof MovableObject || obj instanceof NonMovableObject || obj instanceof GameCharacter) ) {
			return false;
		}
		return true;
	}
	

}
