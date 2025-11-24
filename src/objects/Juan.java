package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Juan extends LightObject implements KillAllFishes{

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
    public boolean canMoveLightObject(Point2D from, Point2D to, Vector2D dir, GameObject cla) {
        for (GameObject obj1 : cla.getRoom().getObjects()) {
				if (obj1.getPosition().equals(to)) {
					if ((obj1 instanceof MovableObject || obj1 instanceof NonMovableObject || obj1 instanceof GameCharacter)
							&& !(obj1 instanceof HoledWall)) {
						return false;
					}
				}
			}

		return true;
    }

    
    @Override
    public void specialAbillity() {
        
    }

    //testar polimorfism a partir daqui
    
}
