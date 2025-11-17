package objects;

import java.util.ArrayList;
import java.util.List;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public abstract class GameObject implements ImageTile {

	private Point2D position;
	private Point2D startingPosition;
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

		if (getStartingPosition() == null) {
            setStartingPosition(position);
        }
	}

	@Override
	public Point2D getPosition() {
		return position;
	}

	public Point2D getBelow(Point2D pos){
		return new Point2D(pos.getX(), pos.getY() + 1);
	}

	public List<Point2D> getAdjacentPositions(Point2D pos) {
		List<Point2D> adj = new ArrayList<>();

		adj.add(new Point2D(pos.getX() + 1, pos.getY())); // direita
		adj.add(new Point2D(pos.getX() - 1, pos.getY())); // esquerda
		adj.add(new Point2D(pos.getX(), pos.getY() - 1)); // cima
		adj.add(getBelow(pos));                           // baixo

		return adj;
	}

	public Point2D getStartingPosition(){
        return startingPosition;
    }

    public void setStartingPosition(Point2D sp){
        startingPosition = sp;
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

	public void reset() {
        if (getStartingPosition() != null) {
            Room r = getRoom();
            if (!r.getObjects().contains(this)) {
                r.addObject(this);
            }
            setPosition(getStartingPosition());
        }
    }

	public void pushObject(GameObject obj, Point2D from, Point2D to) {
		obj.setPosition(to);
	}

	public void pushTwoObject(GameObject obj1, GameObject obj2, Point2D from, Point2D to1, Point2D to2) {
		obj2.setPosition(to2);
		obj1.setPosition(to1);
	}


	public static void GravityMove(Room r) {
		List<Gravity> gravityObjects = new ArrayList<>(); // Criação de uma lista só para os objetos que contêm a interface gravidade

		for ( GameObject obj: r.getObjects())  {
			if ( obj instanceof Gravity ) {
				gravityObjects.add((Gravity) obj); // Percorro toda a room e os objetos contidos nela e os que forem "gravidade" adiciono à lista criada.
			} 
		}
	
		for ( Gravity g: gravityObjects) {
			g.fall();
		}
	
	}

	public static GameObject findObject(Point2D pos, Room r) {
		for (GameObject obj : r.getObjects()) {
			if ( obj.getPosition().equals(pos) && !(obj instanceof Water)) {
				return obj;
			}
		}
		return null;
	}

	public static void removeObject(GameObject obj1, Room r) {
		r.getObjects().remove(obj1);
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
