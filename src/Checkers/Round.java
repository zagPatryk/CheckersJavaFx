package Checkers;

import java.util.Map;

public class Round {
    boolean isRedMove = true;

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
}
