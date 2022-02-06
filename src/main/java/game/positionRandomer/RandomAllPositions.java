package game.positionRandomer;

import game.Battlefield;
import game.Vector2d;

import java.util.Random;

public class RandomAllPositions implements RandomizingStrategy{
    private Battlefield battlefield;
    private Random random = new Random();

    public RandomAllPositions(Battlefield battlefield) {
        this.battlefield = battlefield;
    }

    @Override
    public Vector2d randomPosition() {
        int xCoordinate = random.nextInt(battlefield.size);
        int yCoordinate = random.nextInt(battlefield.size);
        Vector2d position = new Vector2d(xCoordinate, yCoordinate);
        while (!battlefield.isViableHitPosition(position)) {
            xCoordinate = random.nextInt(battlefield.size);
            yCoordinate = random.nextInt(battlefield.size);
            position = new Vector2d(xCoordinate, yCoordinate);
        }
        return position;
    }
}
