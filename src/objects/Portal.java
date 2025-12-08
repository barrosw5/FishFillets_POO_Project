package objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Portal extends NonMovableObject implements Interactable{
    private final int MAXLENGTH = 9;
    private final int MINLENGTH = 1;

    public Portal(Room room) {
        super(room);
    }

    @Override
    public String getName() {
      return "portal";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    @Override
    public boolean interactWithFish(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
        Point2D target = searchPosPortal();
        if ( target != null) {
            pushObject(fish, from, target);
            return false;
        }
        return true; 
    }

    // public Point2D findRandomPos(){
    //     int range = MAXLENGTH - MINLENGTH;
    //     Random generatePos = new Random();
    //     int XPos = generatePos.nextInt(range) + MINLENGTH;
    //     int YPos = generatePos.nextInt(range) + MINLENGTH;
        
    //     Point2D finalPos = new Point2D(XPos, YPos);
    //     GameObject obj1 = findObject(finalPos, getRoom());

    //     if ( obj1 != null) {
    //         return findRandomPos();  
    //     }
    //     return finalPos;
    // }

    public Point2D searchPosPortal() {
        Point2D currentlyPos = getPosition();
        List<Portal> portals = new ArrayList<>();
        for ( GameObject obj: getRoom().getObjects()) {
            if ( obj instanceof Portal && obj.getPosition() != currentlyPos) {
                portals.add((Portal) obj);
            }
        }
        int range = portals.size();
        Random generatePos = new Random();
        int index = generatePos.nextInt(range);
        Portal p = portals.get(index);

        return p.getPosition();
    }

    
}
