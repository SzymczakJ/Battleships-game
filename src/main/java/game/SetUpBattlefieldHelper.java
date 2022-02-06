package game;

import game.gui.App;

public class SetUpBattlefieldHelper {
    private App applicationToBeHelped;
    private int quantityOfIronclads;
    private int quantityOfBattleCruisers;
    private int quantityOfDestroyers;
    private BattleShip ironclad;
    private BattleShip batttleCruiser;
    private BattleShip destroyer;

    public SetUpBattlefieldHelper(App app, int quantityOfIronclads, int quantityOfBattleCruisers, int quantityOfDestroyers,
                                  int pointsPerIroncladHit, int pointsPerBattleCruiserHit, int pointsPerDestroyerHit) {
        this.applicationToBeHelped = app;
        this.quantityOfIronclads = quantityOfIronclads;
        this.quantityOfBattleCruisers = quantityOfBattleCruisers;
        this.quantityOfDestroyers = quantityOfDestroyers;
        ironclad = new BattleShip(5, pointsPerIroncladHit);
        batttleCruiser = new BattleShip(4, pointsPerBattleCruiserHit);
        destroyer = new BattleShip(2, pointsPerDestroyerHit);
    }

    public void changeBattleShipIfNeeded(int currentShipCounter) {
        BattleShip newBattleShip;
        if (currentShipCounter < quantityOfIronclads) {
            newBattleShip = ironclad;
        }
        else if (currentShipCounter < quantityOfIronclads + quantityOfBattleCruisers) {
            newBattleShip = batttleCruiser;
        }
        else newBattleShip = destroyer;

        if (!applicationToBeHelped.currentBattleShipEqualsToOther(newBattleShip)) {
            applicationToBeHelped.changeCurrentBattleship(newBattleShip);
        }
    }

    public boolean lastShipWasPut(int curentShipCounter) {
        return quantityOfIronclads + quantityOfBattleCruisers + quantityOfDestroyers <= curentShipCounter;
    }
}
