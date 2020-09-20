package Checkers.BoardComponents;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.Objects;

public class Field extends Rectangle {
    private final int positionX;
    private final int positionY;

    public Field(int positionX, int positionY, boolean color) {
        this.positionX = positionX;
        this.positionY = positionY;

        if (color) {
            setFill(new ImagePattern(new Image("file:src/resources/Checkers/blackF.png")));
        } else {
            setFill(new ImagePattern(new Image("file:src/resources/Checkers/whiteF.png")));
        }
        setMouseTransparent(true);
        setWidth(80);
        setHeight(80);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return positionX == field.positionX &&
                positionY == field.positionY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(positionX, positionY);
    }
}
