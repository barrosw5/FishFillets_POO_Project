package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Direction;
import static pt.iscte.poo.utils.Direction.DOWN;
import static pt.iscte.poo.utils.Direction.LEFT;
import static pt.iscte.poo.utils.Direction.RIGHT;
import static pt.iscte.poo.utils.Direction.UP;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public abstract class GameCharacter extends GameObject{
	private static final boolean isMovel = true;
	private Direction direction = LEFT; 
	private boolean hasWon = false;		// tentar fazer com que o peixe fora do ecra nao seja mais jogavel
	private boolean hasDied = false;

	public GameCharacter(Room room) {
		super(room, isMovel);
	}
	
	public void move(Vector2D dir) { 
		Point2D startPosition = getPosition();

		if (dir == LEFT.asVector()) {
			direction = LEFT; 	// mover png para a esquerda
		} 
		else if (dir == RIGHT.asVector()) {
			direction = RIGHT;  // mover png para a direita
		}
		else if (dir == UP.asVector()) {
			direction = UP;  	// mover png para cima
		}
		else if (dir == DOWN.asVector()) {
			direction = DOWN;  	// mover png para baixo
		}

		if (fishCanMove(startPosition, dir)) {

			setPosition(startPosition.plus(dir));
			Point2D finalPosition = startPosition.plus(dir);

			if (finalPosition.getX() < 0 || finalPosition.getX() > 9 ||
				finalPosition.getY() < 0 || finalPosition.getY() > 9){
					
				hasWon = true;
    			getRoom().removeObject(this);
			}
			else
				setPosition(finalPosition);
		}
	}

	public Direction getDirection(){
		return direction;
	}

	public void setDirection(Direction d){
		this.direction = d;
	}

	public boolean hasWon(){
		return hasWon;
	}

	public boolean hasDied(){
		return hasDied;
	}

	public void setDeadState(boolean t){
		hasDied = t;
	}

	public void resetWin(){
		hasWon = false;
	}

	@Override
	public int getLayer() {
		return 2;
	}

	public void dead(GameObject remove){ 		// Função de morte ou seja objeto chama isto para matar peixe e deixa sprite de sangue
		setDeadState(true);
		remove.getRoom().addObject(new Blood(getPosition(), remove.getRoom()));
		remove.getRoom().removeObject(remove);
	}

	public static void CharacterSuppor( Room r, GameCharacter fish) {
		if ( ! fishSupport(fish)) {
			fish.dead(fish);
		}
	}
	
	// O CanMove garante que o peixe não vai em direção ou passe algo que não é suposto
	// Está implementado como abstrato no GameCharacter 
	//cada peixe tem o seu visto que os dois têm regras movimentação diferentes
	public abstract boolean fishCanMove(Point2D pos, Vector2D dir);

	public static boolean fishSupport(GameCharacter fish) {
		return fish.fishSupport();
	}

	public abstract boolean fishSupport();
}