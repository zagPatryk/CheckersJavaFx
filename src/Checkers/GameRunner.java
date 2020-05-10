package Checkers;

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

import static Checkers.GameBase.*;

public class GameRunner extends Application {
    private Map<Position, Pawn> pawnMap = new HashMap<>();
    private Round round = new Round();
    private Move moveType = new Move();

    Stage window;
    Scene menuScene;
    Scene choseColorForPlayerScene;
    Scene gameScene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;

        primaryStage.setTitle("Checkers");
        primaryStage.setScene(new Scene(getMenu(), 600, 200));
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

        onePlayerButton.setOnAction( e -> {
            // dodać ustaw tryb
            window.setScene(getChoseColorScene());
        });

        twoPlayersButton.setOnAction( e -> {
            // dodać ustaw tryb
            window.setScene(getGameScene());
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
            // dodać ustaw kolor gracza
            window.setScene(getGameScene());
        });

        blueButton.setOnAction( e -> {
            // dodać ustaw kolor gracza
            window.setScene(getGameScene());
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
                    else if (i >= boardHeight-3) {
                        pawn = createPawn(new Position(j,i), false);
                    }

                    if (pawn != null) {
                        pawnMap.put(pawn.getPosition(), pawn);
                        GridPane.setRowIndex(pawn, i);
                        GridPane.setColumnIndex(pawn, j);
                        GridPane.setValignment(pawn, VPos.CENTER);
                        GridPane.setHalignment(pawn, HPos.CENTER);
                        grid.getChildren().add(pawn);
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
// dodać kill do tyłu
// dodać tworzenie kingów na ostatnim rzędzie
        pawn.setOnMouseReleased(e -> {
            pawn.setRadius(fieldSize * 0.4);
            Position newPosition = new Position((int) (e.getSceneX() - 120) / 80, (int) (e.getSceneY() - 120) / 80);

            switch (moveType.moveType(pawnMap, pawn, newPosition)) {
                case MOVE:
                    round.changePlayer();
                    pawnMap.remove(pawn.getPosition());
                    pawnMap.put(newPosition, pawn);
                    pawn.setPosition(newPosition);
                    GridPane.setColumnIndex(pawn, newPosition.getX());
                    GridPane.setRowIndex(pawn, newPosition.getY());
                    round.setPawnMobility(pawnMap);
                    break;
                case KILL:
                    if (pawn.getIsRed()) {
                        pawnMap.get(new Position(pawn.getPosition().getX() + ((newPosition.getX() - pawn.getPosition().getX()) / 2),
                                newPosition.getY() - 1)).setRadius(0);
                        pawnMap.remove(new Position(pawn.getPosition().getX() + ((newPosition.getX() - pawn.getPosition().getX()) / 2),
                                newPosition.getY() - 1));
                    } else {
                        pawnMap.get(new Position(pawn.getPosition().getX() + ((newPosition.getX() - pawn.getPosition().getX()) / 2),
                                (newPosition.getY() + 1))).setRadius(0);
                        pawnMap.remove(new Position(pawn.getPosition().getX() + ((newPosition.getX() - pawn.getPosition().getX()) / 2),
                                (newPosition.getY() + 1)));
                    }

                    pawnMap.remove(pawn.getPosition());
                    pawnMap.put(newPosition, pawn);
                    pawn.setPosition(newPosition);
                    GridPane.setColumnIndex(pawn, newPosition.getX());
                    GridPane.setRowIndex(pawn, newPosition.getY());

                    if (pawn.getIsRed()) {
                        if (pawnMap.containsKey(new Position(pawn.getPosition().getX() - 1, pawn.getPosition().getY() + 1))
                                && !pawnMap.containsKey(new Position(pawn.getPosition().getX() - 2, pawn.getPosition().getY() + 2))
                                && (pawn.getPosition().getX() - 2) >= 0 && (pawn.getPosition().getY() + 2)<= 7) {
                            if (pawn.getIsRed() == pawnMap.get(new Position(pawn.getPosition().getX() - 1, pawn.getPosition().getY() + 1)).getIsRed()) {
                                round.changePlayer();
                                round.setPawnMobility(pawnMap);
                            } else {
                                round.setPawnMobilityAfterKill(pawnMap, pawn);
                            }
                        } else if (pawnMap.containsKey(new Position(pawn.getPosition().getX() + 1, pawn.getPosition().getY() + 1))
                                && !pawnMap.containsKey(new Position(pawn.getPosition().getX() + 2, pawn.getPosition().getY() + 2))
                                && pawn.getPosition().getX() + 2 <= 7 && pawn.getPosition().getY() + 2 <= 7) {
                            if (pawn.getIsRed() == pawnMap.get(new Position(pawn.getPosition().getX() + 1, pawn.getPosition().getY() + 1)).getIsRed()) {
                                round.changePlayer();
                                round.setPawnMobility(pawnMap);
                            } else {
                                round.setPawnMobilityAfterKill(pawnMap, pawn);
                            }
                        } else {
                            round.changePlayer();
                            round.setPawnMobility(pawnMap);
                        }
                    } else {
                        if (pawnMap.containsKey(new Position(pawn.getPosition().getX() - 1, pawn.getPosition().getY() - 1))
                                && !pawnMap.containsKey(new Position(pawn.getPosition().getX() - 2, pawn.getPosition().getY() - 2))
                                && (pawn.getPosition().getX() - 2) >= 0 && (pawn.getPosition().getY() - 2) >= 0) {
                            if (pawn.getIsRed() == pawnMap.get(new Position(pawn.getPosition().getX() - 1, pawn.getPosition().getY() - 1)).getIsRed()) {
                                round.changePlayer();
                                round.setPawnMobility(pawnMap);
                            } else {
                                round.setPawnMobilityAfterKill(pawnMap, pawn);
                            }
                        } else if (pawnMap.containsKey(new Position(pawn.getPosition().getX() + 1, pawn.getPosition().getY() - 1))
                                && !pawnMap.containsKey(new Position(pawn.getPosition().getX() + 2, pawn.getPosition().getY() - 2))
                                && pawn.getPosition().getX() + 2 <= 7 && pawn.getPosition().getY() - 2 >= 0) {
                            if (pawn.getIsRed() == pawnMap.get(new Position(pawn.getPosition().getX() + 1, pawn.getPosition().getY() - 1)).getIsRed()) {
                                round.changePlayer();
                                round.setPawnMobility(pawnMap);
                            } else {
                                round.setPawnMobilityAfterKill(pawnMap, pawn);
                            }
                        } else {
                            round.changePlayer();
                            round.setPawnMobility(pawnMap);
                        }
                    }
                    break;
                default:
                    break;
            }

            if (round.remainingPawns(pawnMap, true) == 0) {
                System.out.println("Blue Won");
                window.setScene(menuScene);
            } else if (round.remainingPawns(pawnMap, false) == 0) {
                System.out.println("Red Won");
                window.setScene(menuScene);
            }
        });
        return pawn;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
