package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Bomb extends isLightObject implements Gravity{
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
				if ( (obj1 instanceof NonMovable || obj1 instanceof MovableObject || obj1 instanceof GameCharacter)) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean canDown() {
		Point2D pos = this.getPosition();
        Point2D below = new Point2D(pos.getX(), pos.getY() + 1);

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
	public void down() { // Acho que finalmente conseguir por a bomba explodir como deve ser 
		
		if ( canDown()){ // Verifica se pode descer ( ou começar a descida), se sim faz todo o movimento e declara a varáivel como true. 
				Point2D pos = this.getPosition();
				Point2D below = new Point2D(pos.getX(), pos.getY() + 1);
				pushObject(this, pos, below);
				controlDown = true;
		}

		else if ( controlDown == true && ! canDown()) { // Só quando o movimento de descida foi feito e terminado, daí a variavel de controlo e o !CandDown a bomba explode
			Explosion();
			controlDown = false;
		}
	}

	@Override
	public void Explosion() {

		Point2D currentlyPos = this.getPosition();
		Point2D rightPos = new Point2D(currentlyPos.getX() + 1, currentlyPos.getY());
		Point2D leftPos = new Point2D(currentlyPos.getX() + -1, currentlyPos.getY());
		Point2D upPos = new Point2D(currentlyPos.getX(), currentlyPos.getY() -1);
		Point2D belowPos = new Point2D(currentlyPos.getX(), currentlyPos.getY() +1);

		Point2D[] nearObject = { currentlyPos, rightPos, leftPos,belowPos , upPos};
	    


		for ( Point2D pos: nearObject) {
			GameObject remove = GameObject.findObject(pos, this.getRoom());

			if ( remove instanceof Water) {
				continue;
			}

			if ( remove != null) {
				// GameObject.removeObject(remove, this.getRoom());
				this.getRoom().removeObject(remove);
			}
		}
		

	}

}