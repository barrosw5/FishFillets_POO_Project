package pt.iscte.poo.game;

import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;
import objects.*;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;
import static pt.iscte.poo.utils.Direction.LEFT;

public class GameEngine implements Observer {

    private static final String SCORES_PATH = "gamedata" + File.separator + "scores.txt";
    private static final String ROOMS_DIR = "./rooms";

    private Map<String, Room> rooms = new HashMap<>();
    private List<Score> scores = new ArrayList<>();
    
    private Room currentRoom;
    private int playedLevels = 0;
    private int moves = 0;
    private int realTime = 0;
    private int gameStartTimeTicks = 0;
    private int lastTickProcessed = 0;

    private boolean playingFish = true; 
    private boolean onePlayer = false;  
    private boolean initialized = true;
    private boolean isGameOver = false;

    private SmallFish sf = SmallFish.getInstance();
    private BigFish bf = BigFish.getInstance();

    public GameEngine() {
        // assegura que a diretoria exista
        new File("gamedata").mkdirs();
        
        loadGameRooms();
        
        try {
            loadScores();
        } catch (IOException e) {
            System.err.println("Aviso: Não foi possível carregar os scores.");
        }
        
        resetLevel();
        updateGUI();
        updateScoresDisplay();
    }

    // --- Loading & Saving (Com Throws) ---

