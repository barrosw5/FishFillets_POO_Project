package objects;

import java.util.ArrayList;
import java.util.List;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Krab extends LightObject implements Interactable, Gravity{
    private boolean controlDown = false;

    public Krab(Room room, Point2D pos){
        super(room);
        setPosition(pos);
    }

    @Override
    public String getName() {
        return "krab";
    }

    @Override
    public int getLayer() {
        return 2;
    }

    // --- Logica de Movimento Automático ---

    public void move() {
        Direction dir = Math.random() < 0.5 ? Direction.LEFT : Direction.RIGHT;
        Vector2D vec = dir.asVector();
        Point2D currentPos = getPosition();
        Point2D targetPos = currentPos.plus(vec);

        if (!canMoveTo(targetPos)) {
            return;
        }

        GameObject targetObj = findObject(targetPos, getRoom());

        if (targetObj instanceof GameCharacter) {
            handleFishInteraction((GameCharacter) targetObj);
            if (!getRoom().getObjects().contains(this))
                return;
        }

        if (targetObj instanceof Trap) {
            this.die(this);
            return;
        }

        if (targetObj == null || targetObj instanceof Water || targetObj instanceof HoledWall) {
            setPosition(targetPos);
        }
    }

    private boolean canMoveTo(Point2D pos) {
        if (isOutOfBounds(pos))
            return false;
        
        GameObject obj = findObject(pos, getRoom());
        
        if (obj == null || obj instanceof Water || obj instanceof HoledWall || obj instanceof GameCharacter || obj instanceof Trap) {
            return true;
        }
        return false;
    }

    // --- Lógica de Colisão (Peixe entra no Caranguejo ou Caranguejo entra no Peixe) ---

    private void handleFishInteraction(GameCharacter fish) {
        if (fish instanceof SmallFish) {
            fish.die(fish);
        } else if (fish instanceof BigFish) {
            this.die(this);
        }
    }

    // --- Interactable: Quando o peixe tenta entrar na casa do caranguejo ---

    @Override
    public boolean interactWithFish(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
        handleFishInteraction(fish);
        return false; // nao pode ser empurrado
    }

    // --- Método Estático para mover todos os caranguejos da sala ---
    
    public static void moveAllKrabs(Room r) {
        List<Krab> krabs = new ArrayList<>();
        for (GameObject obj : r.getObjects()) {
            if (obj instanceof Krab) {
                krabs.add((Krab) obj);
            }
        }
        for (Krab k : krabs) {
            k.move();
        }
    }

     @Override
    public boolean canSpecialMov() {
        Point2D pos = this.getPosition();
        Point2D below = new Point2D(pos.getX(), pos.getY() + 1);
        GameObject obj = findObject(below, this.getRoom());

        if(obj == null )
            return true;

        return false;
    }

    @Override
    public void specialmov() {
        if (canSpecialMov()) {
            Point2D pos = this.getPosition();
            Point2D below = getBelow(pos);
            pushObject(this, pos, below);
            controlDown = true;
        }

        else if (controlDown && !canSpecialMov()) {
            specialAbillity();
            controlDown = false;
        }
    }

    @Override
    public void specialAbillity() {
    }

    @Override
    public void reset(){
        getRoom().removeObject(this);
    }
    
}
