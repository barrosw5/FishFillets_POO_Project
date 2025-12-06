package objects;

import java.util.ArrayList;
import java.util.List;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

// Esta é a "classe-mãe" dos objetos, ou seja, todas as classes que envolvem os objetos do jogo acabam por derivar desta

public abstract class GameObject implements ImageTile {

    private Point2D position;
    private Point2D startingPosition;
    private Room room;
    private boolean isMovable;

    // Construtor 
    public GameObject(Room room, boolean isMovable) {
        this.room = room;
        this.isMovable = isMovable;
    }

    public GameObject(Point2D position, Room room) {
        if (position == null) {
            throw new IllegalArgumentException("A posição inicial não pode ser nula.");
        }
        this.position = position;
        this.room = room;
    }

    // --- funçoes movimento ---

    @Override
    public Point2D getPosition() { 
        return position; 
    }

    public void setPosition(int x, int y) { 
        setPosition(new Point2D(x, y)); 
    }

    public void setPosition(Point2D position) {
        if (position == null) {
            throw new IllegalArgumentException("Tentativa de definir uma posição nula no objeto " + getName());
        }
        this.position = position;
        
        if (startingPosition == null) {
            startingPosition = position;
        }
    }

    public Point2D getStartingPosition() { return startingPosition; }
    
    public void setStartingPosition(Point2D sp) { 
        if (sp == null) throw new IllegalArgumentException("StartingPosition não pode ser nula.");
        startingPosition = sp; 
    }

    public Point2D getBelow(Point2D pos) {
        if (pos == null) throw new IllegalArgumentException("Não é possível calcular 'abaixo' de uma posição nula.");
        return new Point2D(pos.getX(), pos.getY() + 1);
    }

     public Point2D getAbove(Point2D pos) {
        if (pos == null) throw new IllegalArgumentException("Não é possível calcular 'acima' de uma posição nula.");
        return new Point2D(pos.getX(), pos.getY() -1);
    }

    // Método que vai criar uma lista com todas as posições adjacentes à pos dada
    public List<Point2D> getAdjacentPositions(Point2D pos) {
        if (pos == null) throw new IllegalArgumentException("Não é possível calcular adjacências de uma posição nula.");
        
        List<Point2D> adj = new ArrayList<>();
        adj.add(new Point2D(pos.getX() + 1, pos.getY())); // Direita
        adj.add(new Point2D(pos.getX() - 1, pos.getY())); // Esquerda
        adj.add(new Point2D(pos.getX(), pos.getY() - 1)); // Cima
        adj.add(getBelow(pos));                           // Baixo
        return adj;
    }

    // --- interaçao com a Sala ---

    public Room getRoom() { return room; }
    
    public void setRoom(Room room) { 
        this.room = room; 
    }
    
    public boolean getIsMovel() { return isMovable; }

    // Médodo que acaba por fazer ao Reset ao jogo. Basicamente coloca todos os objetos na sua posição original. Este método é chamando quando a tecla R é clicada
    public void reset() {
        if (startingPosition != null) {
            Room r = getRoom();
            if (r != null && !r.getObjects().contains(this)) {
                r.addObject(this);
            }
            setPosition(startingPosition);
        }
    }

    // Este método basicamente serve para "mover" um objeto de uma determinada pos para outra. É usada basicamente nos objetos móveis quando estes são empurrados por GameCharacters

    public void pushObject(GameObject obj, Point2D from, Point2D to) {
        if (obj == null) throw new IllegalArgumentException("Não é possível empurrar um objeto nulo.");
        obj.setPosition(to);
    }

    // --- logica objetos ---

    // Este método basciamente faz com que a gravidade seja usada. Ela está implementada no GameEngine, no processoTick, ou seja, vai sempre ser chamada quando um Tick (tempo do jogo) for processado
    // Basciamente é criada uma lista de objetos com a interface Gravity presentes na room e depois percorre essa lista durante aqeuele tick e chama a função SpecialMov

    public static void applyGravity(Room r) {
        if (r == null) return;

        List<Gravity> gravityObjects = new ArrayList<>();
        
    

        for (GameObject obj : r.getObjects()) {
            if (obj instanceof Gravity) {
                gravityObjects.add((Gravity) obj);
            }
        }
        
        for (Gravity g : gravityObjects) {
            g.specialmov();
        }
    }

    // Função para procurar um objeto numa pos pretendida naquela Room. Procura todos menos a Água e o Sangue, por motivos óbvios

    public static GameObject findObject(Point2D pos, Room r) {
        if (r == null || pos == null) return null;

        for (GameObject obj : r.getObjects()) {
            if (obj.getPosition().equals(pos) && !(obj instanceof Water) && !(obj instanceof Blood)) {
                return obj;
            }
        }
        return null;
    }


    // Função para verficiar se está ou não fora do "terreno" do jogo 
    public boolean isOutOfBounds(Point2D p) {
        return p.getX() < 0 || p.getX() > 9 || p.getY() < 0 || p.getY() > 9;
    }

    // Método que remove um objeto da room. Basicamente retira-o da lista que contém todos os objetos presentes na room
    public static void removeObject(GameObject obj, Room r) {
        if (r != null && r.getObjects() != null) {
            r.getObjects().remove(obj);
        }
    }

    // Função die: basicamente quando um peixe morre remove-o do tabuleiro e cria o objeto blood, que fica na sua posição  
    public void die(GameObject killed) {
        if (killed == null) return;

        Room r = killed.getRoom();
        
        if (r != null) {
            r.addObject(new Blood(getPosition(), r));   // Deixa sangue
            r.removeObject(killed);                   // Remove o corpo
        }
    }

    @Override
    public String toString() {
        return "| " + getName() + " @ " + getPosition() + " |";
    }
}
