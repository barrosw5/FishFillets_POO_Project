package objects;

public class Score {
    private String name;
    private int scoreInTicks;   // 1 tick == 1 segundo

    public Score(String name, int score) {
        this.name = name;
        this.scoreInTicks = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() { 
        return scoreInTicks;
    }


    @Override
    public String toString() {
        
        int minutes = getScore() / 60;
        int seconds = getScore() % 60;

        String formattedTime = String.format("%02d:%02d", minutes, seconds);
        
        // 4. Esta é a string que a UI vai mostrar
        return "Player: " + getName() + " - Time: " + formattedTime;
    }

    
}
