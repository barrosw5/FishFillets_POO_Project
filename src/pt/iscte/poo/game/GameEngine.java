package pt.iscte.poo.game;

import java.awt.geom.Point2D;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.text.Position;

import objects.SmallFish;
import objects.BigFish;
import objects.GameObject;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Vector2D;

public class GameEngine implements Observer {
	
	private Map<String,Room> rooms;
	private Room currentRoom;
	private int lastTickProcessed = 0;
	private boolean playingFish = true; // true se for o peixe pequeno a jogar e false o contrario
	private final int SPACEBAR = 32;	// código para a spacebar utilizado no ImageGUI.getInstance().keyPressed()
	
	public GameEngine() {
		rooms = new HashMap<String,Room>();
		loadGame();
		currentRoom = rooms.get("room0.txt");
		updateGUI();		
		SmallFish.getInstance().setRoom(currentRoom);
		BigFish.getInstance().setRoom(currentRoom);
	}

	private void loadGame() {
		File[] files = new File("./rooms").listFiles();
		for(File f : files) {
			rooms.put(f.getName(),Room.readRoom(f,this));
		}
	}

	@Override
	public void update(Observed source) {

		if (ImageGUI.getInstance().wasKeyPressed()) {
			int k = ImageGUI.getInstance().keyPressed();

			if(k == SPACEBAR){
				playingFish = !playingFish;
			}
			else{
				if(playingFish){
					SmallFish.getInstance().move(Direction.directionFor(k).asVector());
				}
				else{
					BigFish.getInstance().move(Direction.directionFor(k).asVector());
				}
			}
		}
		int t = ImageGUI.getInstance().getTicks();
		while (lastTickProcessed < t) {
			processTick();
		}
		ImageGUI.getInstance().update();
	}

	// public boolean canMove(Point2D pos, Vector2D dir){
	// 	Point2D finalPos = pos.plus(dir);
		
	// 	for ( GameObject obj: currentRoom.getObjects() ) {
	// 		if ( obj.getPosition().equals(finalPos)) {
				
	// 			if ( obj.getIsMovel() == false ) {
	// 				return false;
	// 			}
	// 		}
	// 	}

	// 	return true;
	// }

	private void processTick() {		
		lastTickProcessed++;
	}

	public void updateGUI() {
		if(currentRoom!=null) {
			ImageGUI.getInstance().clearImages();
			ImageGUI.getInstance().addImages(currentRoom.getObjects());
		}
	}
	
}
