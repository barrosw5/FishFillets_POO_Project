package pt.iscte.poo.game;

import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import objects.BigFish;
import objects.GameObject;
import objects.Score;
import objects.SmallFish;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;

public class GameEngine implements Observer {
	
	private Map<String,Room> rooms;
	private List<Score> scores; // lista em que iremos colocar as pontuações dos players
	private Room currentRoom;
	private int lastTickProcessed = 0;
	private boolean playingFish = true; // true se for o peixe pequeno a jogar e false o contrario
	private int playedLevels = 0;
	private boolean onePlayer = false; // verifica se está só um player em jogo ou não
	private boolean initialized = true;	// serve para carregar no set up fishes os peixes so no inicio
	
	public GameEngine() {
		rooms = new HashMap<String,Room>();
		loadGame();
		resetLevel();

		updateGUI();
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
					break;
				case KeyEvent.VK_LEFT:
				case KeyEvent.VK_RIGHT:
				case KeyEvent.VK_DOWN:
				case KeyEvent.VK_UP:
				case KeyEvent.VK_A:
				case KeyEvent.VK_W:
				case KeyEvent.VK_S:
				case KeyEvent.VK_D:
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
		GameObject.GravityMove(currentRoom); // Esta é a função que faz acontecer o movimento da Gravidade, ela é chamada sempre que o tempo for mexendo no jogo 
	}

	public void updateGUI() {
		if(currentRoom != null) {
			ImageGUI.getInstance().clearImages();
			ImageGUI.getInstance().addImages(currentRoom.getObjects());
		}
	}

	public int getPlayedLevels(){
		return playedLevels;
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

	public void resetLevel(){										// Reset tem de dar reset em tudo
		currentRoom = rooms.get("room" + playedLevels + ".txt");
		ImageGUI.getInstance().setStatusMessage("Level " + (getPlayedLevels() + 1) + ": Good luck! (R for reset)");
		onePlayer = false;
		playingFish = true;
		setupFishesForCurrentRoom();
		currentRoom.resetAll();			//reseta objetos todos moviveis
		updateGUI();
	}

	private void setupFishesForCurrentRoom() {
		SmallFish sf = SmallFish.getInstance();
		BigFish bf = BigFish.getInstance();

		sf.setRoom(currentRoom);
		bf.setRoom(currentRoom);

		sf.setPosition(currentRoom.getSmallFishStartingPosition());
		bf.setPosition(currentRoom.getBigFishStartingPosition());

		if(initialized){
			currentRoom.addObject(sf);
			currentRoom.addObject(bf);
			initialized = false;
		}

		if(!currentRoom.getObjects().contains(SmallFish.getInstance()))
			currentRoom.addObject(sf);

		if(!currentRoom.getObjects().contains(BigFish.getInstance()))
			currentRoom.addObject(bf);

		sf.resetWin();
		bf.resetWin();

	}
	
}
