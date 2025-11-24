package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Pikachu extends GameCharacter{

    private static Pikachu pk = new Pikachu(null);
    private static final String pikachuImage = "picachu";

    public Pikachu(Room room){
        super(room);
    }

    public static Pikachu getInstance() {
		return pk;
	}

    @Override
	public String getName() {
		return pikachuImage;
	}

    @Override
	public int getLayer() {
		return 2;
	}

    @Override       // o bicho é forte
    public boolean fishSupport() {
        return true;
    }

    @Override
    public boolean fishCanMove(Point2D pos, Vector2D dir) {
        if(hasWon())
			return false;

        Point2D finalPos = pos.plus(dir);

        for (GameObject obj : getRoom().getObjects()) {
			if (obj.getPosition().equals(finalPos)) {

				if (obj instanceof Water) {
					continue;
				}
                else{
                    return false;
                }
			}
		}

        return true;
    }
    
}
