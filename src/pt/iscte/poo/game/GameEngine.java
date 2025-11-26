package pt.iscte.poo.game;

import static pt.iscte.poo.utils.Direction.LEFT;

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
import objects.BigFish;
import objects.GameCharacter;
import objects.GameObject;
import objects.Score;
import objects.SmallFish;
import objects.Time;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;

public class GameEngine implements Observer {
    
    private static final String SCORES_FILE = "gamedata" + File.separator + "scores.txt";
    private Map<String,Room> rooms;
    private List<Score> scores; 
    private int gameStartTimeTicks = 0;
    private Room currentRoom;
    private int lastTickProcessed = 0;
    private boolean playingFish = true; 
    private int playedLevels = 0;
    private boolean onePlayer = false; 
    private boolean initialized = true; 
    private boolean isGameOver = false; 
    private int realTime = 0;
    private int moves = 0; 

    private SmallFish sf = SmallFish.getInstance();
    private BigFish bf = BigFish.getInstance();
    
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
        if (files != null) {
            for(File f : files) {
                rooms.put(f.getName(),Room.readRoom(f,this));
            }
        }
    }

    private void loadScores(){
        File file = new File(SCORES_FILE);

        try {           
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
                String[] parts = line.split(",");   // Formato esperado: "Nome,Pontos,Moves"
                
                if (parts.length == 3) { 
                    try {
                        String name = parts[0];
                        int scoreInTicks = Integer.parseInt(parts[1].trim());
                        int movesCount = Integer.parseInt(parts[2].trim());
                        
                        scores.add(new Score(name, scoreInTicks, movesCount));
                    } catch (NumberFormatException e) {
                        System.err.println("Erro ao ler linha de score (formato numérico inválido): " + line);
                    }
                }
            }
            // porque já temos comparable usamos null
            scores.sort(null);
            sc.close();
            
        } catch (FileNotFoundException e) {
            System.err.println("Erro ao carregar pontuações: " + e.getMessage());
        }
    }

    @Override
    public void update(Observed source) {
        
        if(!isGameOver){
            if(sf.hasWon() && bf.hasWon()){
                nextLevel();
            }

            if((sf.hasWon() || bf.hasWon()) && !onePlayer){
                playingFish = !playingFish;
                onePlayer = true;           
            } 

            if(sf.hasDied() || bf.hasDied())
                isGameOver = true;
        }

        if (ImageGUI.getInstance().wasKeyPressed()) {
            int k = ImageGUI.getInstance().keyPressed();
            
            switch (k) {
                case KeyEvent.VK_SPACE:
                    if(!onePlayer){
                        playingFish = !playingFish; 
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
                    if(!isGameOver){
                        if(playingFish){
                            sf.move(Direction.directionFor(k).asVector());
                            moves++; // Incrementa moves
                        }
                        else {
                            bf.move(Direction.directionFor(k).asVector());
                            moves++; // Incrementa moves
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        int t = ImageGUI.getInstance().getTicks();
        while (lastTickProcessed < t) {
            processTick();
        }
        updateGUI();
        ImageGUI.getInstance().update();
        
        if(!isGameOver)
            ImageGUI.getInstance().setStatusMessage("Level " + (getPlayedLevels() + 1) + " | Time: " + realTime() + " | Moves: " + moves); 
        else
            ImageGUI.getInstance().setStatusMessage("Game Over! Check top 5. (R to restart)");
    }

    private void processTick() {
        lastTickProcessed++;
        realTime++;
        
        objects.Explosion.update(currentRoom);  // Apenas esta linha limpa as explosões antigas automaticamente
        GameObject.gravityMove(currentRoom); 
        GameCharacter.CharacterSuppor(currentRoom, sf);
        GameCharacter.CharacterSuppor(currentRoom, bf);
    }

    public String realTime() { 
        Time realTimeLast = Time.convert(realTime);
        return realTimeLast.toString();
    }

    public void updateGUI() {
        if(currentRoom != null) {
            ImageGUI.getInstance().clearImages();
            ImageGUI.getInstance().addImages(currentRoom.getObjects());
        }
    }

    private void updateScoresDisplay() {
        scores.sort(null); 
        
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

    public void resetLevel(){

        if (isGameOver) {
            playedLevels = 0;
            int currentGuiTicks = ImageGUI.getInstance().getTicks(); 
            lastTickProcessed = currentGuiTicks;
            gameStartTimeTicks = currentGuiTicks;
            realTime = 0;
            isGameOver = false;
            moves = 0;
            
            sf.setDeadState(false);
            bf.setDeadState(false);
        }

        currentRoom = rooms.get("room" + playedLevels + ".txt");
        ImageGUI.getInstance().setStatusMessage("Level " + (getPlayedLevels() + 1) + " | Time: " + realTime() + " | Moves: " + moves);
        onePlayer = false;
        playingFish = true;
        setupFishesForCurrentRoom();
        currentRoom.resetAll();        
        updateGUI();
    }

    private void setupFishesForCurrentRoom() {      
        sf.setRoom(currentRoom);
        bf.setRoom(currentRoom);

        sf.setPosition(currentRoom.getSmallFishStartingPosition());
        bf.setPosition(currentRoom.getBigFishStartingPosition());

        sf.setDirection(LEFT);
        bf.setDirection(LEFT);

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
        scores.sort(null);
        
        while (scores.size() > 10) {
            scores.remove(10);
        }

        try {
            PrintWriter writer = new PrintWriter(new File(SCORES_FILE));
            
            for (Score s : scores) {
                writer.println(s.getName() + "," + s.getScore() + "," + s.getMoves());
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
        int finalMoves = this.moves;
        
        String message = "Congratulations! You finished!" + "\n\nInsert your name:";
        String title = "Game Finished";
        
        String playerName = ImageGUI.getInstance().showInputDialog(title, message);

        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "Unknown"; 
        }
        
        String cleanName = playerName.trim();

        Score existingScore = null;
        for (Score s : scores) {
            if (s.getName().equals(cleanName)) {
                existingScore = s;
                break;
            }
        }

        if (existingScore != null) {
            int response = javax.swing.JOptionPane.showConfirmDialog(null, "Player " + cleanName + " exists. Replace score?", "Duplicate Name", javax.swing.JOptionPane.YES_NO_OPTION);

            if (response == javax.swing.JOptionPane.YES_OPTION) {
                scores.remove(existingScore);
                scores.add(new Score(cleanName, finalTimeInTicks, finalMoves));
                saveScores(); 
                updateScoresDisplay();
            }
            
        } else {
            scores.add(new Score(cleanName, finalTimeInTicks, finalMoves));
            saveScores();
            updateScoresDisplay();
        }
        
        ImageGUI.getInstance().setStatusMessage("The game is Over! Check the top 5. (R for restart)");
    }
}