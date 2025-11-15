package pt.iscte.poo.game;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JOptionPane;

import objects.BigFish;
import objects.GameObject;
import objects.Score;
import objects.SmallFish;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;

public class GameEngine implements Observer {
	
	private static final String SCORES_FILE = "gamedata" + File.separator + "scores.txt";
	private Map<String,Room> rooms;
	private List<Score> scores; // lista em que iremos colocar as pontuações dos players
	private int gameStartTimeTicks = 0;
	private Room currentRoom;
	private int lastTickProcessed = 0;
	private boolean playingFish = true; // true se for o peixe pequeno a jogar e false o contrario
	private int playedLevels = 0;
	private boolean onePlayer = false; // verifica se está só um player em jogo ou não
	private boolean initialized = true;	// serve para carregar no set up fishes os peixes so no inicio
	private boolean isGameOver = false;	// verifica se o jogo já acabou
	
	public GameEngine() {
		rooms = new HashMap<String,Room>();
		scores = new ArrayList<>();
		loadGame();
		loadScores();
		resetLevel();
		updateGUI();
		updateScoresDisplay();
	}

	private void loadGame() {
		File[] files = new File("./rooms").listFiles();
		for(File f : files) {
			rooms.put(f.getName(),Room.readRoom(f,this));
		}
	}

	private void loadScores(){
		File file = new File(SCORES_FILE);

		try {
			File parentDir = file.getParentFile();
			
			if (!file.exists()) {
				file.createNewFile();
				return; 
			}

		} catch (IOException e) {
			System.err.println("Erro de I/O ao tentar criar ficheiro de scores: " + e.getMessage());
		}
		
		try {
			Scanner sc = new Scanner(file);

			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] parts = line.split(",");   // Formato "Nome,Pontos"
				if (parts.length == 2) {
					String name = parts[0];
					int scoreInTicks = Integer.parseInt(parts[1]);
					scores.add(new Score(name, scoreInTicks));
				}
			}
			scores.sort((s1, s2) -> Integer.compare(s1.getScore(), s2.getScore()));
			sc.close();
			
		} catch (FileNotFoundException e) {
			System.err.println("Erro ao carregar pontuações (ficheiro não encontrado): " + e.getMessage());
		}
	}

	@Override
	public void update(Observed source) {
		
		if(!isGameOver){
			if(SmallFish.getInstance().hasWon() && BigFish.getInstance().hasWon()){
				nextLevel();
			}

			if((SmallFish.getInstance().hasWon() || BigFish.getInstance().hasWon()) && !onePlayer){
				playingFish = !playingFish;
				onePlayer = true;			// faz com que ao um dos peixes ganhar ele fica unplayable 
			}								// e desta forma só um deles fica ativo
		}

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

	private void updateScoresDisplay() {
		scores.sort((s1, s2) -> Integer.compare(s1.getScore(), s2.getScore()));
		List<String> scoreStrings = new ArrayList<>();
		
		for (Score s : scores) {
			scoreStrings.add(s.toString()); 
		}
		
		ImageGUI.getInstance().setScoreEntries(scoreStrings);
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
			processNewScore();
		}
	}

	public void resetLevel(){										// Reset tem de dar reset em tudo

		if (isGameOver) {
			playedLevels = 0;
			int currentGuiTicks = ImageGUI.getInstance().getTicks(); 
			lastTickProcessed = currentGuiTicks;
			gameStartTimeTicks = currentGuiTicks;
			isGameOver = false;
		}

		currentRoom = rooms.get("room" + playedLevels + ".txt");
		ImageGUI.getInstance().setStatusMessage("Level " + (getPlayedLevels() + 1) + ": Good luck!");
		onePlayer = false;
		playingFish = true;
		setupFishesForCurrentRoom();
		currentRoom.resetAll();			//reseta todos os objetos moviveis
		updateGUI();
	}

	private void setupFishesForCurrentRoom() {		// se possivel tentar colocar o colocar os peixes ao dar reset juntamente com o resetAll
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

	private void saveScores() {
		scores.sort((s1, s2) -> Integer.compare(s1.getScore(), s2.getScore()));
		
		while (scores.size() > 5) {
			scores.remove(5);
		}

		try {
			PrintWriter writer = new PrintWriter(new File(SCORES_FILE));
			
			for (Score s : scores) {
				writer.println(s.getName() + "," + s.getScore());
			}
			writer.close();
			
		} catch (IOException e) {
			System.err.println("Erro ao guardar pontuações: " + e.getMessage());
		}
	}
	
	private void processNewScore() {
		if(isGameOver)
			return;
		isGameOver = true;
		int finalTimeInTicks = lastTickProcessed - gameStartTimeTicks;
		
		String message = "Congratulations! You finished the game!" + "\n\nInsert your name here:";
		String title = "Game Finished";
		String playerName = ImageGUI.getInstance().showInputDialog(title, message);

		if (playerName == null || playerName.trim().isEmpty()) {
			playerName = "Unknown"; 
		}

		scores.add(new Score(playerName.trim(), finalTimeInTicks));
		
		saveScores();
		
		updateScoresDisplay();
		
		ImageGUI.getInstance().setStatusMessage("The game is Over! Check the top 5. (R for restart)");
	}
}
