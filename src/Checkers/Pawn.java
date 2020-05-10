package Checkers;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class Pawn extends Circle implements GameBase {
    private final boolean isRed;
    private Position position = null;

    public Pawn(Position position, boolean isRed) {
        this.position = position;
        this.isRed = isRed;

        setRadius(fieldSize * 0.4);

        if (isRed) {
            setMouseTransparent(false);
            setFill(new ImagePattern(new Image("file:src/resources/Checkers/redPawn.png")));
        } else {
            setMouseTransparent(true);
            setFill(new ImagePattern(new Image("file:src/resources/Checkers/bluePawn.png")));
        }
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public void setTransparent(boolean value) {
        setMouseTransparent(value);
    }

    public boolean getIsRed() {
        return isRed;
    }

    @Override
    public String toString() {
        return "Pawn{" +
                "isRed=" + isRed +
                ", position=" + position +
                '}';
    }


}