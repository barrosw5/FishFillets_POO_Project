package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Direction;
import static pt.iscte.poo.utils.Direction.*;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public abstract class GameCharacter extends GameObject {
    
    private Direction direction = LEFT; 
    private boolean hasWon = false;
    private boolean hasDied = false;

    public GameCharacter(Room room) {
        super(room, true); // true = isMovable
    }
    
    public void move(Vector2D vec) { 
        if (vec == null) {
            throw new IllegalArgumentException("O vetor de movimento não pode ser nulo.");
        }

        Point2D startPosition = getPosition();
        updateDirection(vec);

        if (fishCanMove(startPosition, vec)) {
            Point2D nextPos = startPosition.plus(vec);

            if (isOutOfBounds(nextPos)) {
                hasWon = true;
                if (getRoom() != null) {
                    getRoom().removeObject(this);
                }
            } else {
                setPosition(nextPos);
            }
        }
    }

    private void updateDirection(Vector2D vec) {
        if (vec.equals(LEFT.asVector())) direction = LEFT;
        else if (vec.equals(RIGHT.asVector())) direction = RIGHT;
        else if (vec.equals(UP.asVector())) direction = UP;
        else if (vec.equals(DOWN.asVector())) direction = DOWN;
    }

    // --- funçoes para estados ---

    public Direction getDirection() { 
        return direction; 
    }

    public void setDirection(Direction d) {
        if (d == null) throw new IllegalArgumentException("A direção não pode ser nula.");
        this.direction = d;
    }

    public boolean hasWon() { return hasWon; }
    public void resetWin() { hasWon = false; }

    public boolean hasDied() { return hasDied; }
    public void setDeadState(boolean t) { hasDied = t; }

    public void dead(GameObject killer) {
        if (killer == null) return;

        setDeadState(true);
        Room r = killer.getRoom();
        
        if (r != null) {
            r.addObject(new Blood(getPosition(), r));   // Deixa sangue
            r.removeObject(killer);                   // Remove o corpo
        }
    }

    @Override
    public int getLayer() { return 2; }

    // --- logica peixes ---

    public static void checkSurvivalStatus(Room r, GameCharacter fish) {
        if (fish != null && !fish.fishSupport()) {
            fish.dead(fish);
        }
    }

    public abstract boolean fishCanMove(Point2D pos, Vector2D dir);
    public abstract boolean fishSupport();
}