package game.enums;

import game.Vector2d;

public enum ShootDirection {
    UP,
    RIGHT,
    DOWN,
    LEFT;

    public Vector2d shootInDirection(Vector2d position) {
        return switch (this) {
            case UP -> new Vector2d(position.xCoordinate - 1, position.yCoordinate);
            case RIGHT -> new Vector2d(position.xCoordinate, position.yCoordinate + 1);
            case DOWN -> new Vector2d(position.xCoordinate + 1, position.yCoordinate);
            case LEFT -> new Vector2d(position.xCoordinate, position.yCoordinate - 1);
        };
    }

    public ShootDirection oppositeDirection() {
        return switch (this) {
            case UP -> DOWN;
            case RIGHT -> LEFT;
            case DOWN -> UP;
            case LEFT -> RIGHT;
        };
    }
}
