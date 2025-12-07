package objects;

import java.util.List;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Bomb extends LightObject implements Interactable, Gravity{
	private boolean downDone = false; 

	public Bomb(Room room) {
		super(room);
	}

	@Override
	public String getName() {
		return "bomb";
	}

	@Override
	public int getLayer() {
		return 1;
	}

	// Cai se tiver espaço livre abaixo
	@Override
    public boolean canSpecialMov() {
        Point2D pos = this.getPosition(); // busca a pos da bomba 
        Point2D below = new Point2D(pos.getX(), pos.getY() + 1); // a pos embaixo dela 
        GameObject obj = findObject(below, this.getRoom()); // procura o objeto que está nessa pos

        if(obj == null ) // se não houver nenhum avança
            return true;

        return false;
    }

    // Queda simples: depois de cair e chegar ao seu "final" ativa a explosão
    @Override
    public void specialmov() {
        if (canSpecialMov()) { // se puder descer, muito bem, desce 
            Point2D pos = this.getPosition(); // pos da bomba
            Point2D below = getBelow(pos); // abaixo dels
            pushObject(this, pos, below); // puxa a bomba para baixo 
            downDone = true;
        }

        else if (downDone && !canSpecialMov()) { // caso não possa 
            specialAbillity(); // ativa a specialAbility, ou seja, a explosão 
            downDone = false;
        }
    }

	// Explode destruindo tudo à volta 
	@Override
	public void specialAbillity() {

		Point2D currentPos = this.getPosition(); // pega a pos da bomba 
		List<Point2D> nearObjects = getAdjacentPositions(currentPos); // procura todas as pos em redor da mesma
		nearObjects.add(currentPos); // e adiciona a pos da própria bomba

		Point2D beloPos = getBelow(currentPos); // pega a pos abaixo da bomba

		GameObject below = findObject(beloPos, getRoom()); // procura o obj que está nessa pos 

		if ( below instanceof GameCharacter) { // se for um peixe, não explode, pára por aí o código
			return;
		}

		for (Point2D pos: nearObjects) { // se não, percorre a Lista
			GameObject target = findObject(pos, this.getRoom()); // descobre o obj que está na pos que estamos a "investigar"

			if (target instanceof GameCharacter) { // se for um peixe, mata-o 
				((GameCharacter) target).die(target);
			}

			if (target != null) { // se não for "vazio" remove o objeto de lá 
				this.getRoom().removeObject(target);
			}
			this.getRoom().addObject(new Explosion(pos, this.getRoom())); // e chama o efeito especial da bomba e mete-o na pos.
		}
		

	}

	// Pode ser empurrada; caso exista cadeia de Interactable delega
	@Override
	public boolean interactWithFish(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
		if ( BombPushBy(fish, from, to, dir)) { // verifica se pode ser "empurrado"
			pushObject(this, from, to);// se sim, faz isso 
			return true;
		}
		return false;
	}

	// Regras para empurrar: BigFish pode encadear interações; bloqueia se algo sólido à frente
	public boolean BombPushBy(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
		GameObject obj = findObject(to, fish.getRoom()); // procura o objeto que está à frente da bomba

		if ( fish instanceof BigFish ) { // Se for o bigfish empurra em cadeia 
			if ( obj instanceof Interactable ) {
				Point2D PlusPos = to.plus(dir);
				return ((Interactable)obj).interactWithFish(fish, to, PlusPos, dir);
			}
		}

		if ( obj instanceof NonMovableObject || obj instanceof MovableObject || obj instanceof GameCharacter) { // se for algum destes objetos não avança 
			return false;
		}
		return true; // se for o Smalfish e não houver nada à frente pode empurra a bomba
	}

	@Override
	public void reset(){
		super.reset();
		downDone = false;
	}
}

 
