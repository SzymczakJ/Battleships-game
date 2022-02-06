package game.positionRandomer;

import game.Battlefield;
import game.Vector2d;

import java.util.Random;

public class RandomBlackPositions implements RandomizingStrategy {
    private Battlefield battlefield;
    private Random random = new Random();

    public RandomBlackPositions(Battlefield battlefield) {
        this.battlefield = battlefield;
    }

    @Override
    public Vector2d randomPosition() {
        int xCoordinate = random.nextInt(battlefield.size);
        int addition = xCoordinate % 2;
        int yCoordinate = 2 * random.nextInt((int) battlefield.size / 2) + addition;
        Vector2d position = new Vector2d(xCoordinate, yCoordinate);
        while (!battlefield.isViableHitPosition(position)) {
            xCoordinate = random.nextInt(battlefield.size);
            addition = xCoordinate % 2;
            yCoordinate = 2 * random.nextInt((int) battlefield.size / 2) + addition;
            position = new Vector2d(xCoordinate, yCoordinate);
        }
        return new Vector2d(xCoordinate, yCoordinate);
    }
}
