package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public abstract class MovableObject extends GameObject implements Resettable{
    private static boolean isMovable = true;
    private Point2D startingPosition;
    
    public MovableObject(Room room) {
        super(room, isMovable);
    }

    public void changeMovability(){
        isMovable = !isMovable;
    }

    public abstract boolean isLight();

    public Point2D getStartingPosition(){
        return startingPosition;
    }

    public void setStartingPosition(Point2D sp){
        startingPosition = sp;
    }

    @Override
    public void setPosition(Point2D position) {     // AVERIGUAR ESTE USO
        super.setPosition(position);
        // Regista automaticamente a posição inicial
        if (getStartingPosition() == null) {
            setStartingPosition(position);
        }
    }

    @Override
    public void reset() {
        if (startingPosition != null) {
        Room r = getRoom();
        if (!r.getObjects().contains(this)) {
            r.addObject(this);
        }
        setPosition(startingPosition);
    }
    }
}
