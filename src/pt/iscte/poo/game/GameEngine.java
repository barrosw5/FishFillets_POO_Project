package pt.iscte.poo.game;

import java.awt.event.KeyEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import objects.BigFish;
import objects.SmallFish;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;

public class GameEngine implements Observer {
	
	private Map<String,Room> rooms;
	private Room currentRoom;
	private int lastTickProcessed = 0;
	private boolean playingFish = true; // true se for o peixe pequeno a jogar e false o contrario
	private int playedLevels = 0;
	
	public GameEngine() {
		rooms = new HashMap<String,Room>();
		loadGame();
		currentRoom = rooms.get("room0.txt");		//room0 nao está a corresponder com o txt
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
		System.out.println("SmallFish won: " + SmallFish.getInstance().hasWon());		//debug
		System.out.println("BigFish won: " + BigFish.getInstance().hasWon());

		if(SmallFish.getInstance().hasWon() && BigFish.getInstance().hasWon()){
			playedLevels++;
			if(playedLevels < rooms.size()){
				currentRoom = rooms.get("room" + playedLevels + ".txt");	//incrementa playedLevels para ir para outra sala
			}
			else{
				//show score
			}
		}

		if (ImageGUI.getInstance().wasKeyPressed()) {
			int k = ImageGUI.getInstance().keyPressed();
									// switch para reconhecer input de teclado
			switch (k) {			// não para jogo ao receber tecla indesejada 
				case KeyEvent.VK_SPACE:
					playingFish = !playingFish;
					break;
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_RIGHT:
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_UP:
					if(playingFish){
						SmallFish.getInstance().move(Direction.directionFor(k).asVector());
					}
					else
						BigFish.getInstance().move(Direction.directionFor(k).asVector());
					break;
				default:
					System.out.println("DEBUG: Tecla numero " + k + " clicada (Sem efeito).");
					break;
			}
		}
		int t = ImageGUI.getInstance().getTicks();
		while (lastTickProcessed < t) {
			processTick();
		}
		ImageGUI.getInstance().update();
	}

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
