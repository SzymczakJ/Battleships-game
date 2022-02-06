package game.enums;

import java.util.Random;

public enum BattlefieldOrientation {
    HORIZONTAL,
    VERTICAL;

    public BattlefieldOrientation changeToOtherOrientation() {
        if (this == HORIZONTAL) return VERTICAL;
        else return HORIZONTAL;
    }

    public BattlefieldOrientation randomOrientation() {
        Random random = new Random();
        int x = random.nextInt(2);
        if (x == 0) return HORIZONTAL;
        else return VERTICAL;
    }

    @Override
    public String toString() {
        if (this == HORIZONTAL) return "horizontal";
        else  return "vertical";
    }
}
