package game;

import game.enums.PositionType;

public class BattlefieldPosition {
    public final Vector2d position;
    private int pointsForHitting;
    private PositionType positionType;

    public BattlefieldPosition(Vector2d position, int pointsForHitting, PositionType positionType) {
        this.position = position;
        this.pointsForHitting = pointsForHitting;
        this.positionType = positionType;
    }

    public int getPointsForHitting() {
        return pointsForHitting;
    }

    public void positionWasHit() {
        pointsForHitting = -1;
    }

    @Override
    public String toString() {
        return "BattlefieldPosition{" +
                "position=" + position.toString() +
                ", pointsForHitting=" + pointsForHitting +
                '}';
    }

    public void shipAtPositionWasHit() {
        positionType = PositionType.ALREADYHITSHIP;
    }

    public PositionType getPositionType() {
        return positionType;
    }

    public void switchPositionType(PositionType type) {
        positionType = type;
    }
}
