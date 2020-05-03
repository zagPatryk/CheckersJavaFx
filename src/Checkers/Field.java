package Checkers;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Field extends Rectangle implements GameBase{
    private boolean containPawn = false;


    public Field(int positionX, int positionY, boolean colour) {
        if (colour) {
            setFill(new ImagePattern(new Image("file:src/resources/Checkers/blackF.png")));
        } else {
            setFill(new ImagePattern(new Image("file:src/resources/Checkers/whiteF.png")));
        }
        setWidth(fieldSize);
        setHeight(fieldSize);
        relocate(fieldSize * positionX, fieldSize * positionY);
    }

    public void setContainPawn(boolean containPawn) {
        this.containPawn = containPawn;
    }

    public boolean containPawn() {
        return containPawn;
    }
}
