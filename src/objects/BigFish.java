package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class BigFish extends GameCharacter {

	private static BigFish bf = new BigFish(null);
	private static final String bfNameLeft = "bigFishLeft";
	private static final String bfNameRight = "bigFishRight";
	
	private BigFish(Room room) {
		super(room);
	}

	public static BigFish getInstance() {
		return bf;
	}
	
	@Override
	public String getName() {
		return getDirection() ? bfNameLeft : bfNameRight;
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
	public boolean canMoveFish(Point2D pos, Vector2D dir) {
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
									obj2.canMoveNonLight(firstObjTarget, secondObjTarget, dir, this))) {
									pushTwoObject(obj, obj2, pos, firstObjTarget, secondObjTarget); // Ele usa a função pushTwoObject que criei e que está na classe GameCharacter
									return true;
								}
							}
						}
					}
				}
				
				if ( obj instanceof MovableObject && (obj.canMoveNonLight(finalPos, finalPos.plus(dir), dir, this) || obj.canMoveLightObject(finalPos, finalPos.plus(dir), dir, this) )) {
						Point2D FinalObPos = finalPos.plus(dir); // Se for só um objeto ele faz um processo semelhante, mas mais simples 
						pushObject(obj, finalPos, FinalObPos);				
					return true;
				
				}

				if ( (obj instanceof MovableObject || obj instanceof NonMovable || obj instanceof GameCharacter ) ) {
					return false; // Restrições habituais do movimento do peixe grande 
				}
			}
		}

		return true;
	
	}

	@Override
	public void dead() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'dead'");
	} 
}
