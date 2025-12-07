package objects;

import java.util.ArrayList;
import java.util.List;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Krab extends LightObject implements Interactable, Gravity{
    private boolean downDone = false;

    public Krab(Room room, Point2D pos){
        super(room);
        setPosition(pos);
    }

    // Caranguejo inimigo que se move sozinho e também sofre gravidade
    @Override
    public String getName() {
        return "krab";
    }

    @Override
    public int getLayer() {
        return 2;
    }

    // --- Logica de Movimento Automático ---

    // A cada tick escolhe ir para a esquerda ou direita e lida com colisões básicas
    public void move() {
        Direction dir = Math.random() < 0.5 ? Direction.LEFT : Direction.RIGHT; // Se for menos que 0,5 LEFT, caso contrário RIGHT
        Vector2D vec = dir.asVector(); // define um vetor 
        Point2D currentPos = getPosition(); // a pos atual do krab 
        Point2D targetPos = currentPos.plus(vec); // e a pos para onde vai 

        if (!canMoveTo(targetPos)) { // Se não se pode mover para lá, o código fica por aqui neste TICK
            return;
        }

        GameObject targetObj = findObject(targetPos, getRoom()); // encontra o objeto presente na pos que o krab quer ir 

        if (targetObj instanceof GameCharacter) { // se for um GC chama a função handleFishInteraction 
            handleFishInteraction((GameCharacter) targetObj);
            if (!getRoom().getObjects().contains(this))
                return;
        }

        if (targetObj instanceof Trap) { // Se for uma Trap, o krab morre 
            this.die(this);
            return;
        }

        if (targetObj == null || targetObj instanceof Water || targetObj instanceof HoledWall) { // Se for um detes, o krab somente se move 
            setPosition(targetPos);
        }
    }

    // Função que verifica se krab se pode mover ou não 

    private boolean canMoveTo(Point2D pos) { 
        if (isOutOfBounds(pos)) // se a posição tiver fora do "terreno" de jogo não avança
            return false;
        
        GameObject obj = findObject(pos, getRoom()); // Verifica a posição e se contém um obj
        
        if (obj == null || obj instanceof Water || obj instanceof HoledWall || obj instanceof GameCharacter || obj instanceof Trap) { // Se forem estes, pode avançar 
            return true;
        }
        return false;
    }

    // --- Lógica de Colisão (Peixe entra no Caranguejo ou Caranguejo entra no Peixe) ---

    // SmallFish morre ao tocar; BigFish mata o caranguejo
    private void handleFishInteraction(GameCharacter fish) {
        if (fish instanceof SmallFish) {
            fish.die(fish);
        } else if (fish instanceof BigFish) {
            this.die(this);
        }
    }

    // --- Interactable: Quando o peixe tenta entrar na casa do caranguejo ---
    
    // Se um peixe tenta ocupar a posição, aplica a mesma lógica de interação
    @Override
    public boolean interactWithFish(GameCharacter fish, Point2D from, Point2D to, Vector2D dir) {
        handleFishInteraction(fish);
        return false; // nao pode ser empurrado
    }

    // --- Método Estático para mover todos os caranguejos da sala ---
    
    // Constrói uma lista e chama move() em cada caranguejo presente
    public static void moveAllCrabs(Room r) {
        List<Krab> krabs = new ArrayList<>();
        for (GameObject obj : r.getObjects()) {
            if (obj instanceof Krab) {
                krabs.add((Krab) obj);
            }
        }
        for (Krab k : krabs) {
            k.move();
        }
    }


    // Basicamente um método para verifica se o krab pode fzr o SpecialMove, que no caso dele é sofrer da "gravidade"
    @Override
    public boolean canSpecialMov() {
        Point2D pos = this.getPosition();
        Point2D below = new Point2D(pos.getX(), pos.getY() + 1);
        GameObject obj = findObject(below, this.getRoom());

        if(obj == null )
            return true;

        return false;
    }

    @Override
    public void specialmov() {
        if (canSpecialMov()) {
            Point2D pos = this.getPosition();
            Point2D below = getBelow(pos);
            pushObject(this, pos, below);
            downDone = true;
        }

        else if (downDone && !canSpecialMov()) {
            specialAbillity();
            downDone = false;
        }
    }

    // Não tem qql tipo de specialAbility 

    @Override
    public void specialAbillity() {
    }

    @Override
    public void reset(){
        getRoom().removeObject(this);
    }
    
}
