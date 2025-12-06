package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Anchor extends HeavyObject implements Interactable, Gravity {
    private boolean controlDown = false;
    private boolean MovedOnce = false;

    public Anchor(Room room) {
        super(room);
    }

    @Override
    public String getName() {
        return "anchor";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    // No reset volta ao sítio inicial e volta a poder ser empurrada uma vez
    @Override
    public void reset(){
        if (getStartingPosition() != null)
            MovedOnce = false;
        super.reset();
    }

   // Só cai se a posição de baixo estiver vazia
   @Override
    public boolean canSpecialMov() {
        Point2D pos = this.getPosition(); // Pega a pos do objeto 
        Point2D below = new Point2D(pos.getX(), pos.getY() + 1); // a posição abaico do obj
        GameObject obj = findObject(below, this.getRoom()); // e o obj abaixo da âncora

        if(obj == null ) // se for vazio pode descer, caso contrário não 
            return true;

        return false;
    }

    // Cai até assentar e depois ativa a habilidade especial
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

    // Quando "assenta" destrói o que estiver imediatamente abaixo, nomeadamente o SF e o Trunk 
    @Override
    public void specialAbillity() {
        Point2D currenbtlyPos = this.getPosition(); // pega pos da âncora
        Point2D belowPos = getBelow(currenbtlyPos); // a pos abaixo da âncora 
        GameObject remove = findObject(belowPos, getRoom()); // procura objeto que está embaixo 

        if (remove instanceof SmallFish) { // se for o smalFish mata-o
            ((SmallFish) remove).die(remove);
        }
        if (remove instanceof Trunk) { // se for o tronco destroi-o
            this.getRoom().removeObject(remove);
        }
    }

    // Interação com peixe: só pode ser empurrada uma vez e apenas na horizontal pelo BigFish
    @Override
    public boolean interactWithFish(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
        if ( canPushby(fish, from, to, dir)) { // verifica se pode ser "empurrado" 
            pushObject(this, from, to); // se sim, faz isso 
            return true;
        }
        return false;
    }


    // Valida se o BigFish a consegue empurrar naquela direção
    private boolean canPushby(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) { 
        if (MovedOnce) { // Verifica se já foi movido
            return false;
        }
        if (!Point2D.sameDirectionHorzontal(from, to)) { // Se está a ser empurrado na mesma direção, no caso horizontal 
            return false;
        }

        if ( ! (fish instanceof BigFish )) { // Se é ou ñ o BF a tentar empurrar
            return false;
        }

        GameObject obj = findObject(to, fish.getRoom()); // procura o objeto que está a seguir ao objeto 

         if ( obj instanceof Interactable ) { // se for um interável vai faz uma espécie de recursividade 
				Point2D PlusPos = to.plus(dir); // o que está à frente desse objeto 
                MovedOnce = true;
				return ((Interactable)obj).interactWithFish(fish, to, PlusPos, dir); // e chama pela função de interação desse objeto 
			}

        if ((obj instanceof NonMovableObject || obj instanceof MovableObject || obj instanceof GameCharacter)) { // se for algum destes objetos não avança 
            return false;
        }
    
        MovedOnce = true;
        return true;
    } 
}
