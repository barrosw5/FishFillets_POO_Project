package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Cup extends LightObject implements Interactable, Gravity{
	private boolean downDone = false;

	public Cup(Room room) {
		super(room);
	}

	@Override
	public String getName() {
		return "cup";
	}

	@Override
	public int getLayer() {
		return 1;
	}

	// Desce se a casa de baixo estiver livre
	@Override
   	public boolean canSpecialMov() {
        Point2D pos = this.getPosition(); // Pega a pos do objeto 
        Point2D below = new Point2D(pos.getX(), pos.getY() + 1); // a posição abaico do obj
        GameObject obj = findObject(below, this.getRoom()); // e o obj abaixo da âncora

        if(obj == null ) // se for vazio pode descer, caso contrário não 
            return true;

        return false;
    }

    // Desce uma pos, quando não pode mais, não desce
    @Override
    public void specialmov() {
        if (canSpecialMov()) { // se puder, desce 
            Point2D pos = this.getPosition();
            Point2D below = getBelow(pos);
            pushObject(this, pos, below);
            downDone = true;
        }

        else if (downDone && !canSpecialMov()) { // se não, ativa a habilidade especial, neste caso nenhuma
            specialAbillity();
            downDone = false;
        }
    }

    @Override
	public void specialAbillity() {
	}

	// Interação com peixes
	@Override
	public boolean interactWithFish(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {

		if ( CupPushBy(fish, from, to, dir)) { // Se pode ser empurrado, empurra
			pushObject(this, from, to);
			return true;
		}
		return false;
	}

	public boolean CupPushBy(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
		GameObject obj = findObject(to, fish.getRoom()); // Procura o objeto à frente da taça 

		if ( fish instanceof BigFish ) { // Se for um BF a empurrar pode fazê-lo em cadeia, usando aqui a recursividade
			if ( obj instanceof Interactable ) {
				Point2D PlusPos = to.plus(dir);
				return ((Interactable)obj).interactWithFish(fish, to, PlusPos, dir);
			}
		}

		if ( (obj instanceof MovableObject || obj instanceof NonMovableObject || obj instanceof GameCharacter) && !(obj instanceof HoledWall)) { // Se for algum destes objetos à frente não pode ser empurrado 
			return false;
		}
		return true;
	}
    
}

	
