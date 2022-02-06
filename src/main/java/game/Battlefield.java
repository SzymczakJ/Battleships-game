package game;

import game.enums.BattlefieldOrientation;
import game.enums.PositionType;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Battlefield {
    public final int size;
    private Map<Vector2d, BattlefieldPosition> battlefieldPositions = new HashMap<>();
    Random random = new Random();

    public Battlefield(int size) {
        this.size = size;
    }

    public boolean placeShipOnBattlefield(BattleShip ship, int battleshipId, BattlefieldOrientation shipOrientation, Vector2d position) {
        Vector2d startingPosition = position;
        for(int i = 0; i < ship.sizeOfBattleship; i++) {
            if (!positionInBounds(position) || !checkForSurroundingShips(position)) {
                return false;
            }
            position = position.incrementAccordingToOrientation(shipOrientation);
        }
        position = startingPosition;
        for (int i = 0; i < ship.sizeOfBattleship; i++) {
            battlefieldPositions.put(position, new BattlefieldPosition(position, ship.pointsForHit, PositionType.NOTCHECKED));
            position = position.incrementAccordingToOrientation(shipOrientation);
        }
        return true;
    }

    public boolean checkForSurroundingShips(Vector2d position) {
        return positionIsEmpty(new Vector2d(position.xCoordinate - 1, position.yCoordinate)) &&
                positionIsEmpty(new Vector2d(position.xCoordinate - 1, position.yCoordinate + 1)) &&
                positionIsEmpty(new Vector2d(position.xCoordinate, position.yCoordinate + 1)) &&
                positionIsEmpty(new Vector2d(position.xCoordinate + 1, position.yCoordinate + 1)) &&
                positionIsEmpty(new Vector2d(position.xCoordinate + 1, position.yCoordinate)) &&
                positionIsEmpty(new Vector2d(position.xCoordinate + 1, position.yCoordinate - 1)) &&
                positionIsEmpty(new Vector2d(position.xCoordinate, position.yCoordinate - 1)) &&
                positionIsEmpty(new Vector2d(position.xCoordinate - 1, position.yCoordinate - 1));
    }

    public boolean positionIsEmpty(Vector2d position) {
        return !battlefieldPositions.containsKey(position);
    }

    public boolean positionIsShipUnit(Vector2d position) {
        if (!battlefieldPositions.containsKey(position)) return false;
        else return battlefieldPositions.get(position).getPositionType() == PositionType.ALREADYHITSHIP ||
                battlefieldPositions.get(position).getPositionType() == PositionType.CHECKED ||
                battlefieldPositions.get(position).getPositionType() == PositionType.NOTCHECKED;
    }

    public boolean positionIsNotChecked(Vector2d position) {
        if (battlefieldPositions.get(position) == null) return false;
        else return battlefieldPositions.get(position).getPositionType() == PositionType.NOTCHECKED;
    }

    public boolean positionInBounds(Vector2d position) {
        return position.xCoordinate >= 0 && position.xCoordinate < size &&
                position.yCoordinate >= 0 && position.yCoordinate < size;
    }

    public int pointsValueOnPosition(Vector2d position) {
        if (battlefieldPositions.get(position) == null) {
            return 0;
        }
        else return battlefieldPositions.get(position).getPointsForHitting();
    }

    public Vector2d randomPositionOnBattlefield() {
        int xCoordinate = random.nextInt(size);
        int yCoordinate = random.nextInt(size);
        return new Vector2d(xCoordinate, yCoordinate);
    }

    public void positionGotHit(Vector2d position) {
        if (battlefieldPositions.get(position) == null || battlefieldPositions.get(position).getPositionType() == PositionType.EMPTY)
            battlefieldPositions.put(position, new BattlefieldPosition(position, 0, PositionType.ALREADYHITEMPTY));
        else battlefieldPositions.get(position).shipAtPositionWasHit();
    }

    public boolean isViableHitPosition(Vector2d position) {
        if (battlefieldPositions.get(position) == null) return true;
        else {
            PositionType type = battlefieldPositions.get(position).getPositionType();
            return type == PositionType.NOTCHECKED || type == PositionType.CHECKED;
        }
    }

    public void putEmptyPositionOnBattlefield(Vector2d position) {
        battlefieldPositions.put(position, new BattlefieldPosition(position, 0 , PositionType.EMPTY));
    }

    public void makePositionChecked(Vector2d position) {
        if (battlefieldPositions.get(position) != null) {
            if (battlefieldPositions.get(position).getPositionType() == PositionType.NOTCHECKED) {
                battlefieldPositions.get(position).switchPositionType(PositionType.CHECKED);
            }
        }
    }

    public void drawBattlefield() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Vector2d position = new Vector2d(x, y);
                PositionType type = PositionType.EMPTY;
                if (!positionIsEmpty(position)) type = battlefieldPositions.get(position).getPositionType();
                if (positionIsEmpty(position)) System.out.print("0 ");
                else if (type == PositionType.EMPTY) System.out.print("1 ");
                else if (type == PositionType.ALREADYHITEMPTY || type == PositionType.ALREADYHITSHIP) System.out.print("2 ");
                else if (type == PositionType.CHECKED) System.out.print("3 ");
                else if (type == PositionType.NOTCHECKED) System.out.print("4 ");
            }
            System.out.println();
        }
    }

    public PositionType getPositionType(Vector2d position) {
        if (battlefieldPositions.get(position) == null) return PositionType.EMPTY;
        else return battlefieldPositions.get(position).getPositionType();
    }

    public boolean noShipsToSinkNear(Vector2d position) {
        return (positionIsEmpty(new Vector2d(position.xCoordinate - 1, position.yCoordinate)) ||
                battlefieldPositions.get(new Vector2d(position.xCoordinate - 1, position.yCoordinate)).getPositionType() != PositionType.NOTCHECKED) &&
                (positionIsEmpty(new Vector2d(position.xCoordinate, position.yCoordinate + 1)) ||
                        battlefieldPositions.get(new Vector2d(position.xCoordinate, position.yCoordinate + 1)).getPositionType() != PositionType.NOTCHECKED) &&
                (positionIsEmpty(new Vector2d(position.xCoordinate + 1, position.yCoordinate)) ||
                        battlefieldPositions.get(new Vector2d(position.xCoordinate + 1, position.yCoordinate)).getPositionType() != PositionType.NOTCHECKED) &&
                (positionIsEmpty(new Vector2d(position.xCoordinate, position.yCoordinate - 1)) ||
                        battlefieldPositions.get(new Vector2d(position.xCoordinate, position.yCoordinate - 1)).getPositionType() != PositionType.NOTCHECKED);
    }

    public boolean isShipAtPosition(Vector2d position) {
        return !positionIsEmpty(position) &&
                ((battlefieldPositions.get(new Vector2d(position.xCoordinate, position.yCoordinate)).getPositionType() == PositionType.NOTCHECKED)
                || (battlefieldPositions.get(new Vector2d(position.xCoordinate, position.yCoordinate)).getPositionType() == PositionType.CHECKED)
                || (battlefieldPositions.get(new Vector2d(position.xCoordinate, position.yCoordinate)).getPositionType() == PositionType.ALREADYHITSHIP));
    }

    public Vector2d hornyShipsInLeftVincinity(Vector2d position) {
        if (isShipAtPosition(new Vector2d(position.xCoordinate + 1, position.yCoordinate)))
            return new Vector2d(position.xCoordinate + 1, position.yCoordinate);
        else if (isShipAtPosition(new Vector2d(position.xCoordinate, position.yCoordinate + 1)))
            return new Vector2d(position.xCoordinate, position.yCoordinate + 1);
        return null;
    }

    public Vector2d hornyShipsInRightVincinity(Vector2d position) {
        if (isShipAtPosition(new Vector2d(position.xCoordinate - 1, position.yCoordinate)))
            return new Vector2d(position.xCoordinate - 1, position.yCoordinate);
        else if (isShipAtPosition(new Vector2d(position.xCoordinate, position.yCoordinate - 1)))
            return new Vector2d(position.xCoordinate, position.yCoordinate - 1);
        return null;
    }

    public boolean shipWasSunk(Vector2d position) {
        if (!noShipsToSinkNear(position)) return false;

        Vector2d nextPosition = hornyShipsInLeftVincinity(position);
        while (nextPosition != null) {
            if (!noShipsToSinkNear(nextPosition)) return false;
            nextPosition = hornyShipsInLeftVincinity(nextPosition);
        }

        nextPosition = hornyShipsInRightVincinity(position);
        while (nextPosition != null) {
            if (!noShipsToSinkNear(nextPosition)) return false;
            nextPosition = hornyShipsInRightVincinity(nextPosition);
        }
        return true;
    }

    public void cleanUpTheWreck(Vector2d position) {
        if (!shipWasSunk(position)) return;
        Vector2d nextPosition = hornyShipsInLeftVincinity(position);
        while (nextPosition != null) {
            cleanAroundPosition(nextPosition);
            nextPosition = hornyShipsInLeftVincinity(nextPosition);
        }

        nextPosition = hornyShipsInRightVincinity(position);
        while (nextPosition != null) {
            cleanAroundPosition(nextPosition);
            nextPosition = hornyShipsInRightVincinity(nextPosition);
        }
    }

    public void cleanAroundPosition(Vector2d position) {
        if (positionIsEmpty(new Vector2d(position.xCoordinate - 1, position.yCoordinate)) && positionInBounds(new Vector2d(position.xCoordinate - 1, position.yCoordinate))) {
            putEmptyPositionOnBattlefield(new Vector2d(position.xCoordinate - 1, position.yCoordinate));
        }
        if (positionIsEmpty(new Vector2d(position.xCoordinate - 1, position.yCoordinate + 1)) && positionInBounds(new Vector2d(position.xCoordinate - 1, position.yCoordinate + 1))) {
            putEmptyPositionOnBattlefield(new Vector2d(position.xCoordinate - 1, position.yCoordinate + 1));
        }
        if (positionIsEmpty(new Vector2d(position.xCoordinate, position.yCoordinate + 1)) && positionInBounds(new Vector2d(position.xCoordinate, position.yCoordinate + 1))) {
            putEmptyPositionOnBattlefield(new Vector2d(position.xCoordinate, position.yCoordinate + 1));
        }
        if (positionIsEmpty(new Vector2d(position.xCoordinate + 1, position.yCoordinate + 1)) && positionInBounds(new Vector2d(position.xCoordinate, position.yCoordinate + 1))) {
            putEmptyPositionOnBattlefield(new Vector2d(position.xCoordinate + 1, position.yCoordinate + 1));
        }
        if (positionIsEmpty(new Vector2d(position.xCoordinate + 1, position.yCoordinate)) && positionInBounds(new Vector2d(position.xCoordinate + 1, position.yCoordinate))) {
            putEmptyPositionOnBattlefield(new Vector2d(position.xCoordinate + 1, position.yCoordinate));
        }
        if (positionIsEmpty(new Vector2d(position.xCoordinate + 1, position.yCoordinate - 1)) && positionInBounds(new Vector2d(position.xCoordinate + 1, position.yCoordinate - 1))) {
            putEmptyPositionOnBattlefield(new Vector2d(position.xCoordinate + 1, position.yCoordinate - 1));
        }
        if (positionIsEmpty(new Vector2d(position.xCoordinate, position.yCoordinate - 1)) && positionInBounds(new Vector2d(position.xCoordinate, position.yCoordinate - 1))) {
            putEmptyPositionOnBattlefield((new Vector2d(position.xCoordinate, position.yCoordinate - 1)));
        }
        if (positionIsEmpty(new Vector2d(position.xCoordinate - 1, position.yCoordinate - 1)) && positionInBounds(new Vector2d(position.xCoordinate - 1, position.yCoordinate - 1))) {
            putEmptyPositionOnBattlefield((new Vector2d(position.xCoordinate - 1, position.yCoordinate - 1)));
        }
    }
}
