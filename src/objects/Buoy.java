package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Buoy extends LightObject implements Interactable, Gravity{
    public Buoy(Room room) {
        super(room);
    }

    @Override
    public String getName() {
        return "buoy";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    @Override
    public boolean interactWithFish(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
       if ( canPushBy(fish, from, to, dir)) {
         pushObject(this, from, to);
         return true;
       }
       return false;
    }

    private boolean canPushBy(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
        GameObject obj = findObject(to, getRoom());

        if ( fish instanceof SmallFish && !Point2D.sameDirectionHorzontal(from, to)) {
            return false;
        }
        if (fish instanceof BigFish && dir.getY() < 0) {
            return false;
        }
        if ( obj instanceof Interactable) {
            Point2D PlusPos = to.plus(dir);
            return ((Interactable)obj).interactWithFish(fish, to, PlusPos, dir);
			}
        if ((obj instanceof NonMovableObject || obj instanceof MovableObject || obj instanceof GameCharacter)) {
            return false;
        }
        return true;
    }
    // O canSpecialMov da boia em vez de verificar o que está em baixo verifica o que está acima, basicamente 
    @Override
    public boolean canSpecialMov() {
        Point2D pos = this.getPosition();
        Point2D up = new Point2D(pos.getX(), pos.getY() - 1);
        GameObject obj = findObject(up, this.getRoom());

        if(obj == null )
            return true;

        return false;
    }

    // O SpecialMov é que é um bocado diferente
    @Override
    public void specialmov() {
        Point2D pos = this.getPosition();
        Point2D above = new Point2D(pos.getX(), pos.getY() - 1);
        Point2D below = new Point2D(pos.getX(), pos.getY() + 1);
        GameObject aboveObj = findObject(above, this.getRoom());
        GameObject belowObj = findObject(below, this.getRoom());

        // Vê em que posição a boia está e vai ver os objetos em cima e embaixo 

        // Se puder ir para cima, muito bem, vai. 
        if (canSpecialMov()) {
            pushObject(this, pos, above); 
            return;
        }

        // Se não puder e o objeto que estiver em cima de si for um Movable ativa a specialAbility
        
        if (aboveObj instanceof MovableObject) {
            specialAbillity();
        }
    }


    // A SpecialAbility é basicamente ir afundando.  

    @Override
    public void specialAbillity() {
        Point2D pos = this.getPosition();
        Point2D below = new Point2D(pos.getX(), pos.getY() + 1);
        GameObject obj = findObject(below, this.getRoom());

         if(obj == null ) {
            Point2D posDown = this.getPosition();
            Point2D belowDown = getBelow(posDown);
            pushObject(this, posDown, belowDown);
        }

    }
}
