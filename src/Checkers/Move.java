package Checkers;

import java.util.Map;

import static Checkers.GameBase.MoveType.*;
import static java.lang.Math.abs;

public class Move implements GameBase{

    public MoveType moveType(Map<Position, Pawn> pawnMap, Pawn pawn, Position newPosition) {
        if ((newPosition.getX() < 0 || newPosition.getX() >= boardWidth || newPosition.getY() < 0 || newPosition.getY() >= boardHeight )) {
            return NONE;
        }

        if ((newPosition.getX() + newPosition.getY()) % 2 == 0) {
            return NONE;
        }

        if (pawn.getIsRed()) {
            if (newPosition.getY() < pawn.getPosition().getY()) {
                return NONE;
            }
        } else {
            if (newPosition.getY() > pawn.getPosition().getY()) {
                return NONE;
            }
        }

        if (pawnMap.get(newPosition) != null) {
           return NONE;
        }

        if (pawn.getIsRed()) {
            if (abs(newPosition.getY() - pawn.getPosition().getY()) == 2
                    && abs(newPosition.getX() - pawn.getPosition().getX()) == 2
                    && pawnMap.containsKey(new Position(pawn.getPosition().getX() + ((newPosition.getX() - pawn.getPosition().getX()) / 2),
                    newPosition.getY() - 1))) {
                if (!pawnMap.get(new Position(pawn.getPosition().getX() + ((newPosition.getX() - pawn.getPosition().getX()) / 2),
                        newPosition.getY() - 1)).getIsRed()) {
                    return KILL;
                } else {
                    return NONE;
                }
            }
        } else
            if (abs(newPosition.getY() - pawn.getPosition().getY()) == 2
                    && abs(newPosition.getX() - pawn.getPosition().getX()) == 2
                    && pawnMap.containsKey(new Position(pawn.getPosition().getX() + ((newPosition.getX() - pawn.getPosition().getX()) / 2),
                    (newPosition.getY() + 1)))) {
                if (pawnMap.get(new Position(pawn.getPosition().getX() + ((newPosition.getX() - pawn.getPosition().getX()) / 2),
                        (newPosition.getY() + 1))).getIsRed()) {
                    return KILL;
                } else {
                    return NONE;
                }
        }

        if (abs(newPosition.getY()- pawn.getPosition().getY()) != 1
                || newPosition.getX() == pawn.getPosition().getX()
                || abs(newPosition.getX()- pawn.getPosition().getX()) != 1) {
            return NONE;
        }

        return MOVE;
    }
}
