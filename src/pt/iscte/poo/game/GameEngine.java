package pt.iscte.poo.game;

import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;
import objects.*;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;
import static pt.iscte.poo.utils.Direction.LEFT;

public class GameEngine implements Observer {

    // Estado do jogo: rooms carregadas e leaderboard
    private Map<String, Room> rooms;

    // Atributos globais do projeto 
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
    private final ScoreManager scoreManager;

    // Construtor: garante diretoria, carrega rooms, scores e inicia GUI
    public GameEngine() {
        // garante que a pasta exista para nao dar erro
        new File("gamedata").mkdirs();
        
        GameRoomsLoader loader = new GameRoomsLoader();

        try {
            this.rooms = loader.loadAllRooms(this);
        } catch (Exception e) {
            System.err.println("ERRO CRÍTICO: " + e.getMessage());
        }
    
        this.scoreManager = new ScoreManager();
        
        resetLevel();
        updateGUI();
        updateScoresDisplay();
    }


    public int getRealTime() {
        return realTime;
    }

    public void setRealTime(int time){
        realTime = time;
    }

    // --- main game jogo ---

    // Chamado pela GUI: trata vitória/derrota, input e ticks pendentes
    @Override
    public void update(Observed source) {
        if (!isGameOver) {
            checkWinConditions();
            checkLossConditions();
        }

        handleInput();

        // sincroniza o tempo com os ticks
        int currentGuiTicks = ImageGUI.getInstance().getTicks();
        while (lastTickProcessed < currentGuiTicks) {
            processTick();
        }

        updateGUI();
        ImageGUI.getInstance().update();
        updateStatusMessage();
    }

    // Vitória quando ambos os peixes saem, troca de controlo se só um sai
    private void checkWinConditions() {
        if (sf.hasWon() && bf.hasWon()) {
            nextLevel();
        } else if ((sf.hasWon() || bf.hasWon()) && !onePlayer) {
            playingFish = !playingFish;
            onePlayer = true;
        }
    }

    // Game over se algum peixe morrer
    private void checkLossConditions() {
        if (sf.hasDied() || bf.hasDied()) {
            isGameOver = true;
        }
    }

    // Teclas: R reinicia, espaço alterna peixe, WASD/Setas movem
    private void handleInput() {
        if (!ImageGUI.getInstance().wasKeyPressed())
            return;

        int k = ImageGUI.getInstance().keyPressed();

        if (k == KeyEvent.VK_R) { // se for o R reseta 
            resetLevel();
            return;
        }

        if (isGameOver) // se o jogo acabou o código pára aqui 
            return;

        switch (k) { 
            case KeyEvent.VK_SPACE: // se for SPACE troca de fish 
                if (!onePlayer) playingFish = !playingFish;
                break;
            case KeyEvent.VK_LEFT: // aqui as teclas de movimentação, left, right, up e down 
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_A:
            case KeyEvent.VK_W:
            case KeyEvent.VK_S:
            case KeyEvent.VK_D:
                GameCharacter activeFish = playingFish ? sf : bf;
                activeFish.move(Direction.directionFor(k).asVector());
                // move os inimigos (neste caso só o krab)
                Krab.moveAllCrabs(currentRoom);
                moves++;
                break;
            default:
                break;
        }
    }

    // Avança um tick: tempo, explosões, gravidade e suporte dos peixes
    private void processTick() {
        lastTickProcessed++;
        realTime++;
        
        objects.Explosion.update(currentRoom);
        GameObject.applyGravity(currentRoom);
        GameCharacter.checkSurvivalStatus(currentRoom, sf);
        GameCharacter.checkSurvivalStatus(currentRoom, bf);
    }

    // --- updates visuais ---

    // Refaz a renderização com os objetos da sala atual
    public void updateGUI() {
        if (currentRoom != null) {
            ImageGUI.getInstance().clearImages();
            ImageGUI.getInstance().addImages(currentRoom.getObjects());
        }
    }

    // Mostra tempo, nível e movimentos ou mensagem de game over
    private void updateStatusMessage() {
        String msg = !isGameOver 
            ? "Level " + (playedLevels + 1) + " | Time: " + Time.convert(realTime).toString() + " | Moves: " + moves
            : "Game Over! Check top 10. (R to restart)";
        
        ImageGUI.getInstance().setStatusMessage(msg);
    }

    // Atualiza painel lateral com scores ordenados
    private void updateScoresDisplay() {
        // tira a lista ao manager
        List<Score> scores = scoreManager.getScores(); 
        scores.sort(null); 
        List<String> scoreList = new ArrayList<>();
        for (Score s : scores)
            scoreList.add(s.toString());
        ImageGUI.getInstance().setScoreEntries(scoreList);
    }

    // --- logica dos niveis ---

    // Incrementa nível ou finaliza o jogo se não houver mais rooms
    public void nextLevel() {
        playedLevels++;
        if (playedLevels < rooms.size()) {
            resetLevel();
        } else {
            handleGameCompletion();
        }
    }

    // Reposiciona peixes e reset aos objetos resetáveis
    public void resetLevel() {
        if (isGameOver) performFullReset(); // se houve GameOver vai resetar tudo, caso contrário faz o reset do necesssário apenas

        currentRoom = rooms.get("room" + playedLevels + ".txt");
        
        onePlayer = false;
        playingFish = true;
        setupFishesInRoom();
        currentRoom.resetResettable();
        
        updateStatusMessage();
        updateGUI();
    }

    // Limpa estado de game over e reinicia contadores
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

    // Configura peixes na sala atual e garante direção/estado inicial
    private void setupFishesInRoom() {
        sf.setRoom(currentRoom); // em que Room vão os peixes 
        bf.setRoom(currentRoom);
        sf.setPosition(currentRoom.getSmallFishStartingPosition()); // a posição inicial deles 
        bf.setPosition(currentRoom.getBigFishStartingPosition());
        sf.setDirection(LEFT); // para que lado começam, LEFT, no caso 
        bf.setDirection(LEFT);
        sf.resetWin(); // reset as vitórias 
        bf.resetWin();
        sf.resetSpinned();
        bf.resetSpinned();
        
        if (initialized || !currentRoom.getObjects().contains(sf)) currentRoom.addObject(sf);
        if (initialized || !currentRoom.getObjects().contains(bf)) currentRoom.addObject(bf);
        
        initialized = false;
    }

    // --- logica do score ---

    // Quando acaba o último nível, pede nome e grava no top 10
    private void handleGameCompletion() {
        if (isGameOver) return; 
        isGameOver = true; // faz com q a variável que controla o GameOver fica a true 

        String playerName = ImageGUI.getInstance().showInputDialog("Game Finished", 
            "Congratulations! You finished!\n\nInsert your name:"); // pede o nome do jogador
        
        if (playerName == null || playerName.trim().isEmpty())
            playerName = "Unknown"; // se for vazio escrever Unknown 

        String cleanName = playerName.trim(); 

        int finalTime = lastTickProcessed - gameStartTimeTicks; // o temp final do jogador 
        
        scoreManager.addOrUpdateScore(cleanName, finalTime, moves); // adiciona ao score
        updateScoresDisplay();
        ImageGUI.getInstance().setStatusMessage("The game is Over! Check the top 10. (R for restart)"); // e a msg final
    }
    
    public int getPlayedLevels() {
        return playedLevels;
    }
}
