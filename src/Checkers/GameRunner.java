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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import java.awt.*;

import static java.awt.event.InputEvent.BUTTON1_DOWN_MASK;
import static java.lang.Thread.sleep;

public class GameRunner extends Application {
    private Map<Position, Pawn> pawnMap = new HashMap<>();
    private Map<Position, Pawn> loadedPawnMap = new HashMap<>();
    private Round round = new Round();
    private Move move = new Move();

    private File mapS = new File("MapSave");
    private File roundS = new File("RoundSave");
    private File computerS = new File("ComputerSave");

    private ComputerPlayer computerPlayer = null;
    private int tryCounter;

    private Stage window;
    private Scene menuScene;
    private Scene choseColorForPlayerScene;
    private Scene gameScene;


    @Override
    public void start(Stage primaryStage){
        window = primaryStage;
        menuScene = new Scene(getMenu(), 600, 200);

        primaryStage.setTitle("Checkers");
        primaryStage.setScene(menuScene);
        primaryStage.centerOnScreen();
        primaryStage.show();
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

        Button loadButton = new Button("Load");
        HBox hBoxLoadButton = new HBox(10);
        hBoxLoadButton.setAlignment(Pos.CENTER);
        hBoxLoadButton.getChildren().add(loadButton);
        gridMenu.add(hBoxLoadButton, 1, 6);

        onePlayerButton.setOnAction( e -> {
            window.setScene(getChoseColorScene());
        });

        twoPlayersButton.setOnAction( e -> {
            window.setScene(getGameScene());
        });

        loadButton.setOnAction( e -> {
            if (loadGame()) {
                window.setScene(getGameScene());
                round.setPawnMobility(pawnMap);
            }
         });

        return gridMenu;
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
            window.centerOnScreen();
        });

        blueButton.setOnAction( e -> {
            computerPlayer = new ComputerPlayer(true);
            window.setScene(getGameScene());
            window.centerOnScreen();

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

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Field field = new Field(j,i,(i + j) % 2 != 0);
                GridPane.setRowIndex(field, i);
                GridPane.setColumnIndex(field, j);
                grid.getChildren().add(field);
            }
        }

        if (loadedPawnMap.isEmpty()) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if ((i + j) % 2 != 0) {
                        Pawn pawn = null;
                        if (i <= 2) {
                            pawn = createPawn(new Position(j,i), true);
                            pawnMap.put(pawn.getPosition(), pawn);
                        }
                        else if (i >= 5) {
                            pawn = createPawn(new Position(j,i), false);
                            pawnMap.put(pawn.getPosition(), pawn);
                        }
                    }
                }
            }
        } else {
            for (Map.Entry<Position, Pawn> singlePawnSet : loadedPawnMap.entrySet()) {
                Pawn singlePawn = singlePawnSet.getValue();
                Pawn pawn = null;
                pawn = createPawn(singlePawn.getPosition(), singlePawn.getIsRed());
                pawnMap.put(pawn.getPosition(), pawn);
            }
        }

        for (Map.Entry<Position, Pawn> singlePawnSet : pawnMap.entrySet()) {
            Pawn singlePawn = singlePawnSet.getValue();
            GridPane.setRowIndex(singlePawn, singlePawn.getPosition().getY());
            GridPane.setColumnIndex(singlePawn, singlePawn.getPosition().getX());
            GridPane.setValignment(singlePawn, VPos.CENTER);
            GridPane.setHalignment(singlePawn, HPos.CENTER);
            grid.getChildren().add(singlePawn);
            singlePawn.toFront();
        }

        Button saveButton = new Button("Save");
        HBox hBoxSaveButton = new HBox(10);
        hBoxSaveButton.setAlignment(Pos.CENTER);
        hBoxSaveButton.getChildren().add(saveButton);
        hBoxSaveButton.setPadding(new Insets(0, 0, 0, 50));
        grid.add(hBoxSaveButton, 10, 10);

        saveButton.setOnAction( e -> {
            saveGame();
            clearMemory();
            window.setScene(menuScene);
            window.centerOnScreen();
        });

        gameScene = new Scene(grid);
        return gameScene;
    }

    public Pawn createPawn(Position position, boolean color) {
        Pawn pawn = new Pawn(position, color);

        pawn.setOnMousePressed(e -> {
            pawn.setRadius(40);
        });

        pawn.setOnMouseReleased(e -> {
            pawn.setRadius(35);

            Position newPosition = new Position((int) (e.getSceneX() - 120) / 80, (int) (e.getSceneY() - 120) / 80);
            switch (move.moveType(pawnMap, pawn, newPosition)) {
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
                case NONE:
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

            if (round.remainingPawns(pawnMap, true) == 0
                    || move.allPawnsReadyToMove(pawnMap).stream().noneMatch(f -> f.getIsRed())) {
                end("Blue won");
            } else if (round.remainingPawns(pawnMap, false) == 0
                    || move.allPawnsReadyToMove(pawnMap).stream().noneMatch(f -> !f.getIsRed())) {
                end("Red won");
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

    public void changePositionByComputer(ComputerMove computerMove) {
        if (tryCounter < 30) {
            try {
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

                x = (int) (window.getX() + 100 + 80 * (computerMove.getNewPosition().getX() + 1));
                y = (int) (window.getY() + 100 + 80 * (computerMove.getNewPosition().getY() + 1));

                robot.mouseMove(x, y);
                robot.mouseRelease(BUTTON1_DOWN_MASK);
                robot.mouseMove(position.x, position.y);
                sleep(250);
            } catch (NullPointerException | InterruptedException e) {
                System.out.println(e);
            }
            tryCounter++;
        } else {
            tryCounter = 0;
            round.changePlayer();
        }
    }

    public void end(String whoWin) {
        VBox endRoot = new VBox(40);
        endRoot.getChildren().add(new Label(whoWin));
        endRoot.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8);");
        endRoot.setAlignment(Pos.CENTER);
        endRoot.setPadding(new Insets(200));

        Button end = new Button("End");
        endRoot.getChildren().add(end);

        Stage popupEndStage = new Stage(StageStyle.TRANSPARENT);
        popupEndStage.initOwner(window);
        popupEndStage.initModality(Modality.APPLICATION_MODAL);
        popupEndStage.centerOnScreen();
        popupEndStage.setScene(new Scene(endRoot));

        end.setOnAction(event -> {
            clearMemory();
            popupEndStage.close();
            window.setScene(menuScene);
            window.centerOnScreen();
        });

        popupEndStage.show();
    }

    public void saveGame() {

        try {
            ObjectOutputStream mapSave = new ObjectOutputStream (new FileOutputStream(mapS));
            mapSave.writeObject(pawnMap);
            mapSave.close();

            ObjectOutputStream roundSave = new ObjectOutputStream (new FileOutputStream(roundS));
            roundSave.writeObject(round.isRedMove());
            roundSave.close();

            ObjectOutputStream computerSave = new ObjectOutputStream (new FileOutputStream(computerS));
            computerSave.writeObject(computerPlayer);
            computerSave.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public boolean loadGame() {
        try {
            ObjectInputStream mapLoad = new ObjectInputStream(new FileInputStream(mapS));
            Object readMap = null;
            readMap = mapLoad.readObject();
            loadedPawnMap.clear();
            if(readMap instanceof HashMap) {
                loadedPawnMap.putAll((HashMap) readMap);
            } mapLoad.close();

            ObjectInputStream roundLoad = new ObjectInputStream(new FileInputStream(roundS));
            Object readRound = roundLoad.readObject();
            if (!readRound.equals(round.isRedMove())) {
                round.changePlayer();
            } roundLoad.close();

            ObjectInputStream computerLoad = new ObjectInputStream(new FileInputStream(computerS));
            Object readComputer = computerLoad.readObject();

            if (readComputer != null) {
                computerPlayer = new ComputerPlayer(!round.isRedMove());
            }
            computerLoad.close();

        } catch (Exception e) {
            VBox errorRoot = new VBox(40);
            errorRoot.getChildren().add(new Label("Error"));
            errorRoot.setStyle("-fx-background-color: rgba(205,26,26,0.8);");
            errorRoot.setAlignment(Pos.CENTER);
            errorRoot.setPadding(new Insets(200));

            Button errorButton = new Button("Back to menu");
            errorRoot.getChildren().add(errorButton);

            Stage popupErrorStage = new Stage(StageStyle.TRANSPARENT);
            popupErrorStage.initOwner(window);
            popupErrorStage.initModality(Modality.APPLICATION_MODAL);
            popupErrorStage.centerOnScreen();
            popupErrorStage.setScene(new Scene(errorRoot));

            errorButton.setOnAction(event -> {
                pawnMap.clear();
                popupErrorStage.close();
            });
            popupErrorStage.show();
            return false;
        } return true;
    }

    public void clearMemory() {
        tryCounter = 0;
        pawnMap.clear();
        loadedPawnMap.clear();
        computerPlayer = null;
        if (!round.isRedMove()) {
            round.changePlayer();
        }
    }
}
