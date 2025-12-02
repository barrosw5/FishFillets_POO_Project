package objects;

import pt.iscte.poo.game.Room;

public abstract class MovableObject extends GameObject {
    private static boolean isMovable = true;
    private boolean controlDown = false; 
    
    public MovableObject(Room room) {
        super(room, isMovable);
    }

    public void changeMovability(){
        isMovable = !isMovable;
    }

    @Override
    public void reset() {
        super.reset();
        controlDown = false;    // para a bomba nao pensar que ainda está a cair
    }

}
