package game;

import java.util.Stack;

public class ShipFocuser {
    private final Battlefield battlefield;
    private final ComputerPlayer computerPlayer;
    Stack<Vector2d> positionsToCheck = new Stack<>();

    public ShipFocuser(Battlefield battlefield, ComputerPlayer computerPlayer) {
        this.battlefield = battlefield;
        this.computerPlayer = computerPlayer;
    }

    public void focusOnShip(Vector2d position) {
        positionsToCheck.push(position);
        Vector2d currentPosition;
        while (!positionsToCheck.isEmpty()) {
            currentPosition = positionsToCheck.pop();
            checkPositionsAroundPosition(currentPosition);
        }
    }

    public void checkPositionsAroundPosition(Vector2d position) {
        sortOutPosition(new Vector2d(position.xCoordinate - 1, position.yCoordinate));
        sortOutPosition(new Vector2d(position.xCoordinate - 1, position.yCoordinate + 1));
        sortOutPosition(new Vector2d(position.xCoordinate, position.yCoordinate + 1));
        sortOutPosition(new Vector2d(position.xCoordinate + 1, position.yCoordinate + 1));
        sortOutPosition(new Vector2d(position.xCoordinate + 1, position.yCoordinate));
        sortOutPosition(new Vector2d(position.xCoordinate + 1, position.yCoordinate - 1));
        sortOutPosition(new Vector2d(position.xCoordinate, position.yCoordinate - 1));
        sortOutPosition(new Vector2d(position.xCoordinate - 1, position.yCoordinate - 1));
    }

    public void sortOutPosition(Vector2d position) {
        if (battlefield.positionIsEmpty(position)) battlefield.putEmptyPositionOnBattlefield(position);
        else if (battlefield.positionIsNotChecked(position)) {
            battlefield.makePositionChecked(position);
            computerPlayer.addPositionToHit(position);
            positionsToCheck.push(position);
        }
    }
}
