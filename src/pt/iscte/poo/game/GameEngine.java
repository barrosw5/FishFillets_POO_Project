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
	public static int playedLevels = 0;
	private boolean onePlayer = false; // verifica se está só um player em jogo ou não
	
	public GameEngine() {
		rooms = new HashMap<String,Room>();
		loadGame();
		currentRoom = rooms.get("room" + playedLevels + ".txt");		//por alguma razao as posições iniciais dos peixes estão a ser as do ultimo nivel
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

		if(SmallFish.getInstance().hasWon() && BigFish.getInstance().hasWon()){
			nextLevel();
		}

		if((SmallFish.getInstance().hasWon() || BigFish.getInstance().hasWon()) && !onePlayer){
			playingFish = !playingFish;
			onePlayer = true;			// faz com que ao um dos peixes ganhar ele fica unplayable 
		}								// e desta forma só um deles fica ativo

		if (ImageGUI.getInstance().wasKeyPressed()) {
			int k = ImageGUI.getInstance().keyPressed();
									// switch para reconhecer input de teclado
			switch (k) {
				case KeyEvent.VK_SPACE:
					if(!onePlayer){
						playingFish = !playingFish;		// caso um dos peixes já tenha ganho a spacebar fica useless
					}
					break;
				case KeyEvent.VK_R:
					resetLevel();
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

	public void nextLevel(){
		playedLevels++;
		if(playedLevels < rooms.size()){
			resetLevel();
		}
		else{
			//show score
		}
	}

	public void resetLevel(){
		currentRoom = rooms.get("room" + playedLevels + ".txt");	//incrementa playedLevels para ir para outra sala
		SmallFish.getInstance().resetWin();
		BigFish.getInstance().resetWin();			// win reset para não passar os niveis todos
		SmallFish.getInstance().setRoom(currentRoom);
		BigFish.getInstance().setRoom(currentRoom);
		updateGUI();
	}
	
}
