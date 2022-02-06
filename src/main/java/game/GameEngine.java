package game;

import game.gui.App;
import javafx.application.Platform;

public class GameEngine implements Runnable{
    private final Battlefield battlefield;
    private final App appRunningEngine;
    private final ComputerPlayer computerPlayer;
    private int moveCounter = 0;
    private final int maxMoves;
    private final int maxPoints;

    public GameEngine(Battlefield battlefield, App appRunningEngine, ComputerPlayer computerPlayer, int maxMoves, int maxPoints) {
        this.battlefield = battlefield;
        this.appRunningEngine = appRunningEngine;
        this.computerPlayer = computerPlayer;
        this.maxMoves = maxMoves;
        this.maxPoints = maxPoints;
    }


    @Override
    public void run() {
        Platform.runLater(() -> {
            appRunningEngine.renderPlayersMap();
        });
        while (moveCounter < maxMoves && maxPoints > computerPlayer.getPoints()) {
            while (!appRunningEngine.getComputerTurn()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("some fault in game engine");
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("some fault in game engine");
            }
            moveCounter++;
            computerPlayer.hitPosition();
            Platform.runLater(() -> {
                appRunningEngine.renderPlayersMap();
            });
            appRunningEngine.switchTurns();
        }
        Platform.runLater(() -> {
            appRunningEngine.renderEndScreen(computerPlayer.getPoints());
        });
    }

}
