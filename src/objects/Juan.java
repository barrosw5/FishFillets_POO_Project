package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Juan extends LightObject implements Interactable, Gravity{
    private boolean downDone = false;
    private int controlOne = 0;
    private boolean leftBlocked = false;
    private boolean rightBlocked = true;
    public Juan(Room room){
        super(room);
    }

    // Um personagem especial com movimento próprio
    @Override
    public String getName() {
        return "juan";
    }

    @Override
    public int getLayer() {
        return 1;
    }

   // Ele tem um movimento especial, que consiste no seguinte: começa por descer se puder, depois vai para a esq, sobe, desce, e vai para a esq, até não conseguir mais
   // Quando não consegue mais ir para a esq, vai para a direita e repete o procedimento até não puder mais, voltando outra vez para a esquerda 
   @Override
    public boolean canSpecialMov() {
        Point2D pos = this.getPosition();
        Point2D below = new Point2D(pos.getX(), pos.getY() + 1);
        GameObject obj = findObject(below, this.getRoom());

        if( obj == null )
            return true;

        return false;
    }

    // Primeiro desce; depois alterna entre esquerda/direita e tenta subir
    @Override
    public void specialmov() {
        if (canSpecialMov() && downDone == false) {
            Point2D pos = this.getPosition();
            Point2D below = getBelow(pos);
            pushObject(this, pos, below);
        }
        else {
            downDone = true;
        }

        if ( downDone ) {
            specialAbillity();
        } 
        
    }

    // Empurra para a esquerda se estiver livre
    private boolean leftPush(Point2D currentPos) {
        Point2D leftPos = new Point2D(currentPos.getX() - 1, currentPos.getY());
        GameObject leftObj = findObject(leftPos, getRoom());

        if ( leftObj == null) {
            pushObject(this, currentPos, leftPos);
            return true;
        }
        return false;
    }

     // Empurra para a direita se estiver livre
     private boolean rightPush(Point2D currentPos) {
        Point2D rightPos = new Point2D(currentPos.getX() + 1, currentPos.getY());
        GameObject rightObj = findObject(rightPos, getRoom());

        if ( rightObj == null) {
            pushObject(this, currentPos, rightPos);
            return true;
        }
        return false;
    }
    
    
    // Tenta deslocar-se lateralmente e depois tenta subir 
    @Override
    public void specialAbillity() {   
        if ( controlOne < 1) {
            controlOne = 1;
            if ( rightBlocked ) {
                boolean pushedLeft = leftPush(getPosition());
                if (pushedLeft) {
                    return; // fez o desvio para a esquerda; subida fica para o tick seguinte
                } else {
                    leftBlocked = true; // deteta bloqueio à esquerda e pára
                    rightBlocked = false;
                    downDone = false;
                    controlOne = 0;
                    return;
            }
            }
            else {
                boolean pushedRight = rightPush(getPosition());
                if (pushedRight) {
                    return; // fez o desvio para a esquerda; subida fica para o tick seguinte
                } else {
                    rightBlocked = true;
                    leftBlocked = false; 
                    downDone = false;
                    controlOne = 0;
                    return;
                }
            }
        }

        Point2D pos = getPosition();
        Point2D upPos = new Point2D(pos.getX(), pos.getY() -1);
        GameObject obj = findObject(upPos, this.getRoom());

        if ( obj == null) {
            pushObject(this, pos, upPos);
        }
        else {
            downDone = false;
            controlOne = 0;
            leftBlocked = false; // reset quando falha a subida
        }
    }

    // Se o BigFish o empurrar, morre; caso contrário segue regras normais de bloqueio
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

		if ( fish instanceof BigFish ) { // Se o bigFish tentar empurrá-lo morre
            fish.die(fish);
            return false;
		}

		if ( (obj instanceof MovableObject || obj instanceof NonMovableObject || obj instanceof GameCharacter) && !(obj instanceof HoledWall)) {
			return false; // Se tiver estes objetos à frente não move
		}
		return true;
	}

    // Repõe estado interno para repetir o padrão de movimento
     @Override
    public void reset(){
        super.reset();
        downDone = false;
        controlOne = 0;
        leftBlocked = false;
        rightBlocked = true;
    }
}
