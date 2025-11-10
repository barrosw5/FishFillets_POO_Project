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

			while(sc.hasNext()){
				for(int i = 0; i < 10; i++){
					String linha = sc.nextLine();
					char[] caracteres = linha.toCharArray();
					for(int j = 0; j < caracteres.length; j++){
						switch(caracteres[j]){
							case ' ':
								GameObject water = new Water(r);
								water.setPosition(new Point2D(j, i));
								r.addObject(water);
								break;
							case 'W':
								GameObject wall = new Wall(r);
								wall.setPosition(new Point2D(j, i));
								r.addObject(wall);
								break;
							case 'B':
								GameObject bf = BigFish.getInstance();
								bf.setPosition(j, i);
								r.addObject(bf);
								break;
							case 'S':
								GameObject sf = SmallFish.getInstance();
								sf.setPosition(j, i);
								r.addObject(sf);
								break;
							case 'H':
								GameObject steelHorizontal = new SteelHorizontal(r);
								steelHorizontal.setPosition(new Point2D(j, i));
								r.addObject(steelHorizontal);
								break;
						}
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