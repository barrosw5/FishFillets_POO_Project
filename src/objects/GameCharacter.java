package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public abstract class GameCharacter extends GameObject{
	private static final boolean isMovel = true;
	private boolean direction = true; 	// true é esquerda, false é direita
	private boolean hasWon = false;	// tentar fazer com que o peixe fora do ecra nao seja mais jogavel

	public GameCharacter(Room room) {
		super(room, isMovel);
	}
	
	public void move(Vector2D dir) { 
		Point2D startPosition = getPosition();
		if (fishCanMove(startPosition, dir)) {
			if (dir.getX() > 0 && direction == true) {
                direction = false; 	// mover png para a direita
            } 
			else if (dir.getX() < 0 && direction == false) {
                direction = true;  	// mover png para a esquerda
            }
			setPosition(startPosition.plus(dir));		
			Point2D finalPosition = startPosition.plus(dir);

			if (finalPosition.getX() < 0 || finalPosition.getX() > 9 ||
				finalPosition.getY() < 0 || finalPosition.getY() > 9){
					
				hasWon = true;
				// remove peixe da sala
    			getRoom().removeObject(this);
			}
			else
				setPosition(finalPosition);
		}
	}

	public boolean getDirection(){
		return direction;
	}

	public boolean hasWon(){
		return hasWon;
	}

	public void resetWin(){
		hasWon = false;
	}


	@Override
	public int getLayer() {
		return 2;
	}

	public void dead(GameObject remove, GameObject cla){ // Criada a função que vai, basicamente, matar os pexies, removendo-os do Room
		cla.getRoom().removeObject(remove);
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