package Checkers;

import Checkers.BoardComponents.Pawn;
import Checkers.StoringData.ComputerMove;
import Checkers.StoringData.Position;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class ComputerPlayer {
    private Move move = new Move();
    private Random random = new Random();

    boolean isRedPlayer = true;

    public boolean isRedPlayer() {
        return isRedPlayer;
    }

    public ComputerPlayer(boolean isRedPlayer) {
        this.isRedPlayer = isRedPlayer;
    }

    public ComputerMove decideComputerNewPosition(Map<Position, Pawn> pawnMap) {
        Position newPosition = null;
        Pawn pawn = null;
        List<Pawn> computerPawnsReadyToKillList = null;
        List<Pawn> computerPawnsReadyToMoveList = null;

        if (!move.allPawnsReadyToKill(pawnMap).isEmpty()) {
            List<Pawn> allPawnsReadyToKillList = move.allPawnsReadyToKill(pawnMap);

            computerPawnsReadyToKillList = allPawnsReadyToKillList.stream()
                    .filter(e -> e.getIsRed() == isRedPlayer).collect(Collectors.toList());

            if (!computerPawnsReadyToKillList.isEmpty()) {

                pawn = computerPawnsReadyToKillList.get(random.nextInt(computerPawnsReadyToKillList.size()));

                switch (move.pawnPossibleKillMoves(pawnMap, pawn).get(pawn)) {
                    case UP_LEFT:
                        newPosition = new Position(pawn.getPosition().getX() - 2, pawn.getPosition().getY() - 2);
                        break;
                    case UP_RIGHT:
                        newPosition = new Position(pawn.getPosition().getX() + 2, pawn.getPosition().getY() - 2);
                        break;
                    case DOWN_LEFT:
                        newPosition = new Position(pawn.getPosition().getX() - 2, pawn.getPosition().getY() + 2);
                        break;
                    case DOWN_RIGHT:
                        newPosition = new Position(pawn.getPosition().getX() + 2, pawn.getPosition().getY() + 2);
                        break;
                    default:
                        break;
                }
            }
        } else if(!move.allPawnsReadyToMove(pawnMap).isEmpty()) {
            List<Pawn> allPawnsReadyToMoveList = move.allPawnsReadyToMove(pawnMap);
            computerPawnsReadyToMoveList =  allPawnsReadyToMoveList.stream()
                    .filter(e -> e.getIsRed() == isRedPlayer).collect(Collectors.toList());

            if (!computerPawnsReadyToMoveList.isEmpty()) {
                pawn = computerPawnsReadyToMoveList.get(random.nextInt(computerPawnsReadyToMoveList.size() - 1));

                switch (move.pawnPossibleMoves(pawnMap, pawn).get(pawn)) {
                    case UP_LEFT:
                        newPosition = new Position(pawn.getPosition().getX() - 1, pawn.getPosition().getY() - 1);
                        break;
                    case UP_RIGHT:
                        newPosition = new Position(pawn.getPosition().getX() + 1, pawn.getPosition().getY() - 1);
                        break;
                    case DOWN_LEFT:
                        newPosition = new Position(pawn.getPosition().getX() - 1, pawn.getPosition().getY() + 1);
                        break;
                    case DOWN_RIGHT:
                        newPosition = new Position(pawn.getPosition().getX() + 1, pawn.getPosition().getY() + 1);
                        break;
                    default:
                        break;
                }
            }
        } else {
            System.out.println("Hmmm");
        }

        return new ComputerMove(pawn, newPosition);
    }
}
