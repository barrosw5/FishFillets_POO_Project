package objects;

import pt.iscte.poo.game.Room;

public abstract class MovableObject extends GameObject {
    private static boolean isMovable = true;
    
    public MovableObject(Room room) {
        super(room, isMovable);
    }

    // Permite alternar o estado global de movimentação dos móveis (se for necessário bloquear tudo)
    public void changeMovability(){
        isMovable = !isMovable;
    }

    @Override
    public void reset() {
        super.reset();
    }

}
