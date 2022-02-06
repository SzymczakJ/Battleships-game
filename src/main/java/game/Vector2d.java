package game;

import game.enums.BattlefieldOrientation;

import java.util.Objects;

public class Vector2d {
    public final int xCoordinate;
    public final int yCoordinate;


    @Override
    public String toString() {
        return "Vector2d{" +
                "xCoordinate=" + xCoordinate +
                ", yCoordinate=" + yCoordinate +
                '}';
    }

    public Vector2d(int xCoordinate, int yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2d position = (Vector2d) o;
        return xCoordinate == position.xCoordinate && yCoordinate == position.yCoordinate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xCoordinate, yCoordinate);
    }

    public Vector2d incrementAccordingToOrientation(BattlefieldOrientation orientation) {
        if (orientation == BattlefieldOrientation.HORIZONTAL) {
            return new Vector2d(xCoordinate + 1, yCoordinate);
        }
        else {
            return new Vector2d(xCoordinate, yCoordinate + 1);
        }
    }
}
