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

    // Estado do jogo: rooms carregadas e leaderboard
    private Map<String, Room> rooms = new HashMap<>();
    private List<Score> scores = new ArrayList<>();
    

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

    // Construtor: garante diretoria, carrega rooms, scores e inicia GUI
    public GameEngine() {
        // garante que a pasta exista para nao dar erro
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

    // --- Loading e Saving ---

    // Varre a pasta rooms e cria as instâncias correspondentes
    private void loadGameRooms() {
        File dir = new File(ROOMS_DIR); // usa a diretoria em que as rooms estão presentes 
        if (!dir.exists()) { // se não existe ele lança um erro diz que não foram encontradas rooms 
            System.err.println("Diretoria das rooms não encontrada: " + ROOMS_DIR);
            return;
        }

        File[] files = dir.listFiles(); // exitindo, cria uma lista com em que cada indice representa um file
        if (files != null) {
            for (File f : files) { // percorre a Lista de files e adiciona no HashMap das Rooms o file que corresponde a cada Room
                rooms.put(f.getName(), Room.readRoom(f, this)); // usa a função readRoom para ler o file em si e as informações 
            }
        }
    }

    // Lê ficheiro de scores (cria se não existir) e ordena
    private void loadScores() throws IOException {
        File file = new File(SCORES_PATH); // o file onde contrm os scores 
        
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
            // ordena logo ao carregar
            scores.sort(null);
        }
    }

    // Converte uma linha do file num Score válido, ignorando erros
    private void processScoreLine(String line) {
        String[] parts = line.split(","); // separa a linha num array em que os indices foram separados por ","
        if (parts.length == 3) { // se tiver três de tamanho avança 
            try {
                String name = parts[0];
                int score = Integer.parseInt(parts[1].trim());
                int mvs = Integer.parseInt(parts[2].trim());
                scores.add(new Score(name, score, mvs)); // faz o devido seperador e adiciona o score à lista 
            } catch (NumberFormatException e) {
                System.err.println("Score ignorado (formato inválido): " + line);
            }
        }
        // se nao forem linhas de 3 partes separadas por , ele ignora
    }

    // Método em que basicamente salva os Scores
    private void saveScores() throws IOException {
        scores.sort(null); //  ordena 
        while (scores.size() > 10) scores.remove(10); // while para garantir que só tem mesmo o TOP10 dos scores na lista 

        try (PrintWriter writer = new PrintWriter(new File(SCORES_PATH))) { // tenta escrever os scores no file pré-definido 
            for (Score s : scores) {
                writer.println(s.getName() + "," + s.getScore() + "," + s.getMoves());
            }
        }
        catch(IOException e){
            System.err.println("Erro ao escrever no ficheiro de scores.txt");
        }
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
                Krab.moveAllKrabs(currentRoom);
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
        scores.sort(null);
        List<String> scoreList = new ArrayList<>();
        for (Score s : scores) scoreList.add(s.toString());
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

        int finalTime = lastTickProcessed - gameStartTimeTicks; // o teu final do jogador 
        
        addOrUpdateScore(cleanName, finalTime, moves); // adiciona ao score
        ImageGUI.getInstance().setStatusMessage("The game is Over! Check the top 10. (R for restart)"); // e a msg final
    }

    // Adiciona ou atualia o Score

    private void addOrUpdateScore(String name, int time, int moves) {
        Score existing = null;
        for (Score s : scores) {  // se exitir já, verifica isso 
            if (s.getName().equals(name)) {
                existing = s;
                break;
            }
        }

        if (existing != null) { // se realmente assistir. o jogo pergunta se quer mesmo confirmar a nova pontuação 
            int resp = JOptionPane.showConfirmDialog(null, 
                "Player " + name + " exists. Replace score?", "Duplicate Name", JOptionPane.YES_NO_OPTION);
            if (resp != JOptionPane.YES_OPTION) return;
            
            scores.remove(existing); // remove a antiga
        }
        
        scores.add(new Score(name, time, moves)); // e adiciona a nova ou substitui 
        
        try { // tenta salvar o score
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
