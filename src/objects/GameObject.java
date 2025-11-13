package objects;

import java.util.ArrayList;
import java.util.List;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public abstract class GameObject implements ImageTile {

	private Point2D position;
	private Room room;
	private boolean isMovable;

	public GameObject(Room room, boolean isMovel) {
		this.room = room;
		this.isMovable = isMovel;
	}

	public GameObject(Point2D position, Room room) {
		this.position = position;
		this.room = room;
	}

	public void setPosition(int i, int j) {
		position = new Point2D(i, j);
	}

	public void setPosition(Point2D position) {
		this.position = position;
	}

	@Override
	public Point2D getPosition() {
		return position;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public boolean getIsMovel() {
		return isMovable;
	}

	public void pushObject(GameObject obj, Point2D from, Point2D to) {
		obj.setPosition(to);
	}

	public void pushTwoObject(GameObject obj1, GameObject obj2, Point2D from, Point2D to1, Point2D to2) {
		obj2.setPosition(to2);
		obj1.setPosition(to1);
	}


	public static void GravityMove(Room r) {
		List<Gravity> gravityObjects = new ArrayList<>();

		for ( GameObject obj: r.getObjects())  {
			if ( obj instanceof Gravity ) {
				gravityObjects.add((Gravity) obj);
			}
		}

			for ( Gravity g: gravityObjects) {
				if ( g.canDown()) {
					g.down();
				}
			}
	
	} 

	// ---------------------------------------
	// Método canMove
	// ---------------------------------------
	public boolean canMoveLightObject(Point2D form, Point2D to, Vector2D dir,GameObject cla){
		return false;
	}

	public boolean canMoveNonLight(Point2D form, Point2D to, Vector2D dir,GameObject cla){
		return false;
	}
	@Override
	public String toString(){
		return "| " + this.getName() + " in position: " + this.getPosition() + " |";
	}
}
