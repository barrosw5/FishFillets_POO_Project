package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Key extends LightObject implements Interactable, Gravity{
    private boolean controlDown = false;

    public Key(Room r){
        super(r);
    }

    @Override
    public String getName() {
        return "key";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    @Override
    public boolean interactWithFish(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
        GameObject targetObj = findObject(to, this.getRoom());

        if(targetObj instanceof Gate){
            getRoom().removeObject(targetObj);
            getRoom().removeObject(this);
            return true;
        }

        if(targetObj == null){
            pushObject(this, from, to);
            return true;
        }
        return false;
    }


    @Override
    public boolean canSpecialMov() {
        Point2D belowPos = this.getBelow(this.getPosition());
        if(findObject(belowPos, this.getRoom()) == null)
            return true;
        return false;
    }

    @Override
    public void specialmov() {
        if(canSpecialMov()){
            pushObject(this, this.getPosition(), this.getBelow(this.getPosition()));
        }

        else if (controlDown && !canSpecialMov()) { // se não, ativa a habilidade especial, neste caso nenhuma
            specialAbillity();
            controlDown = false;
        }
    }

    @Override
    public void specialAbillity() {
    }

}
