package Checkers.BoardComponents;

import Checkers.StoringData.Position;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.Serializable;

public class Pawn extends Circle implements Serializable {
    private final boolean isRed;
    private Position position;
    private boolean superPawn = false;
    private boolean nextMoveKill = false;

    public Pawn(Position position, boolean isRed) {
        this.position = position;
        this.isRed = isRed;

        setRadius(35);

        if (isRed) {
            setMouseTransparent(false);
            setFill(new ImagePattern(new Image("file:src/resources/Checkers/redPawn.png")));
        } else {
            setMouseTransparent(true);
            setFill(new ImagePattern(new Image("file:src/resources/Checkers/bluePawn.png")));
        }
    }

    public void setPosition(Position newPosition) {
        this.position = newPosition;
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

    public void setSuperPawn(boolean superPawn) {
        this.superPawn = superPawn;
        if (isRed) {
            setMouseTransparent(false);
            setFill(new ImagePattern(new Image("file:src/resources/Checkers/redSuperPawn.png")));
        } else {
            setMouseTransparent(true);
            setFill(new ImagePattern(new Image("file:src/resources/Checkers/blueSuperPawn.png")));
        }
    }

    public boolean isSuperPawn() {
        return superPawn;
    }

    public boolean isNextMoveKill() {
        return nextMoveKill;
    }

    public void setNextMoveKill(boolean nextMoveKill) {
        this.nextMoveKill = nextMoveKill;
    }

    @Override
    public String toString() {
        return "Pawn{" +
                "isRed=" + isRed +
                ", position=" + position +
                ", superPawn=" + superPawn +
                '}';
    }
}