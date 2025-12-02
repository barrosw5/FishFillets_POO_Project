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

    @Override
    public String getName() {
        return "juan";
    }

    @Override
    public int getLayer() {
        return 1;
    }

   @Override
    public boolean canSpecialMov() {
        Point2D pos = this.getPosition();
        Point2D below = getBelow(pos);
        GameObject obj = findObject(below, this.getRoom());

        if( obj == null )
            return true;

        return false;
    }

    // logica do juan: tenta cair, se nao der tenta desviar
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

    private boolean leftPush(Point2D currentPos) {
        Point2D leftPos = new Point2D(currentPos.getX() - 1, currentPos.getY());
        GameObject leftObj = findObject(leftPos, getRoom());

        if ( leftObj == null) {
            pushObject(this, currentPos, leftPos);
            return true;
        }
        return false;
    }

     private boolean rightPush(Point2D currentPos) {
        Point2D rightPos = new Point2D(currentPos.getX() + 1, currentPos.getY());
        GameObject rightObj = findObject(rightPos, getRoom());

        if ( rightObj == null) {
            pushObject(this, currentPos, rightPos);
            return true;
        }
        return false;
    }
    
    // tenta ir para os lados se estiver bloqueado
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
        Point2D upPos = getAbove(pos);
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
            fish.die(fish);
            return false;
		}

		if ( (obj instanceof MovableObject || obj instanceof NonMovableObject || obj instanceof GameCharacter) && !(obj instanceof HoledWall)) {
			return false;
		}
		return true;
	}

     @Override
    public void reset(){
        super.reset();
        downDone = false;
        controlOne = 0;
        leftBlocked = false;
        rightBlocked = true;
    }
}
