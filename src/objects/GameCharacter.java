package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public abstract class GameCharacter extends GameObject {
	private static final boolean isMovel = true;
	private boolean direction = true; 	// true é esquerda, false é direita
	private boolean hasWon = false;	// tentar fazer com que o peixe fora do ecra nao seja mais jogavel

	public GameCharacter(Room room) {
		super(room, isMovel);
	}
	
	public void move(Vector2D dir) { 
		Point2D startPosition = getPosition();
		if (canMoveFish(startPosition, dir)) {
			if (dir.getX() > 0 && direction == true) {
                direction = false; 	// mover png para a direita
            } else if (dir.getX() < 0 && direction == false) {
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

	public void pushObject(GameObject obj, Point2D from, Point2D to) {
		obj.setPosition(to);
	}

	public void pushTwoObject(GameObject obj1, GameObject obj2, Point2D from, Point2D to1, Point2D to2) {
		obj2.setPosition(to2);
		obj1.setPosition(to1);
	}

	@Override
	public int getLayer() {
		return 2;
	}
	
	// O CanMove garante que o peixe não vai em direção ou passe algo que não é suposto
	// Está implementado como abstrato no GameCharacter 
	//cada peixe tem o seu visto que os dois têm regras movimentação diferentes
	public abstract boolean canMoveFish(Point2D pos, Vector2D dir);
}