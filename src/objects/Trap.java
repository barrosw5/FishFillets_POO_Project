package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Trap extends HeavyObject implements Interactable, Gravity {
	private boolean controlDown = false;

	public Trap(Room room) {
		super(room);
	}

	@Override
	public String getName() {
		return "trap";
	}

	@Override
	public int getLayer() {
		return 1;
	}

	// SF atravessa, outros morrem ao tocar na trap
	@Override
	public boolean interactWithFish(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
		if ( fish instanceof SmallFish) {
			return true;
		}
		fish.die(fish);
		return false;
	}
    // Método para verificar se a Trap pode fazer o seu special move, no caso, sofrer com o efeito da gravidade
	@Override
	public boolean canSpecialMov() {
		Point2D pos = this.getPosition();
        Point2D below = new Point2D(pos.getX(), pos.getY() + 1);
        GameObject obj = findObject(below, this.getRoom()); // o obj abaixo da Trap 

        if(obj == null ) // se não houver nenhum pode, fazer o SpecialMov
            return true;

        return false;
	}

	// Queda e trigger da habilidade (vazia) quando assenta
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

}
