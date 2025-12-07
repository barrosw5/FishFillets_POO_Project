package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class SpinningWheel extends NonMovableObject implements Interactable{

    public SpinningWheel(Room room) {
        super(room);
    }

    @Override
    public String getName() {
        return "spinningWheel";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    @Override
    public boolean interactWithFish(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
        fish.spin();
        return true;
    }

    
}
