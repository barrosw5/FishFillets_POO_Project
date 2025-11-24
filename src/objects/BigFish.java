package objects;

import java.util.ArrayList;
import java.util.List;
import pt.iscte.poo.game.Room;
import static pt.iscte.poo.utils.Direction.DOWN;
import static pt.iscte.poo.utils.Direction.LEFT;
import static pt.iscte.poo.utils.Direction.RIGHT;
import static pt.iscte.poo.utils.Direction.UP;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class BigFish extends GameCharacter {

	private static BigFish bf = new BigFish(null);
	private static final String bfNameLeft = "bigFishLeft";
	private static final String bfNameRight = "bigFishRight";
	private static final String bfNameUp = "bigFishUp";
	private static final String bfNameDown = "bigFishDown";
	
	private BigFish(Room room) {
		super(room);
	}

	public static BigFish getInstance() {
		return bf;
	}
	
	@Override
	public String getName() {
		if(getDirection() == LEFT)
			return bfNameLeft;
		if(getDirection() == RIGHT)
			return bfNameRight;
		if(getDirection() == UP)
			return bfNameUp;
		if(getDirection() == DOWN)
			return bfNameDown;
		return null;
	}

	@Override
	public int getLayer() {
		return 2;
	}


	// Consegue fazer o movimento do peixe grande: na horizontal ele tem duas opções: ou empurra dois ou um só. 
	// Na vertical só consegue empurrar um 
	// O código ainda, creio eu, pode ser mais descentralizado, ou seja, criar mini funções, farei isso depois. Para já está funcional. 
	// A gravidade está em construção...
	@Override
	public boolean fishCanMove(Point2D pos, Vector2D dir) {
		if (hasWon())
			return false;

		Point2D finalPos = pos.plus(dir);
		
		for ( GameObject obj: this.getRoom().getObjects() ) {
			if ( obj.getPosition().equals(finalPos)) { // Verifica se existe algum objeto na sua frente 
				if (Point2D.sameDirectionHorzontal(pos, finalPos)) { // Se sim, vê se a direção que o user que seguir é na horizontal com a função q criei e está no Point2D
					Point2D firstObjTarget = finalPos.plus(dir); // Primeiro objeto 
					Point2D secondObjTarget = firstObjTarget.plus(dir); // Segundo objeto 

					if (obj instanceof MovableObject) { // Se o primeiro for móvel vai verificar o segundo e tal 
						for (GameObject obj2 : this.getRoom().getObjects()) {
							if (obj2.getPosition().equals(firstObjTarget)) {
								if (obj2 instanceof MovableObject &&
									(obj2.canMoveLightObject(firstObjTarget, secondObjTarget, dir, this) ||
									obj2.canMoveHeavyObject(firstObjTarget, secondObjTarget, dir, this))) {
									pushTwoObject(obj, obj2, pos, firstObjTarget, secondObjTarget); // Ele usa a função pushTwoObject que criei e que está na classe GameCharacter
									return true;
								}
							}
						}
					}
				}
				
				if ( obj instanceof MovableObject && (obj.canMoveHeavyObject(finalPos, finalPos.plus(dir), dir, this) || obj.canMoveLightObject(finalPos, finalPos.plus(dir), dir, this))) {
						Point2D FinalObPos = finalPos.plus(dir); // Se for só um objeto ele faz um processo semelhante, mas mais simples 
						pushObject(obj, finalPos, FinalObPos);				
					return true;
				
				}

				if ( obj instanceof KillBigFish  || obj instanceof KillAllFishes) { // adicionado a função que mata o peixe grande quando este toca na Trap 
					dead(bf, this);
				}

				if ( (obj instanceof MovableObject || obj instanceof NonMovableObject || obj instanceof GameCharacter ) ) {
					return false; // Restrições habituais do movimento do peixe grande 
				}
			}
		}

		return true;
	
	}

	@Override
	public boolean fishSupport() {
		Point2D CurrentlyPos = getPosition();
		int lengthPos = CurrentlyPos.getY();

		List<Point2D> posUp = new ArrayList<>(); 

		for ( int i = lengthPos - 1; i > 0; i--) {
			posUp.add( new Point2D(CurrentlyPos.getX(), i));
		}

		int countHeavy = 0;
		int counLight = 0;

		for ( Point2D objPos: posUp) {
			GameObject obj = findObject(objPos, getRoom());

			if ( !( obj instanceof MovableObject) && countHeavy == 0 && counLight == 0) {
				return true;
			}

			if ( obj instanceof HeavyObject) {
				countHeavy ++;
			}

			if ( obj instanceof LightObject) {
				counLight ++;
			}
		}
		
		if ( countHeavy > 1) {
			return false;
		}
		return true;
	}


}
