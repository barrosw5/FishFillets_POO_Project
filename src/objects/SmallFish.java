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

public class SmallFish extends GameCharacter {

	private static SmallFish sf = new SmallFish(null);
	private static final String sfNameLeft = "smallFishLeft";
	private static final String sfNameRight = "smallFishRight";
	private static final String sfNameUp = "smallFishUp";
	private static final String sfNameDown = "smallFishDown";
	private int superMoves = 0;
	
	private SmallFish(Room room) {
		super(room);
	}

	public static SmallFish getInstance() {
		return sf;
	}
	
	// Função que vai mudar a imagem do peixe coforme a direção que ele segue

	@Override
	public String getName() {
		if(getDirection() == LEFT)
			return sfNameLeft;
		if(getDirection() == RIGHT)
			return sfNameRight;
		if(getDirection() == UP)
			return sfNameUp;
		if(getDirection() == DOWN)
			return sfNameDown;
		return null;
	}

	public boolean hasSuperMoves(){
		return superMoves > 0;
	}
	
	// O método que ditas as condiçõs para o peixe se mover ou não 
	@Override
	public boolean fishCanMove(Point2D pos, Vector2D dir) {
		if (hasWon()) // Se já venceu, não mexe 
			return false;

		Point2D finalPos = pos.plus(dir); // verifica pos para onde se quer deslocar
		GameObject obj = findObject(finalPos, getRoom()); // encontra o obj presente na pos para onde quer ir 
		Point2D finalObjPos = finalPos.plus(dir); // a pos após aquela para onde o peixe quer ir

		if ( obj == null) { // se não houver nenhum obj nessa pos ele move-se
			return true;
		}

		if ( obj instanceof Interactable) { // se houver um e for Interactable vai retornar true ou falso conforme o que as restrições que o obj tem definidas
			return ((Interactable) obj).interactWithFish(this, finalPos, finalObjPos, dir);
		}

		return false;

	}

	// Função fishSupport: esta é chamada pela função heckSurvivalStatus do GC

	@Override
	public boolean fishSupport() {
		if(superMoves > 0)
			return true;

		Point2D CurrentlyPos = getPosition(); // encontra a pos onde está o peixe
		int lengthPos = CurrentlyPos.getY(); // encontra o Y em que o peixe está

		List<Point2D> posUp = new ArrayList<>();  // Cria uma lista de pos

		for ( int i = lengthPos -1; i > 0; i--) { // Preenche a lista com todas as pos acima do peixe
			posUp.add( new Point2D(CurrentlyPos.getX(), i));
		}

		int movableCount = 0; // Var contadora

		for ( Point2D objPos: posUp) {
			GameObject obj = findObject(objPos, getRoom());

			if (!(obj instanceof MovableObject)) {
				break;
			}

			// morre se tiver heavy object em cima
			if (obj instanceof HeavyObject) {
				return false;
			}

			movableCount++;

			// so aguenta 1 objeto leve
			if (movableCount > 1) {
				return false;
			}
		}
		
		return true;
	}

    public void activateSuperPower() {
       this.superMoves = 10;
    }

	@Override
	public void move(Vector2D vec){
		super.move(vec);

		if(superMoves > 0){
			superMoves--;
			if(superMoves == 0){
				System.out.println("The effect finished");
			}
		}
	}

	@Override
	public void reset(){
		super.reset();
		superMoves = 0;
	}

	@Override
	public boolean canPassSmallSpaces() {
		return true;
	}

	@Override
	public boolean canPushHeavyObjects() {
		return false;
	}
}
