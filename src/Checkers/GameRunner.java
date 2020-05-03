package Checkers;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import static Checkers.GameBase.*;


public class GameRunner extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
//        Pane pane = new Pane();
//        pane.setPrefSize(fieldSize * boardWidth, fieldSize * boardHeight);

        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(new Image("file:src/resources/Checkers/table.jpg"), BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);

//        Group group = new Group();
//        for (int i = 0; i < boardHeight; i++) {
//            for (int j = 0; j < boardWidth; j++) {
//                group.getChildren().add(new Field(i,j,(i+j) % 2 != 0));
//            }
//        }

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(120, 120, 120, 120));
        grid.setBackground(background);

        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                Field field = new Field(i,j,(i + j) % 2 != 0);
                GridPane.setRowIndex(field, i);
                GridPane.setColumnIndex(field, j);
                grid.getChildren().addAll(field);

                if ((i + j) % 2 != 0) {
                    Pawn pawn = null;
                    if (i <= 2) {
                        pawn = new Pawn(i, j, true);

                    } else if (i >= 5) {
                        pawn = new Pawn(i, j, false);
                    }
                    if (pawn != null) {
                        field.setContainPawn(true);
                        GridPane.setRowIndex(pawn, i);
                        GridPane.setColumnIndex(pawn, j);
                        GridPane.setValignment(pawn, VPos.CENTER);
                        GridPane.setHalignment(pawn, HPos.CENTER);
                        grid.getChildren().add(pawn);
                    }
                }
            }
        }

//        GridPane.setRowIndex(r1, 2);
//        GridPane.setColumnIndex(r1, 0);
//        pane.getChildren().addAll(group);

        Scene scene = new Scene(grid, 1200, 900, Color.BLACK);
//        Scene scene = new Scene(pane,new ImagePattern(new Image("file:src/resources/Checkers/board.jpg")));
//        Scene scene = new Scene(pane);

        primaryStage.setTitle("Checkers");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
