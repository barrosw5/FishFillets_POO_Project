package pt.iscte.poo.game;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JOptionPane;
import objects.Score;

public class ScoreManager {

    private static final String SCORES_PATH = "gamedata" + File.separator + "scores.txt";
    private List<Score> scores;

    public ScoreManager() {
        this.scores = new ArrayList<>();
        // Garante que a pasta exista para nao dar erro
        new File("gamedata").mkdirs();
        
        try {
            loadScores();
        } catch (IOException e) {
            System.err.println("Aviso: Não foi possível carregar os scores.");
        }
    }

    // Retorna a lista de scores (útil para a GUI)
    public List<Score> getScores() {
        return scores;
    }

    // --- Loading e Saving ---

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

    // Adiciona ou atualia o Score
    public void addOrUpdateScore(String name, int time, int moves) {
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
    }
}