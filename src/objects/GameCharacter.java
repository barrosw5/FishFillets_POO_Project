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
    private boolean spinned = false;

    public GameCharacter(Room room) {
        super(room, true); // true = isMovable
    }
    

    // Função move dos "personagens": basicamente dos dois peixes. 
    public void move(Vector2D vec) { 
        if(hasSpinned()){
            vec = new Vector2D(vec.getX() * -1, vec.getY() * -1);
        }

        if (vec == null) { // Se o vetor for nulo ele lança logo uma exceção. 
            throw new IllegalArgumentException("O vetor de movimento não pode ser nulo.");
        }
        Point2D startPosition = getPosition(); // Vai muscar a posição em que o peixe está com o getPosition
        updateDirection(vec); // Atualiza a direção . Exemplo: o vector é LEFT.asVector, então atualiza-se a direction para LEFT. Direction é uma variável definida no incio desta classe

        if (fishCanMove(startPosition, vec)) { // Se o peixe se puder mover, move
            Point2D nextPos = startPosition.plus(vec);  // Encontra-se a nova pos

            if (isOutOfBounds(nextPos)) { // Se tiver fora do "terreno" a próxima pos, significa que aquele peixe "já venceu", caso contrário, o peixe move-se para lá 
                hasWon = true;
                if (getRoom() != null) {
                    getRoom().removeObject(this);
                }
            } else {
                setPosition(nextPos);
            }
        }
    }

    // Atualiza a direção do peixe 

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

    public boolean hasSpinned() { return spinned; }
    public void resetSpinned() { spinned = false; }

    public void spin(){
        spinned = ! spinned;
    }

    @Override
    public void die(GameObject killed) {
        super.die(killed);
        setDeadState(true);
    }

    @Override
    public int getLayer() { return 2; }

    // --- logica peixes ---


    // Verifica se o peixe pode viver ou não. Ela é chamada a cada Tick (tempo do jogo)

    public static void checkSurvivalStatus(Room r, GameCharacter fish) {
        if (fish != null && !fish.fishSupport()) {
            fish.die(fish);
        }
    }

    // Funções abstrastra presente em cada peixe

    public abstract boolean fishCanMove(Point2D pos, Vector2D dir);
    public abstract boolean fishSupport();
    public abstract boolean canPassSmallSpaces();
    public abstract boolean canPushHeavyObjects();
    
}