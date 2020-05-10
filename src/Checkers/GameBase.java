package Checkers;

public interface GameBase {
    int fieldSize = 80;
    int boardWidth = 8;
    int boardHeight = 8;

    enum MoveType {
        NONE, MOVE, KILL
    }
}
