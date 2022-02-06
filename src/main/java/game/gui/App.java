package game.gui;

import game.*;
import game.enums.BattlefieldOrientation;
import game.enums.PositionType;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    private List<Node> listOfStartingScreenElements = new ArrayList<>();
    private GridPane[] gridPane = {new GridPane(), new GridPane()};
    VBox mainStartingScreenVBox = new VBox();
    private BattleShip currentBattleshipToPut;
    private BattlefieldOrientation currentBattleFieldOrientation = BattlefieldOrientation.VERTICAL;
    private int shipsAlreadyPut = 0;
    private SetUpBattlefieldHelper setUpBattlefieldHelper;
    private PlayerPointCounter playerPointCounter = new PlayerPointCounter();
    private boolean computersTurn = false;
    private VBox[][] computerGridPaneContent;
    private VBox sinkingNotification = new VBox();
    private int sunkenShips = 0;
    private boolean easyStrategy = false;

    private Battlefield playersBattlefield;
    private int maxMoves;
    private int quantityOfIronclads;
    private int pointsPerIroncladHit;
    private int quantityOfBattleCruisers;
    private int pointsPerBattleCruiserHit;
    private int quantityOfDestroyers;
    private int pointsPerDestroyerHit;
    private int maxPoints;
    private ComputerPlayer computerPlayer;

    public void start(Stage primaryStage) {
        TextField mapEdgeSize = createHBoxWithTextField("Map edge size: ", "10");

        TextField maxMoves = createHBoxWithTextField("Total of one player moves: ", "50");
        this.maxMoves = Integer.parseInt(maxMoves.getText());

        TextField quantityOfIronclads = createHBoxWithTextField("Quantity of ironclads: ", "1");
        TextField pointsPerIroncladHit = createHBoxWithTextField("Points per ironclad hit: ", "10");

        TextField quantityOfBattleCruisers = createHBoxWithTextField("Quantity of battle cruisers", "2");
        TextField pointsPerBattleCruiserHit = createHBoxWithTextField("Points per battle cruiser hit: ", "5");

        TextField quantityOfDestroyers = createHBoxWithTextField("Quantity of destroyers", "4");
        TextField pointsPerDestroyerHit = createHBoxWithTextField("Points per destroyer hit: ", "2");

        Button startGameButton = new Button("Set your ships on map!");
        startGameButton.setOnAction((event -> {
            readGameOptions(Integer.parseInt(quantityOfIronclads.getText()), Integer.parseInt(quantityOfBattleCruisers.getText()),
                    Integer.parseInt(quantityOfDestroyers.getText()), Integer.parseInt(pointsPerIroncladHit.getText()),
                    Integer.parseInt(pointsPerBattleCruiserHit.getText()), Integer.parseInt(pointsPerDestroyerHit.getText()));
            setUpBattlefieldHelper = new SetUpBattlefieldHelper(this, this.quantityOfIronclads, this.quantityOfBattleCruisers,
                    this.quantityOfDestroyers, this.pointsPerIroncladHit, this.pointsPerBattleCruiserHit, this.pointsPerDestroyerHit);

            playersBattlefield = new Battlefield(Integer.parseInt(mapEdgeSize.getText()));
            startPuttingShipsOnBattlefield(mainStartingScreenVBox, playersBattlefield);
        }));

        addStartingScreenElementsToVBox(mainStartingScreenVBox);
        mainStartingScreenVBox.getChildren().add(startGameButton);

        Scene scene = new Scene(mainStartingScreenVBox, 600, 700);
        primaryStage.setTitle("Symulation settings");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private TextField createHBoxWithTextField(String textFieldName, String textFieldDefault) {
        Text nameOfTextField = new Text(textFieldName);
        TextField textField = new TextField();
        textField.setText(textFieldDefault);
        HBox hBox = new HBox(20, nameOfTextField, textField);
        hBox.setAlignment(Pos.BASELINE_CENTER);
        listOfStartingScreenElements.add(hBox);
        return textField;
    }

    private void readGameOptions(int quantityOfIronclads, int quantityOfBattleCruisers, int quantityOfDestroyers,
                            int pointsPerIroncladHit, int pointsPerBattleCruiserHit, int pointsPerDestroyerHit) {
        this.quantityOfIronclads = quantityOfIronclads;
        this.pointsPerIroncladHit = pointsPerIroncladHit;
        this.quantityOfBattleCruisers = quantityOfBattleCruisers;
        this.pointsPerBattleCruiserHit = pointsPerBattleCruiserHit;
        this.quantityOfDestroyers = quantityOfDestroyers;
        this.pointsPerDestroyerHit = pointsPerDestroyerHit;
        this.maxPoints = quantityOfIronclads * pointsPerIroncladHit * 5 + quantityOfBattleCruisers * pointsPerBattleCruiserHit * 4
                + quantityOfDestroyers * pointsPerDestroyerHit * 2;
    }

    private void fillComputerGridPaneContent(int size) {
        computerGridPaneContent = new VBox[size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                computerGridPaneContent[x][y] = new VBox();
            }
        }
    }

    private void addStartingScreenElementsToVBox(VBox vBox) {
        for (Node elementOfVBox: listOfStartingScreenElements) {
            vBox.getChildren().add(elementOfVBox);
        }
    }

    private void startPuttingShipsOnBattlefield(VBox mainStartingScreenVBox, Battlefield battlefield) {
        mainStartingScreenVBox.getChildren().clear();
        mainStartingScreenVBox.getChildren().add(gridPane[0]);

        Button changeOrientationButton = new Button("Current orientation of ship is: " + currentBattleFieldOrientation.toString());
        changeOrientationButton.setOnAction(event -> {
            currentBattleFieldOrientation = currentBattleFieldOrientation.changeToOtherOrientation();
            changeOrientationButton.setText("Current orientation of ship is: " + currentBattleFieldOrientation.toString());
        });
        mainStartingScreenVBox.getChildren().add(changeOrientationButton);

        setUpBattlefieldHelper.changeBattleShipIfNeeded(shipsAlreadyPut);
        renderGridpaneForPlacingShips(battlefield);
    }

    private void renderGridpaneForPlacingShips(Battlefield battlefield) {
        gridPane[0].setGridLinesVisible(false);
        gridPane[0].getColumnConstraints().clear();
        gridPane[0].getRowConstraints().clear();
        gridPane[0].getChildren().clear();
        gridPane[0].setGridLinesVisible(true);
        for (int x = 0; x < battlefield.size; x++) {
            for (int y = 0; y < battlefield.size; y++) {
                Vector2d position = new Vector2d(x, y);
                Node gridPaneButton = createButtonForPlacingShips(position, battlefield);
                gridPane[0].add(gridPaneButton, x, y, 1, 1);
            }
        }
    }

    public void renderLastSceneBeforeStart(Battlefield battlefield, VBox mainStartingScreenVBox) {
        mainStartingScreenVBox.getChildren().clear();
        gridPane[0].setGridLinesVisible(false);
        gridPane[0].getColumnConstraints().clear();
        gridPane[0].getRowConstraints().clear();
        gridPane[0].getChildren().clear();
        gridPane[0].setGridLinesVisible(true);

        for (int x = 0; x < battlefield.size; x++) {
            for (int y = 0; y < battlefield.size; y++) {
                Vector2d position = new Vector2d(x, y);
                Node gridPaneButton = createUnresponsiveButton(position, battlefield, true);
                gridPane[0].add(gridPaneButton, x, y, 1, 1);
            }
        }

        mainStartingScreenVBox.getChildren().add(gridPane[0]);

        Button easyButton = new Button("easy mode");
        easyButton.setOnAction(event -> {
            easyStrategy = true;
        });
        mainStartingScreenVBox.getChildren().add(easyButton);

        Button startButton = new Button("Start game");
        startButton.setOnAction(event -> {
            startRealGame(mainStartingScreenVBox);
        });
        mainStartingScreenVBox.getChildren().add(startButton);
    }

    private void startRealGame(VBox mainStartingScreenVBox) {
        Battlefield computerBattlefield = new Battlefield(playersBattlefield.size);
        computerPlayer = new ComputerPlayer(playersBattlefield, easyStrategy);
        computerPlayer.putShipsOnMap(computerBattlefield, quantityOfIronclads, quantityOfBattleCruisers, quantityOfDestroyers,
                pointsPerIroncladHit, pointsPerBattleCruiserHit, pointsPerDestroyerHit);
        fillComputerGridPaneContent(playersBattlefield.size);

        mainStartingScreenVBox.getChildren().clear();
        mainStartingScreenVBox.getChildren().add(gridPane[0]);
        mainStartingScreenVBox.getChildren().add(gridPane[1]);
        mainStartingScreenVBox.getChildren().add(sinkingNotification);
        mainStartingScreenVBox.setSpacing(50);

        createComputerGridPane(computerBattlefield);

        GameEngine gameEngine = new GameEngine(playersBattlefield, this, computerPlayer, maxMoves, maxPoints);
        Thread gameEngineThread = new Thread(gameEngine);
        gameEngineThread.start();

    }

    private void createComputerGridPane(Battlefield battlefield) {
        for (int x = 0; x < battlefield.size; x++) {
            for (int y = 0; y < battlefield.size; y++) {
                Vector2d position = new Vector2d(x, y);
                computerGridPaneContent[x][y].getChildren().add(createButtonForComputerGridPane(position, battlefield));
                gridPane[1].add(computerGridPaneContent[x][y], x, y, 1, 1);
            }
        }
    }

    private Node createUnresponsiveButton(Vector2d position, Battlefield battlefield, boolean showShips) {
        Button button = new Button();
        PositionType positionType = battlefield.getPositionType(position);
        if (positionType == PositionType.ALREADYHITEMPTY) button.setStyle("-fx-background-color: #000000");
        else if (positionType == PositionType.ALREADYHITSHIP) button.setStyle("-fx-background-color: #DC143C");
        else if (showShips && (positionType == PositionType.CHECKED || positionType == PositionType.NOTCHECKED)) {
            button.setStyle("-fx-background-color: #0000FF");
        }
        else button.setStyle("-fx-background-color: #F0FFFF");
        return button;
    }

    private Node createButtonForPlacingShips(Vector2d position, Battlefield battlefield) {
        Button button = new Button();
        int valueOnPosition = battlefield.pointsValueOnPosition(position);
        if (valueOnPosition > 0) {
            button.setStyle("-fx-background-color: #0000FF");
        }
        else button.setStyle("-fx-background-color: #F0FFFF");
        button.setOnAction(event -> {
            if (battlefield.placeShipOnBattlefield(currentBattleshipToPut, shipsAlreadyPut, currentBattleFieldOrientation, position)) {
                shipsAlreadyPut++;
                setUpBattlefieldHelper.changeBattleShipIfNeeded(shipsAlreadyPut);
                if (setUpBattlefieldHelper.lastShipWasPut(shipsAlreadyPut)) {
                    renderLastSceneBeforeStart(battlefield, mainStartingScreenVBox);
                }
                else renderGridpaneForPlacingShips(battlefield);
            }
        });
        return button;
    }

    private Node createButtonForComputerGridPane(Vector2d position, Battlefield battlefield) {
        Button button = new Button();
        PositionType positionType = battlefield.getPositionType(position);
        button.setOnAction(event -> {
            if (!computersTurn) {
                if (positionType == PositionType.NOTCHECKED || positionType == PositionType.CHECKED) {
                    playerPointCounter.addPoints(battlefield.pointsValueOnPosition(position));
                    battlefield.positionGotHit(position);
                    if (battlefield.shipWasSunk(position)) notifyOfSinking();
                }
                computerGridPaneContent[position.xCoordinate][position.yCoordinate].getChildren().clear();
                computerGridPaneContent[position.xCoordinate][position.yCoordinate].getChildren().add(createUnresponsiveButton(position, battlefield, false));
                if (playerPointCounter.getPoints() == maxPoints) {
                    renderEndScreen(computerPlayer.getPoints());
                } else switchTurns();
            }
        });
        return button;
    }

    public void changeCurrentBattleship(BattleShip battleShip) {
        currentBattleshipToPut = battleShip;
    }

    public boolean currentBattleShipEqualsToOther(BattleShip battleShip) {
        return battleShip.equals(currentBattleshipToPut);
    }

    public void renderPlayersMap() {
        gridPane[0].setGridLinesVisible(false);
        gridPane[0].getColumnConstraints().clear();
        gridPane[0].getRowConstraints().clear();
        gridPane[0].getChildren().clear();
        gridPane[0].setGridLinesVisible(true);
        for (int x = 0; x < playersBattlefield.size; x++) {
            for (int y = 0; y < playersBattlefield.size; y++) {
                Vector2d position = new Vector2d(x, y);
                Node gridPaneButton = createUnresponsiveButton(position, playersBattlefield, true);
                gridPane[0].add(gridPaneButton, x, y, 1, 1);
            }
        }
    }

    public boolean getComputerTurn() {
        return computersTurn;
    }

    public void renderEndScreen(int computerPoints) {
        mainStartingScreenVBox.getChildren().clear();
        Text playerScore = new Text("players points: " + String.valueOf(playerPointCounter.getPoints()));
        Text computerScore = new Text("computer points: " + String.valueOf(computerPoints));
        mainStartingScreenVBox.setSpacing(50);
        mainStartingScreenVBox.getChildren().add(playerScore);
        mainStartingScreenVBox.getChildren().add(computerScore);
    }

    public void switchTurns() {
        computersTurn = !computersTurn;
    }

    public void notifyOfSinking() {
        sunkenShips++;
        sinkingNotification.getChildren().clear();
        Text notification = new Text("You have sunk " + String.valueOf(sunkenShips) + " ships!");
        sinkingNotification.getChildren().add(notification);
    }
}
