package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public class Explosion extends GameObject{

    // Variável para saber se a explosão é "recente"
    private int lifeTimer = 1;

    public Explosion(Point2D position, Room room) {
        super(position, room);
    }

    @Override
    public String getName() {
        return "explosion";
    }

    @Override
    public int getLayer() {
        return 2;
    }

    // Gere todas as explosões da sala e remove as que já passaram o tempo de vida
    public static void update(Room room) {
        room.getObjects().removeIf(obj -> {
            if (obj instanceof Explosion) {
                Explosion e = (Explosion) obj;
                e.lifeTimer--;
                return e.lifeTimer <= 0; // Remove se a vida chegar a 0
            }
            return false;
        });
    }

    // Num reset também desaparece
    @Override
    public void reset(){
        getRoom().removeObject(this);
    }
}
