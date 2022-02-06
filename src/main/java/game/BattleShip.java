package game;

import java.util.Objects;

public class BattleShip {
    public final int sizeOfBattleship;
    public final int pointsForHit;


    public BattleShip(int sizeOfBattleship, int pointsForHit) {
        this.sizeOfBattleship = sizeOfBattleship;
        this.pointsForHit = pointsForHit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BattleShip that = (BattleShip) o;
        return sizeOfBattleship == that.sizeOfBattleship && pointsForHit == that.pointsForHit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sizeOfBattleship, pointsForHit);
    }

    @Override
    public String toString() {
        return "BattleShip{" +
                "sizeOfBattleship=" + sizeOfBattleship +
                ", pointsForHit=" + pointsForHit +
                '}';
    }
}
