package game.enums;

public enum PositionType {
    ALREADYHITSHIP,
    ALREADYHITEMPTY,
    EMPTY,
    NOTCHECKED,
    CHECKED;


    @Override
    public String toString() {
        return switch (this) {
            case ALREADYHITSHIP -> "alreadyHitShip";
            case ALREADYHITEMPTY -> "alreadyHitEmpty";
            case EMPTY -> "empty";
            case NOTCHECKED -> "notChecked";
            case CHECKED -> "checked";
        };
    }
}
