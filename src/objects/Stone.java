package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Stone extends HeavyObject implements Interactable, Gravity{
	private boolean controlDown = false;
	private boolean krabSpawned = false;

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
	public void reset(){
		super.reset();
		krabSpawned = false;
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
        Point2D belowPos = getBelow(currentPos);
        GameObject target = findObject(belowPos, getRoom());

        if (target instanceof SmallFish) {
            ((SmallFish) target).die(target);
        }
		else if (target instanceof Trunk) {
            getRoom().removeObject(target);
        }
	}

	@Override
	public boolean interactWithFish(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
		if (canBePushed(fish, from, to, dir)) {
			pushObject(this, from, to);

			if(dir.getY() == 0 && !krabSpawned){
				trySpawnKrab(from);
			}
			return true;
		}
		
		return false;
	}

	public void trySpawnKrab(Point2D stonePos) {
		Point2D abovePos = new Point2D(stonePos.getX(), stonePos.getY()-1);

		if(isOutOfBounds(abovePos))
			return;
	
		GameObject objAbove = findObject(abovePos, getRoom());

		if(objAbove == null || objAbove instanceof Water){
			Krab krab = new Krab(getRoom(), abovePos);
			getRoom().addObject(krab);
			krabSpawned = true;
		}
	}

	public boolean canBePushed(GameCharacter fish, Point2D from, Point2D to, Vector2D dir ) {
		GameObject obj = findObject(to, fish.getRoom()) ;

		if ( !(fish instanceof BigFish)) {
			return false; 
		}

		if ( obj instanceof Interactable ) {
			Point2D plusPos = to.plus(dir);
			return ((Interactable)obj).interactWithFish(fish, to, plusPos, dir);
		}

		if ((obj instanceof MovableObject || obj instanceof NonMovableObject || obj instanceof GameCharacter) ) {
			return false;
		}
		return true;
	}
}
