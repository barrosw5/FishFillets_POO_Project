package pt.iscte.poo.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import objects.*;
import pt.iscte.poo.utils.Point2D;

public class Room {

    // Variáveis globais 

    private String roomName;
    private GameEngine engine;

    // A lista de Objetos que está na Room
    private List<GameObject> objects;
    private List<GameObject> resettableObjects;
    private Point2D smallFishStartingPosition;
    private Point2D bigFishStartingPosition;

   
   // O Construtor da Room, inicializa as Listas 
    public Room() {
        objects = new ArrayList<>();
        resettableObjects = new ArrayList<>();
    }

    // --- Getters e Setters ---
    public String getName() { 
		return roomName;
	}
    private void setName(String name) {
		roomName = name;
	}
    private void setEngine(GameEngine engine) {
		this.engine = engine;
	}
    
    public Point2D getSmallFishStartingPosition() {
		return smallFishStartingPosition;
	}
    public void setSmallFishStartingPosition(Point2D pos) {
		this.smallFishStartingPosition = pos; 
	}
    
    public Point2D getBigFishStartingPosition() {
		return bigFishStartingPosition;
	}
    public void setBigFishStartingPosition(Point2D pos) {
		this.bigFishStartingPosition = pos;
	}
    
    public List<GameObject> getObjects() {
		return objects;
	}
    public List<GameObject> getResettableObjects() {
		return resettableObjects;
	}

    // --- funções para objetos ---

    // Adiciona objeto à sala e atualiza a GUI
    public void addObject(GameObject obj) {
        objects.add(obj);
        registerResettable(obj);
        if(engine != null) engine.updateGUI();
    }

    // Remove objeto e força refresh na GUI
    public void removeObject(GameObject obj) {
        objects.remove(obj);
        if(engine != null) engine.updateGUI();
    }

    // Mantém lista de objetos que podem ser repostos num reset
    public void registerResettable(GameObject r) {
        if (!resettableObjects.contains(r)) {
            resettableObjects.add(r);
        }
    }

    // Faz reset a tudo o que não são GameCharacters
    public void resetResettable() {			// Reseta objetos não gameCharacters
        for (GameObject r : resettableObjects) {
            if (!(r instanceof GameCharacter)) {
                r.reset();
            }
        }
    }

    // --- leitura do ficheiro txt ---

    // Lê um ficheiro de room (10 linhas) e cria a respetiva instância
    public static Room readRoom(File f, GameEngine engine) {
        try (Scanner sc = new Scanner(f)) {
            Room r = new Room();
            r.setEngine(engine);
            r.setName(f.getName());

            for (int i = 0; i < 10; i++) {
                if (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    processLine(r, line, i);
                }
            }
            return r;

        } catch (FileNotFoundException e) {
            System.err.println("Erro: Ficheiro não encontrado -> " + f.getAbsolutePath());
            return null;
        }
    }

    // Processa uma linha do ficheiro (y) criando água + objetos dessa linha
    private static void processLine(Room r, String line, int y) {
        char[] chars = line.toCharArray();
        for (int x = 0; x < chars.length; x++) {

            // adiciona sempre agua no fundo
            GameObject water = new Water(r);
            water.setPosition(new Point2D(x, y));
            r.addObject(water);

            createObjectFromChar(r, chars[x], x, y);
        }
    }

    // Tradução de cada char do mapa para o objeto correspondente
    private static void createObjectFromChar(Room r, char c, int x, int y) {
        Point2D pos = new Point2D(x, y);
        GameObject obj = null;

        switch (c) {
            case ' ': break; // água por isso nada
            case 'B': r.setBigFishStartingPosition(pos); break;
            case 'S': r.setSmallFishStartingPosition(pos); break;
            case 'W': obj = new Wall(r); break;
            case 'H': obj = new SteelHorizontal(r); break;
            case 'V': obj = new SteelVertical(r); break;
            case 'C': obj = new Cup(r); break;
            case 'R': obj = new Stone(r); break;
            case 'A': obj = new Anchor(r); break;
            case 'b': obj = new Bomb(r); break;
            case 'T': obj = new Trap(r); break;
            case 'Y': obj = new Trunk(r); break;
            case 'X': obj = new HoledWall(r); break;
            case 'J': obj = new Juan(r); break;
            case 'U': obj = new Buoy(r); break;
            default:
                System.err.println("Não era suposto chegar aqui nunca: " + c);
                break;
        }

        if (obj != null) {
            obj.setPosition(pos);
            r.addObject(obj);
        }
    }
}
