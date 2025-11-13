package pt.iscte.poo.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import objects.*;
import pt.iscte.poo.utils.Point2D;

public class Room {
	
	private List<GameObject> objects;
	private String roomName;
	private GameEngine engine;
	private Point2D smallFishStartingPosition;
	private Point2D bigFishStartingPosition;
	
	public Room() {
		objects = new ArrayList<GameObject>();
	}

	private void setName(String name) {
		roomName = name;
	}
	
	public String getName() {
		return roomName;
	}
	
	private void setEngine(GameEngine engine) {
		this.engine = engine;
	}

	public void addObject(GameObject obj) {
		objects.add(obj);
		engine.updateGUI();
	}
	
	public void removeObject(GameObject obj) {
		objects.remove(obj);
		engine.updateGUI();
	}
	
	public List<GameObject> getObjects() {
		return objects;
	}

	public void setSmallFishStartingPosition(Point2D heroStartingPosition) {
		this.smallFishStartingPosition = heroStartingPosition;
	}
	
	public Point2D getSmallFishStartingPosition() {
		return smallFishStartingPosition;
	}
	
	public void setBigFishStartingPosition(Point2D heroStartingPosition) {
		this.bigFishStartingPosition = heroStartingPosition;
	}
	
	public Point2D getBigFishStartingPosition() {
		return bigFishStartingPosition;
	}
	
	public static Room readRoom(File f, GameEngine engine) {
		try {
			Room r = new Room();
			r.setEngine(engine);
			r.setName(f.getName());

			Scanner sc = new Scanner(f);

			for(int i = 0; i < 10; i++){
				String linha = sc.nextLine();
				char[] caracteres = linha.toCharArray();
				for(int j = 0; j < caracteres.length; j++){

					GameObject water = new Water(r);
					water.setPosition(new Point2D(j, i));
					r.addObject(water);

					switch(caracteres[j]){
						case ' ':
							break;
						case 'B':
							r.setBigFishStartingPosition(new Point2D(j, i));
							break;
						case 'S':
							r.setSmallFishStartingPosition(new Point2D(j, i));
							break;
						case 'W':
							GameObject wall = new Wall(r);
							wall.setPosition(new Point2D(j, i));
							r.addObject(wall);
							break;
						case 'H':
							GameObject steelHorizontal = new SteelHorizontal(r);
							steelHorizontal.setPosition(new Point2D(j, i));
							r.addObject(steelHorizontal);
							break;
						case 'V':
							GameObject steelVertical = new SteelVertical(r);
							steelVertical.setPosition(new Point2D(j, i));
							r.addObject(steelVertical);
							break;
						case 'C':
							GameObject cup = new Cup(r);
							cup.setPosition(new Point2D(j, i));
							r.addObject(cup);
							break;
						case 'R':
							GameObject stone = new Stone(r);
							stone.setPosition(new Point2D(j, i));
							r.addObject(stone);
							break;
						case 'A':
							GameObject anchor = new Anchor(r);
							anchor.setPosition(new Point2D(j, i));
							r.addObject(anchor);
							break;
						case 'b':
							GameObject bomb = new Bomb(r);
							bomb.setPosition(new Point2D(j, i));
							r.addObject(bomb);
							break;
						case 'T':
							GameObject trap = new Trap(r);
							trap.setPosition(new Point2D(j, i));
							r.addObject(trap);
							break;
						case 'Y':
							GameObject trunk = new Trunk(r);
							trunk.setPosition(new Point2D(j, i));
							r.addObject(trunk);
							break;
						case 'X':
							GameObject holedWall = new HoledWall(r);
							holedWall.setPosition(new Point2D(j, i));
							r.addObject(holedWall);
							break;
					}
				}
			}

			sc.close();
		
			return r;
		} catch (FileNotFoundException e) {
			System.err.println("Lembra-te lá de quando fizeste aquela função de ler o ficheiro de texto...");
		}
		return null;
	}
	
}