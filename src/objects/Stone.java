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
	public boolean interactWithFish(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
		if ( StonePushBy(fish, from, to, dir)) {
			pushObject(this, from, to);
			return true;
		}
		
		return false;
	}

	public boolean StonePushBy(GameCharacter fish, Point2D from, Point2D to, Vector2D dir ) {
		GameObject obj = findObject(to, fish.getRoom()) ;

		if ( fish instanceof BigFish ) {
			if ( obj instanceof Interactable ) {
				Point2D PlusPos = to.plus(dir);
				return ((Interactable)obj).interactWithFish(fish, to, PlusPos, dir);
			}
		}

		if ((obj instanceof MovableObject || obj instanceof NonMovableObject || obj instanceof GameCharacter) ) {
			return false;
		}
		return true;
	}
	

}
