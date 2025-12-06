package objects;

public class Time { // Classe time para fazer o tempo em tempo real 
    private int minutes;
    private int seconds;

    // Guarda uma representação simples de tempo
    public Time ( int minutes, int seconds) {
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public int getMinutes() {
        return minutes;
    }



    public int getSeconds() {
        return seconds;
    }

    // método static para converter segundos totais em minutos/segundos
    public static Time convert(int timeSeconds) {
        int minutes = timeSeconds/ 60;
        int seconds = timeSeconds % 60;

        return new Time(minutes, seconds);
    }

    @Override
    public String toString() {
        return getMinutes() + "m" + getSeconds() + "s";
    }
}
