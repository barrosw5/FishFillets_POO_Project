package objects;

public class Score implements Comparable<Score> {
    private String name;
    private int scoreInTicks;   // 1 tick == 1 segundo
    private int moves;

    // Guarda um registo de pontuação para o quadro de scores
    public Score(String name, int score, int moves) {
        this.name = name;
        this.scoreInTicks = score;
        this.moves = moves;
    }

    public String getName() {
        return name;
    }

    public int getScore() { 
        return scoreInTicks;
    }

    public int getMoves() {
        return moves;
    }

    // Função para comprar Scores, serve essencialmente para ordená-los. 

    @Override
    public int compareTo(Score other) {
        if (this.scoreInTicks != other.scoreInTicks) {
            return Integer.compare(this.scoreInTicks, other.scoreInTicks);
        }
        return Integer.compare(this.moves, other.moves);
    }

    // Converte o tempo em mm:ss para apresentação
    @Override
    public String toString() {
        int minutes = getScore() / 60;
        int seconds = getScore() % 60;

        String formattedTime = String.format("%02d:%02d", minutes, seconds);
        
        return getName() + " - " + formattedTime;
    }
}
