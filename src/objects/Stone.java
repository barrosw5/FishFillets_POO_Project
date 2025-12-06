package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Stone extends HeavyObject implements Interactable, Gravity{
	private boolean controlDown = false;
	private boolean krabSpawned = false;

	public Stone(Room room) {
		super(room);
	}

	@Override
	public String getName() {
		return "stone";
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public void reset(){
		super.reset();
		krabSpawned = false;
	}

	// Verifica se a stone pode realizar o SpecialMov, que no caso é sofrer da ação da gravidade
	@Override
    public boolean canSpecialMov() {
        Point2D pos = this.getPosition(); // busca a pos da stone
        Point2D below = new Point2D(pos.getX(), pos.getY() + 1); // a pos abaixo da stone
        GameObject obj = findObject(below, this.getRoom()); // verifica qual é o obj presente naquela pos

        if(obj == null ) // se não há um, procede
            return true;

        return false;
    }

    // Função que faz o mov
    @Override
    public void specialmov() {
        if (canSpecialMov()) { // se puder se mover, move 
            Point2D pos = this.getPosition(); // pos da stone
            Point2D below = getBelow(pos); // pos abaixo da stone
            pushObject(this, pos, below); // e vai para baixo 
            controlDown = true;
        }

        else if (controlDown && !canSpecialMov()) { // se não puder e movimento tiver acabado, chama a specialAbility 
            specialAbillity();
            controlDown = false;
        }
    }

	// Se "terminar" o movimento num peixe pequeno mata-o, se for tronco destrói-o
	@Override
	public void specialAbillity() {
		Point2D currentPos = this.getPosition();
        Point2D belowPos = getBelow(currentPos);
        GameObject target = findObject(belowPos, getRoom());

        if (target instanceof SmallFish) {
            ((SmallFish) target).die(target);
        }
		else if (target instanceof Trunk) {
            getRoom().removeObject(target);
        }
	}

	// BigFish pode empurrar, se empurrar horizontalmente gera um Krab acima (uma vez)
	@Override
	public boolean interactWithFish(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
		if (canBePushed(fish, from, to, dir)) { // Se puder se empurrado, move 
			pushObject(this, from, to);

			if(dir.getY() == 0 && !krabSpawned){ // Se esse movimento for na horizontal 
				trySpawnKrab(from); // tenta "criar" um krap se puder, claro 
			}
			return true;
		}
		
		return false;
	}

	// Verifica se há espaço livre acima para criar o caranguejo
	public void trySpawnKrab(Point2D stonePos) {
		Point2D abovePos = new Point2D(stonePos.getX(), stonePos.getY()-1); // procura a pos acima da stone

		if(isOutOfBounds(abovePos)) // se essa posição é fora do tabuleiro para por ali 
			return;
	
		GameObject objAbove = findObject(abovePos, getRoom()); // verifica que objeto está nessa posição 

		if(objAbove == null){ // se tiver vazia cria o krab
			Krab krab = new Krab(getRoom(), abovePos);
			getRoom().addObject(krab);
			krabSpawned = true;
		}
	}

	public boolean canBePushed(GameCharacter fish, Point2D from, Point2D to, Vector2D dir ) {
		GameObject obj = findObject(to, fish.getRoom()) ; // verifica qual objeto é que está presente na posição à frente da stone 

		if ( !(fish instanceof BigFish)) { // se for um GC diferente do BF não pode movimentar a stone
			return false; 
		}

		if ( obj instanceof Interactable ) { // se o obj for Interactable pode haver um movimento em cadeia e usamos a recursividade 
			Point2D plusPos = to.plus(dir);
			return ((Interactable)obj).interactWithFish(fish, to, plusPos, dir);
		}

		if ((obj instanceof MovableObject || obj instanceof NonMovableObject || obj instanceof GameCharacter) ) { // se for um obj pertencente a estas classes a stone não move
			return false;
		}
		return true;
	}
}
