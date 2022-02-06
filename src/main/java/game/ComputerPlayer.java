package game;

import game.enums.BattlefieldOrientation;
import game.positionRandomer.RandomAllPositions;
import game.positionRandomer.RandomBlackPositions;
import game.positionRandomer.RandomWhitePositions;
import game.positionRandomer.RandomizingStrategy;
import javafx.stage.Stage;

import java.util.Random;
import java.util.Stack;

public class ComputerPlayer {
    private final Battlefield battlefield;
    private final RandomizingStrategy positionRandomer;
    Stack<Vector2d> positionsToHit = new Stack<>();
    private final ShipFocuser shipFocuser;
    private int points = 0;

    public ComputerPlayer(Battlefield battlefield, boolean usesSimpleStrategy) {
        this.battlefield = battlefield;
        if (usesSimpleStrategy) {
            positionRandomer = new RandomAllPositions(battlefield);
        }
        else {
            Random random = new Random();
            int x = random.nextInt(2);
            System.out.println(x);
            if (x == 1) positionRandomer = new RandomBlackPositions(battlefield);
            else positionRandomer = new RandomWhitePositions(battlefield);
        }
        shipFocuser = new ShipFocuser(battlefield, this);
    }

    public void putShipsOnMap(Battlefield battlefield, int quantityOfIronclads, int quantityOfBattleCruisers, int quantityOfDestroyers,
                              int pointsPerIroncladHit, int pointsPerBattleCruiserHit, int pointsPerDestroyerHit) {
        BattleShip ironclad = new BattleShip(5, pointsPerIroncladHit);
        BattleShip battleCruiser = new BattleShip(4, pointsPerBattleCruiserHit);
        BattleShip destroyer = new BattleShip(2, pointsPerDestroyerHit);

        int i = 0;
        BattlefieldOrientation orientation = BattlefieldOrientation.HORIZONTAL;
        while (i < quantityOfIronclads) {
            orientation = orientation.randomOrientation();
            if (battlefield.placeShipOnBattlefield(ironclad, 1, orientation, battlefield.randomPositionOnBattlefield())) i++;
        }

        i = 0;
        while (i < quantityOfBattleCruisers) {
            orientation = orientation.randomOrientation();
            if (battlefield.placeShipOnBattlefield(battleCruiser, 1, orientation, battlefield.randomPositionOnBattlefield())) i++;
        }

        i = 0;
        while (i < quantityOfDestroyers) {
            orientation = orientation.randomOrientation();
            if (battlefield.placeShipOnBattlefield(destroyer, 1, orientation, battlefield.randomPositionOnBattlefield())) i++;
        }
    }

    public void hitPosition() {
        Vector2d positionToHit;
        if (!positionsToHit.isEmpty()) {
            positionToHit = positionsToHit.pop();
            battlefield.positionGotHit(positionToHit);
            points += battlefield.pointsValueOnPosition(positionToHit);
        }
        else {
            positionToHit = positionRandomer.randomPosition();
            System.out.println(positionToHit);
            battlefield.positionGotHit(positionToHit);
            if (battlefield.positionIsShipUnit(positionToHit)) {
                points += battlefield.pointsValueOnPosition(positionToHit);
                shipFocuser.focusOnShip(positionToHit);
            }
        }
    }


    public void addPositionToHit(Vector2d position) {
        positionsToHit.push(position);
    }

    public int getPoints() {
        return points;
    }
}
