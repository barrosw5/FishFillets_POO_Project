package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Buoy extends LightObject implements Interactable, Gravity{
    public Buoy(Room room) {
        super(room);
    }

    // Boia leve que flutua
    @Override
    public String getName() {
        return "buoy";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    // Interação com o peixe 
    @Override
    public boolean interactWithFish(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
       if ( canPushBy(fish, from, to, dir)) { // Se pode ser empurrado, empurra 
         pushObject(this, from, to);
         return true;
       }
       return false;
    }

    private boolean canPushBy(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
        GameObject obj = findObject(to, getRoom()); // Verifica o objeto à frente da boia

        if ( fish instanceof SmallFish && !Point2D.sameDirectionHorzontal(from, to)) { // Se o empurra não for na horizontal 
            return false;
        }
        if ( obj instanceof Interactable) { // Se o obj for interável usa a recursividade 
            Point2D PlusPos = to.plus(dir);
            return ((Interactable)obj).interactWithFish(fish, to, PlusPos, dir);
			}
        if ((obj instanceof NonMovableObject || obj instanceof MovableObject || obj instanceof GameCharacter)) { // Se for um obj pertencente a estas classes não pode ser empurrado 
            return false;
       }
       return true;
    }

    //  verifica o espaço acima em vez de abaixo
    @Override
    public boolean canSpecialMov() {
        Point2D pos = this.getPosition(); // Pega a pos do objeto 
        Point2D below = new Point2D(pos.getX(), pos.getY() + 1); // a posição abaico do obj
        GameObject obj = findObject(below, this.getRoom()); // e o obj abaixo da boia

        if(obj == null ) // se for vazio pode descer, caso contrário não 
            return true;

        return false;
    }

    // Sobe se puder, se tiver algo móvel em cima ativa a habilidade de afundar
    @Override
    public void specialmov() {
        Point2D pos = this.getPosition(); // busca a pos da boia
        Point2D below = getBelow(pos);

        // Vê em que posição a boia está e vai ver os objetos em cima e embaixo 

        // Se puder ir para cima, muito bem, vai. 
        if (canSpecialMov()) {
            pushObject(this, pos, below);
        }

        specialAbillity();
    }

    @Override
    public void specialAbillity() {
        Direction dir = Math.random() < 0.5 ? Direction.LEFT : Direction.RIGHT; // Se for menos que 0,5 LEFT, caso contrário RIGHT
        Vector2D vec = dir.asVector(); // define um vetor 
        Point2D currentPos = getPosition(); // a pos atual do krab 
        Point2D targetPos = currentPos.plus(vec); // e a pos para onde vai 
        GameObject target = findObject(targetPos, getRoom());
        
        if (target == null) {
            pushObject(this, currentPos, targetPos);
        }
        else{
            if(dir == Direction.LEFT){
                target = findObject(currentPos.plus(Direction.RIGHT.asVector()), getRoom());
                if(target == null)
                    pushObject(this, getPosition(), currentPos.plus(Direction.RIGHT.asVector()));
            }
            else{
                target = findObject(currentPos.plus(Direction.LEFT.asVector()), getRoom());
                if(target == null)
                    pushObject(this, getPosition(), currentPos.plus(Direction.LEFT.asVector()));
            }
        }
    }
}
