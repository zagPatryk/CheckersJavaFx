package Checkers;

import Checkers.BoardComponents.Pawn;
import Checkers.StoringData.Position;

import java.io.Serializable;
import java.util.Map;

import static java.lang.Math.abs;

public class Round implements Serializable {
    private static final long serialVersionUID = 99292;
    private boolean isRedMove = true;

    public boolean isRedMove() {
        return isRedMove;
    }

    public void changePlayer() {
        isRedMove = !isRedMove;
    }

    public void setPawnMobility(Map<Position, Pawn> pawnMap) {
        for (Map.Entry<Position, Pawn> singlePawn : pawnMap.entrySet()) {
            if (singlePawn.getValue().getIsRed()) {
                singlePawn.getValue().setTransparent(!isRedMove);
            } else {
                singlePawn.getValue().setTransparent(isRedMove);
            }
        }
    }

    public void setPawnMobilityAfterKill(Map<Position, Pawn> pawnMap, Pawn pawn) {
        for (Map.Entry<Position, Pawn> singlePawn : pawnMap.entrySet()) {
            if (!singlePawn.getValue().equals(pawn)) {
                singlePawn.getValue().setTransparent(true);
            }
        }
    }

    public int remainingPawns(Map<Position, Pawn> pawnMap, Boolean isPawnRed) {
        int remainingPawns = 0;
        for (Map.Entry<Position, Pawn> singlePawn : pawnMap.entrySet()) {
            if (singlePawn.getValue().getIsRed() == isPawnRed) {
                remainingPawns++;
            }
        }
        return remainingPawns;
    }

    public void killPawn(Map<Position, Pawn> pawnMap, Pawn pawn, Position newPosition) {
        int deltaX= 0;
        int deltaY = 0;
        int loop = abs(pawn.getPosition().getX() - newPosition.getX()) - 1;
        for (int i = 0; i < loop; i++) {
            if (pawn.getPosition().getX() - newPosition.getX() > 0) {
                deltaX--;
            } else if (pawn.getPosition().getX() - newPosition.getX() < 0) {
                deltaX++;
            }
            if (pawn.getPosition().getY() - newPosition.getY() > 0) {
                deltaY--;
            } else if (pawn.getPosition().getY() - newPosition.getY() < 0) {
                deltaY++;
            }
            if (pawnMap.containsKey(new Position(pawn.getPosition().getX() + deltaX,
                    pawn.getPosition().getY() + deltaY))) {
                if (pawnMap.get(new Position(pawn.getPosition().getX() + deltaX,
                        pawn.getPosition().getY() + deltaY)).getIsRed() != pawn.getIsRed()) {
                    pawnMap.get(new Position(pawn.getPosition().getX() + deltaX,
                            pawn.getPosition().getY() + deltaY)).setRadius(0);
                    pawnMap.remove(new Position(pawn.getPosition().getX() + deltaX,
                            pawn.getPosition().getY() + deltaY));
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Round{" +
                "isRedMove=" + isRedMove +
                '}';
    }
}
