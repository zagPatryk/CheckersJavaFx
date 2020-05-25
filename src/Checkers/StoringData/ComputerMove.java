package Checkers.StoringData;

import Checkers.BoardComponents.Pawn;

public class ComputerMove {
    private Position newPosition;
    private Pawn pawn;

    public ComputerMove(Pawn pawn, Position newPosition) {
        this.newPosition = newPosition;
        this.pawn = pawn;
    }

    public Position getNewPosition() {
        return newPosition;
    }

    public Pawn getPawn() {
        return pawn;
    }

    public void setPawn(Pawn pawn) {
        this.pawn = pawn;
    }

    @Override
    public String toString() {
        return "ComputerMove{" +
                "newPosition=" + newPosition +
                ", pawn=" + pawn +
                '}';
    }
}