    private void loadGameRooms() {
        File dir = new File(ROOMS_DIR);
        if (!dir.exists()) {
            System.err.println("Diretoria das rooms não encontrada: " + ROOMS_DIR);
            return;
        }

        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                rooms.put(f.getName(), Room.readRoom(f, this));
            }
        }
    }

    private void loadScores() throws IOException {
        File file = new File(SCORES_PATH);
        
        if (!file.exists()) {
            boolean created = file.createNewFile();
            if (!created) {
                throw new IOException("Falha ao criar o ficheiro de scores: " + SCORES_PATH);
            }
            return; 
        }

        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                processScoreLine(line);
            }
            scores.sort(null);
        }
    }

    private void processScoreLine(String line) {
        String[] parts = line.split(",");
        if (parts.length == 3) {
            try {
                String name = parts[0];
                int score = Integer.parseInt(parts[1].trim());
                int mvs = Integer.parseInt(parts[2].trim());
                scores.add(new Score(name, score, mvs));
            } catch (NumberFormatException e) {
                System.err.println("Score ignorado (formato inválido): " + line);
            }
        }
    }

    private void saveScores() throws IOException {
        scores.sort(null);
        while (scores.size() > 10) scores.remove(10);

        try (PrintWriter writer = new PrintWriter(new File(SCORES_PATH))) {
            for (Score s : scores) {
                writer.println(s.getName() + "," + s.getScore() + "," + s.getMoves());
            }
        }
    }

    // --- loop do jogo ---

    @Override
    public void update(Observed source) {
        if (!isGameOver) {
            checkWinConditions();
            checkLossConditions();
        }

        handleInput();

        int currentGuiTicks = ImageGUI.getInstance().getTicks();
        while (lastTickProcessed < currentGuiTicks) {
            processTick();
        }

        updateGUI();
        ImageGUI.getInstance().update();
        updateStatusMessage();
    }

    private void checkWinConditions() {
        if (sf.hasWon() && bf.hasWon()) {
            nextLevel();
        } else if ((sf.hasWon() || bf.hasWon()) && !onePlayer) {
            playingFish = !playingFish;
            onePlayer = true;
        }
    }

    private void checkLossConditions() {
        if (sf.hasDied() || bf.hasDied()) {
            isGameOver = true;
        }
    }

    private void handleInput() {
        if (!ImageGUI.getInstance().wasKeyPressed())
            return;

        int k = ImageGUI.getInstance().keyPressed();

        if (k == KeyEvent.VK_R) {
            resetLevel();
            return;
        }

        if (isGameOver)
            return;

        switch (k) {
            case KeyEvent.VK_SPACE:
                if (!onePlayer) playingFish = !playingFish;
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_A:
            case KeyEvent.VK_W:
            case KeyEvent.VK_S:
            case KeyEvent.VK_D:
                GameCharacter activeFish = playingFish ? sf : bf;
                activeFish.move(Direction.directionFor(k).asVector());
                Krab.moveAllCrabs(currentRoom);
                moves++;
                break;
            default:
                break;
        }
    }

    private void processTick() {
        lastTickProcessed++;
        realTime++;
        
        objects.Explosion.update(currentRoom);
        GameObject.applyGravity(currentRoom);
        GameCharacter.checkSurvivalStatus(currentRoom, sf);
        GameCharacter.checkSurvivalStatus(currentRoom, bf);
    }

    // --- updates visuais ---

    public void updateGUI() {
        if (currentRoom != null) {
            ImageGUI.getInstance().clearImages();
            ImageGUI.getInstance().addImages(currentRoom.getObjects());
        }
    }

    private void updateStatusMessage() {
        String msg = !isGameOver 
            ? "Level " + (playedLevels + 1) + " | Time: " + Time.convert(realTime).toString() + " | Moves: " + moves
            : "Game Over! Check top 10. (R to restart)";
        
        ImageGUI.getInstance().setStatusMessage(msg);
    }

    private void updateScoresDisplay() {
        scores.sort(null);
        List<String> scoreList = new ArrayList<>();
        for (Score s : scores) scoreList.add(s.toString());
        ImageGUI.getInstance().setScoreEntries(scoreList);
    }

    // --- logica dos niveis ---

    public void nextLevel() {
        playedLevels++;
        if (playedLevels < rooms.size()) {
            resetLevel();
        } else {
            handleGameCompletion();
        }
    }

    public void resetLevel() {
        if (isGameOver) performFullReset();

        currentRoom = rooms.get("room" + playedLevels + ".txt");
        
        onePlayer = false;
        playingFish = true;
        setupFishesInRoom();
        currentRoom.resetResettable();
        
        updateStatusMessage();
        updateGUI();
    }

    private void performFullReset() {
        playedLevels = 0;
        int currentTicks = ImageGUI.getInstance().getTicks();
        lastTickProcessed = currentTicks;
        gameStartTimeTicks = currentTicks;
        realTime = 0;
        moves = 0;
        isGameOver = false;
        sf.setDeadState(false);
        bf.setDeadState(false);
    }

    private void setupFishesInRoom() {
        sf.setRoom(currentRoom);
        bf.setRoom(currentRoom);
        sf.setPosition(currentRoom.getSmallFishStartingPosition());
        bf.setPosition(currentRoom.getBigFishStartingPosition());
        sf.setDirection(LEFT);
        bf.setDirection(LEFT);
        sf.resetWin();
        bf.resetWin();

        if (initialized || !currentRoom.getObjects().contains(sf)) currentRoom.addObject(sf);
        if (initialized || !currentRoom.getObjects().contains(bf)) currentRoom.addObject(bf);
        
        initialized = false;
    }

    // --- logica do score ---

    private void handleGameCompletion() {
        if (isGameOver) return;
        isGameOver = true;

        String playerName = ImageGUI.getInstance().showInputDialog("Game Finished", 
            "Congratulations! You finished!\n\nInsert your name:");
        
        if (playerName == null || playerName.trim().isEmpty())
            playerName = "Unknown";

        String cleanName = playerName.trim();

        int finalTime = lastTickProcessed - gameStartTimeTicks;
        
        addOrUpdateScore(cleanName, finalTime, moves);
        ImageGUI.getInstance().setStatusMessage("The game is Over! Check the top 10. (R for restart)");
    }

    private void addOrUpdateScore(String name, int time, int moves) {
        Score existing = null;
        for (Score s : scores) {
            if (s.getName().equals(name)) {
                existing = s;
                break;
            }
        }

        if (existing != null) {
            int resp = JOptionPane.showConfirmDialog(null, 
                "Player " + name + " exists. Replace score?", "Duplicate Name", JOptionPane.YES_NO_OPTION);
            if (resp != JOptionPane.YES_OPTION) return;
            
            scores.remove(existing);
        }
        
        scores.add(new Score(name, time, moves));
        
        try {
            saveScores();
        } catch (IOException e) {
            System.err.println("Erro ao gravar: " + e.getMessage());
        }
        
        updateScoresDisplay();
    }
    
    public int getPlayedLevels() {
        return playedLevels;
    }
}