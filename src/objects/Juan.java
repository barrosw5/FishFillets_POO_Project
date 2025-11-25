package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Juan extends LightObject implements Interactable{

    public Juan(Room room){
        super(room);
    }

    @Override
    public String getName() {
        return "juan";
    }

    @Override
    public int getLayer() {
        return 1;
    }
    
    @Override
    public void specialAbillity() {

        
        
    }

    @Override
    public boolean interactWithFish(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
        	if ( JuanPushBy(fish, from, to, dir)) {
			pushObject(this, from, to);
			return true;
		}
		return false;
    }

    public boolean JuanPushBy(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
		GameObject obj = findObject(to, fish.getRoom());

		if ( fish instanceof BigFish ) {
            fish.setDeadState(true);
			this.getRoom().removeObject(fish);
            return false;
		}

		if ( (obj instanceof MovableObject || obj instanceof NonMovableObject || obj instanceof GameCharacter) && !(obj instanceof HoledWall)) {
			return false;
		}
		return true;
	}
    //testar polimorfism a partir daqui
    
}
