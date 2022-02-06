package game;

public class PlayerPointCounter {
    private int pointCounter = 0;

    public void addPoints(int points) {
        pointCounter += points;
    }

    public int getPoints() {
        return pointCounter;
    }
}
