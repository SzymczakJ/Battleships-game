package game;

import game.enums.ShootDirection;

import java.util.Stack;

public class ShipFocuser {
    private final Battlefield battlefield;
    private final ComputerPlayer computerPlayer;

    public ShipFocuser(Battlefield battlefield, ComputerPlayer computerPlayer) {
        this.battlefield = battlefield;
        this.computerPlayer = computerPlayer;
    }

    public void focusOnShip(Vector2d position) {
        ShootDirection shootDirection = findWhereToShoot(position);
        if (battlefield.shipWasSunk(position)) {
            battlefield.cleanUpTheWreck(position);
        }
        else {
            shootInLine(position, shootDirection);
            shootDirection = shootDirection.oppositeDirection();
            shootInLine(position, shootDirection);
            battlefield.cleanUpTheWreck(position);
        }
    }

    private ShootDirection findWhereToShoot(Vector2d position) {
        Vector2d newPosition = new Vector2d(position.xCoordinate - 1, position.yCoordinate);
        sortOutPosition(newPosition);
        if (battlefield.isViableHitPosition(newPosition)) {
            return ShootDirection.UP;
        }
        newPosition = new Vector2d(position.xCoordinate, position.yCoordinate + 1);
        sortOutPosition(newPosition);
        if (battlefield.isViableHitPosition(newPosition)) {
            return ShootDirection.RIGHT;
        }
        newPosition = new Vector2d(position.xCoordinate + 1, position.yCoordinate);
        sortOutPosition(newPosition);
        if (battlefield.isViableHitPosition(newPosition)) {
            return ShootDirection.DOWN;
        }
        newPosition = new Vector2d(position.xCoordinate, position.yCoordinate - 1);
        sortOutPosition(newPosition);
        return ShootDirection.LEFT;
    }

    private void shootInLine(Vector2d position, ShootDirection shootDirection) {
        Vector2d newPosition = shootDirection.shootInDirection(position);
        while (!battlefield.shipWasSunk(newPosition) && battlefield.isViableHitPosition(newPosition) && battlefield.positionInBounds(newPosition) && !battlefield.positionIsEmpty(newPosition)) {
            sortOutPosition(newPosition);
            newPosition = shootDirection.shootInDirection(newPosition);
        }
        if (battlefield.positionIsEmpty(newPosition) && !battlefield.shipWasSunk(newPosition)) {
            sortOutPosition(newPosition);
        }
    }

    public void sortOutPosition(Vector2d position) {
        if (battlefield.positionIsEmpty(position)) {
            computerPlayer.addPositionToHit(position);
            battlefield.putEmptyPositionOnBattlefield(position);
        }
        else if (battlefield.positionIsNotChecked(position)) {
            battlefield.makePositionChecked(position);
            computerPlayer.addPositionToHit(position);
        }
    }
}
