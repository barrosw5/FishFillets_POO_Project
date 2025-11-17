package objects;

import java.util.List;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Bomb extends LightObject implements Gravity{
	private boolean controlDown = false; // Variável para controlo do Down, verifica se a bomba pode explodir

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
	public boolean isLight() {
		return true;
	}

	@Override
	public boolean canMoveLightObject(Point2D from, Point2D to, Vector2D dir, GameObject cla) {
		for ( GameObject obj1: cla.getRoom().getObjects()) {
			if ( obj1.getPosition().equals(to)) {
				if ( (obj1 instanceof NonMovableObject || obj1 instanceof MovableObject || obj1 instanceof GameCharacter)) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean canFall() {
		Point2D pos = this.getPosition();
        Point2D below = getBelow(pos);

        if (below.getY() >= 10) { 
            return false;
        }

        for (GameObject obj : this.getRoom().getObjects()) {
            if (obj.getPosition().equals(below) && !(obj instanceof Water)) {
                return false;
            }
        }
	
        return true;
	}

	@Override
	public void fall() { // Acho que finalmente conseguir por a bomba explodir como deve ser 
		
		if (canFall()){ // Verifica se pode descer ( ou começar a descida), se sim faz todo o movimento e declara a varáivel como true. 
			Point2D pos = this.getPosition();
			Point2D below = getBelow(pos);
			pushObject(this, pos, below);
			controlDown = true;
		}

		else if ( controlDown == true && ! canFall()) { // Só quando o movimento de descida foi feito e terminado, daí a variavel de controlo e o !CandDown a bomba explode
			specialAbillity();
			controlDown = false;
		}
	}

	@Override
	public void specialAbillity() {

		Point2D currentlyPos = this.getPosition();
		List<Point2D> nearObjects = getAdjacentPositions(currentlyPos);
		nearObjects.add(currentlyPos);

		Point2D beloPos = getBelow(currentlyPos);

		GameObject below = findObject(beloPos, getRoom());

		if ( below instanceof GameCharacter) {
			return;
		}

		for (Point2D pos: nearObjects) {
			GameObject remove = findObject(pos, this.getRoom());
			// Point2D removePos = pos;

			//  if ( removePos.equals(beloPos) && remove instanceof GameCharacter) {
			//  	break;
			//  }

			if (remove != null) {
				this.getRoom().removeObject(remove);
			}
		}
		

	}

}