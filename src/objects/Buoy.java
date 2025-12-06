package objects;

import pt.iscte.poo.game.Room;
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
        Point2D pos = this.getPosition();
        Point2D up = new Point2D(pos.getX(), pos.getY() - 1);
        GameObject obj = findObject(up, this.getRoom());

        if(obj == null )
            return true;

        return false;
    }

    // Sobe se puder, se tiver algo móvel em cima ativa a habilidade de afundar
    @Override
    public void specialmov() {
        Point2D pos = this.getPosition(); // busca a pos da boia
        Point2D above = getAbove(pos); // vê a pos acima da boia 
        GameObject aboveObj = findObject(above, this.getRoom()); // procura o obj nessa posição 

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


    //  Afunda uma casa se estiver bloqueada em cima e tiver água/livre em baixo

    @Override
    public void specialAbillity() {
        Point2D pos = this.getPosition(); // encontra a pos da boia 
        Point2D below = new Point2D(pos.getX(), pos.getY() + 1); // a pos abaixo dela
        GameObject obj = findObject(below, this.getRoom()); // o obj nessa pos

         if(obj == null ) { // se não houver nenhum obj afunda uma pos.
            Point2D posDown = this.getPosition();
            Point2D belowDown = getBelow(posDown);
            pushObject(this, posDown, belowDown);
        }

    }
}
