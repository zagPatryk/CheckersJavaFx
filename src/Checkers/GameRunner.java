package Checkers;

import Checkers.BoardComponents.Field;
import Checkers.BoardComponents.Pawn;
import Checkers.StoringData.ComputerMove;
import Checkers.StoringData.Position;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;



import java.util.HashMap;
import java.util.Map;

import java.awt.*;
import static java.awt.event.InputEvent.BUTTON1_DOWN_MASK;

import static java.lang.Thread.sleep;

public class GameRunner extends Application {
    private Map<Position, Pawn> pawnMap = new HashMap<>();
    private Round round = new Round();
    private Move move = new Move();
    private ComputerPlayer computerPlayer = null;

    private int fieldSize = 80;
    private int boardWidth = 8;
    private int boardHeight = 8;

    private Stage window;
    private Scene menuScene; // zobaczyć czego szare
    private Scene choseColorForPlayerScene;
    private Scene gameScene;



    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;

        primaryStage.setTitle("Checkers");
        primaryStage.setScene(new Scene(getMenu(), 600, 200));
        primaryStage.show();

//        while (true) {
//            changePositionByComputer(pawnMap,);
//        }
    }

    public Parent getMenu() {
        GridPane gridMenu = new GridPane();
        gridMenu.setAlignment(Pos.CENTER);
        gridMenu.setHgap(10);
        gridMenu.setVgap(10);
        gridMenu.setPadding(new Insets(25, 25, 25, 25));

        Button onePlayerButton = new Button("One player mode");
        HBox hBoxOnePlayerButton = new HBox(10);
        hBoxOnePlayerButton.setAlignment(Pos.CENTER);
        hBoxOnePlayerButton.getChildren().add(onePlayerButton);
        gridMenu.add(hBoxOnePlayerButton, 1, 4);

        Button twoPlayersButton = new Button("Two players mode");
        HBox hBoxTwoPlayersButton = new HBox(10);
        hBoxTwoPlayersButton.setAlignment(Pos.CENTER);
        hBoxTwoPlayersButton.getChildren().add(twoPlayersButton);
        gridMenu.add(hBoxTwoPlayersButton, 1, 5);

        onePlayerButton.setOnAction( e -> {
            window.setScene(getChoseColorScene());
        });

        twoPlayersButton.setOnAction( e -> {
            window.setScene(getGameScene());
        });

        return gridMenu; // zamienić w klase
    }

    public Scene getChoseColorScene() {
        GridPane gridChoseColor = new GridPane();
        gridChoseColor.setAlignment(Pos.CENTER);
        gridChoseColor.setHgap(10);
        gridChoseColor.setVgap(10);
        gridChoseColor.setPadding(new Insets(25, 25, 25, 25));

        Label choseColorLabel = new Label("Chose pawn color");
        choseColorLabel.setFont(new Font("Arial", 24));
        gridChoseColor.add(choseColorLabel, 2, 2);

        Button redButton = new Button("Red");
        HBox hBoxRedButton = new HBox(10);
        hBoxRedButton.setAlignment(Pos.CENTER);
        hBoxRedButton.getChildren().add(redButton);
        gridChoseColor.add(hBoxRedButton, 1, 4);

        Button blueButton = new Button("Blue");
        HBox hBoxBlueButton = new HBox(10);
        hBoxBlueButton.setAlignment(Pos.CENTER);
        hBoxBlueButton.getChildren().add(blueButton);
        gridChoseColor.add(hBoxBlueButton, 3, 4);

        redButton.setOnAction( e -> {
            computerPlayer = new ComputerPlayer(false);
            window.setScene(getGameScene());
        });

        blueButton.setOnAction( e -> {
            computerPlayer = new ComputerPlayer(true);
            window.setScene(getGameScene());

            round.changePlayer();
            ComputerMove computerMove = computerPlayer.decideComputerNewPosition(pawnMap);
            changePositionByComputer(computerMove);
            round.changePlayer();

        });
        choseColorForPlayerScene = new Scene(gridChoseColor, 400, 200);
        return choseColorForPlayerScene;
    }

    public Scene getGameScene() {
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(new Image("file:src/resources/Checkers/table.jpg"), BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setPadding(new Insets(120, 120, 120, 120));
        grid.setBackground(background);

        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                Field field = new Field(j,i,(i + j) % 2 != 0);
                GridPane.setRowIndex(field, i);
                GridPane.setColumnIndex(field, j);
                grid.getChildren().add(field);

                if ((i + j) % 2 != 0) {
                    Pawn pawn = null;
                    if (i <= 2) {
                        pawn = createPawn(new Position(j,i), true);
                    }
                    else if (i >= boardHeight - 3) {
                        pawn = createPawn(new Position(j,i), false);
                    }

                    if (pawn != null) {
                        pawnMap.put(pawn.getPosition(), pawn);
                        GridPane.setRowIndex(pawn, i);
                        GridPane.setColumnIndex(pawn, j);
                        GridPane.setValignment(pawn, VPos.CENTER);
                        GridPane.setHalignment(pawn, HPos.CENTER);
                        grid.getChildren().add(pawn);
                        pawn.toFront();
                    }
                }
            }
        }
        gameScene = new Scene(grid);
        return gameScene;
    }

    public Pawn createPawn(Position position, boolean color) {
        Pawn pawn = new Pawn(position, color);

        pawn.setOnMousePressed(e -> {
            pawn.setRadius(fieldSize * 0.5);
        });

        pawn.setOnMouseReleased(e -> {
            pawn.setRadius(fieldSize * 0.4);
            boolean whichPlayerMove = round.isRedMove();
            Position newPosition = new Position((int) (e.getSceneX() - 120) / 80, (int) (e.getSceneY() - 120) / 80);
            switch (move.moveType(pawnMap, pawn, newPosition)) {
                case NONE:
                    break;
                case MOVE:
                    round.changePlayer();
                    changePositionOfPawn(pawn, newPosition);
                    round.setPawnMobility(pawnMap);
                    break;
                case KILL:
                    round.killPawn(pawnMap, pawn, newPosition);
                    changePositionOfPawn(pawn, newPosition);
                    pawn.setNextMoveKill(false);

                    if (move.pawnPossibleKillMoves(pawnMap, pawn).containsKey(pawn)) {
                        pawn.setNextMoveKill(true);
                        round.setPawnMobilityAfterKill(pawnMap, pawn);
                    } else {
                        round.changePlayer();
                        round.setPawnMobility(pawnMap);
                    }
                    break;
                default:
                    break;
            }

            if (pawn.getIsRed()) {
                if (pawn.getPosition().getY() == 7) {
                    pawn.setSuperPawn(true);
                }
            } else {
                if (pawn.getPosition().getY() == 0) {
                    pawn.setSuperPawn(true);
                }
            }

// nie przenosi do menu. Scena zatrzymana z powiększonym pionkiem i bez killa
            if (round.remainingPawns(pawnMap, true) == 0) {
                System.out.println("Blue Won");
                window.setScene(menuScene);  // to nie działa jak powinno
            } else if (round.remainingPawns(pawnMap, false) == 0) {
                System.out.println("Red Won");
                window.setScene(menuScene);
            }

            if (computerPlayer != null) {
                if (computerPlayer.isRedPlayer() == round.isRedMove()) {
                    tryCounter = 0;
                    ComputerMove computerMove = computerPlayer.decideComputerNewPosition(pawnMap);
                    changePositionByComputer(computerMove);
                }
            }

        });
        return pawn;
    }

    public void changePositionOfPawn(Pawn pawn, Position newPosition) {
        pawnMap.remove(pawn.getPosition());
        pawnMap.put(newPosition, pawn);
        pawn.setPosition(newPosition);
        GridPane.setColumnIndex(pawn, newPosition.getX());
        GridPane.setRowIndex(pawn, newPosition.getY());
        pawn.toFront();
    }

    public static void main(String[] args) {
        launch(args);
    }

    int tryCounter;

    public void changePositionByComputer(ComputerMove computerMove) {
        if (tryCounter < 10) {
            try {
                System.out.println(computerMove.getPawn());
                System.out.println("===================");
                int x = (int) (window.getX() + 100 + 80 * (computerMove.getPawn().getPosition().getX() + 1));
                int y = (int) (window.getY() + 100 + 80 * (computerMove.getPawn().getPosition().getY() + 1));
                Robot robot = null;
                Point position = MouseInfo.getPointerInfo().getLocation();

                try {
                    robot = new Robot();
                } catch (AWTException e) {
                    e.printStackTrace();
                }
                robot.mouseMove(x, y);
                robot.mousePress(BUTTON1_DOWN_MASK);
                robot.mouseMove(position.x, position.y);

                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                x = (int) (window.getX() + 100 + 80 * (computerMove.getNewPosition().getX() + 1));
                y = (int) (window.getY() + 100 + 80 * (computerMove.getNewPosition().getY() + 1));
                robot.mouseMove(x, y);
                robot.mouseRelease(BUTTON1_DOWN_MASK);
                robot.mouseMove(position.x, position.y);
            } catch (NullPointerException e) {
                System.out.println(e);
            }
            tryCounter++;
        }
    }
}
