package Checkers;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class Pawn extends Circle implements GameBase{
    private int positionX;
    private int positionY;

//    private double moveX;
//    private double moveY;

    public Pawn(int x, int y, boolean color) {
        positionX = x;
        positionY = y;

        setRadius(fieldSize * 0.4);
        if (color) {
            setFill(new ImagePattern(new Image("file:src/resources/Checkers/redPawn.png")));
        } else {
            setFill(new ImagePattern(new Image("file:src/resources/Checkers/bluePawn.png")));
        }

        relocate(fieldSize * positionX, fieldSize * positionY);

//        setOnMousePressed(e -> {
//            moveX = e.getSceneX();
//            moveY = e.getSceneY();
//        });
//
//        setOnMouseDragged(E -> {
//            relocate(moveX, moveY);
//        });
    }
}
