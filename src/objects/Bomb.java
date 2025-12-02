package objects;

import java.util.List;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Bomb extends LightObject implements Interactable, Gravity{
	private boolean controlDown = false; 

	public Bomb(Room room) {
		super(room);
	}

	@Override
	public String getName() {
		return "bomb";
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
    public boolean canSpecialMov() {
        Point2D pos = this.getPosition();
        Point2D below = new Point2D(pos.getX(), pos.getY() + 1);
        GameObject obj = findObject(below, this.getRoom());

        if(obj == null )
            return true;

        return false;
    }

    @Override
    public void specialmov() {
        if (canSpecialMov()) {
            Point2D pos = this.getPosition();
            Point2D below = getBelow(pos);
            pushObject(this, pos, below);
            controlDown = true;
        }

        else if (controlDown && !canSpecialMov()) {
            specialAbillity();
            controlDown = false;
        }
    }

	@Override
	public void specialAbillity() {

		Point2D currentPos = this.getPosition();
		List<Point2D> nearObjects = getAdjacentPositions(currentPos);
		nearObjects.add(currentPos);

		Point2D beloPos = getBelow(currentPos);

		GameObject below = findObject(beloPos, getRoom());

		if ( below instanceof GameCharacter) {
			return;
		}

		for (Point2D pos: nearObjects) {
			GameObject target = findObject(pos, this.getRoom());

			if (target instanceof GameCharacter) {
				((GameCharacter) target).die(target);
			}

			if (target != null) {
				this.getRoom().removeObject(target);
			}
			this.getRoom().addObject(new Explosion(pos, this.getRoom()));
		}
		

	}

	@Override
	public boolean interactWithFish(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
		if ( BombPushBy(fish, from, to, dir)) {
			pushObject(this, from, to);
			return true;
		}
		return false;
	}

	public boolean BombPushBy(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
		GameObject obj = findObject(to, fish.getRoom());

		if ( fish instanceof BigFish ) {
			if ( obj instanceof Interactable ) {
				Point2D PlusPos = to.plus(dir);
				return ((Interactable)obj).interactWithFish(fish, to, PlusPos, dir);
			}
		}

		if ( obj instanceof NonMovableObject || obj instanceof MovableObject || obj instanceof GameCharacter) {
			return false;
		}
		return true;
	}

}

 