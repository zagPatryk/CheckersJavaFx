package Checkers;

import Checkers.BoardComponents.Pawn;
import Checkers.StoringData.ComputerMove;
import Checkers.StoringData.Position;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class ComputerPlayer  implements Serializable {
    private static final long serialVersionUID = 99293;
    private Move move = new Move();
    private Random random = new Random();

    private boolean isRedPlayer;

    public ComputerPlayer(boolean isRedPlayer) {
        this.isRedPlayer = isRedPlayer;
    }

    public boolean isRedPlayer() {
        return isRedPlayer;
    }

    public ComputerMove decideComputerNewPosition(Map<Position, Pawn> pawnMap) {
        Position newPosition = null;
        Pawn pawn = null;

        List<Pawn> allPawnsReadyToKillList = move.allPawnsReadyToKill(pawnMap);
        List<Pawn> computerPawnsReadyToKillList = allPawnsReadyToKillList.stream()
                .filter(e -> e.getIsRed() == isRedPlayer).collect(Collectors.toList());;

        List<Pawn> allPawnsReadyToMoveList = move.allPawnsReadyToMove(pawnMap);
        List<Pawn> computerPawnsReadyToMoveList = allPawnsReadyToMoveList.stream()
                .filter(e -> e.getIsRed() == isRedPlayer).collect(Collectors.toList());

        if (!computerPawnsReadyToKillList.isEmpty()) {
            pawn = computerPawnsReadyToKillList.get(random.nextInt(computerPawnsReadyToKillList.size()));

            if (pawn.isSuperPawn()) {
                int delta = 1;
                int mobilityLoop = 1;

                if (pawn.isSuperPawn()) {
                    mobilityLoop = 8;
                }

                boolean dropFirstLoop = false;
                boolean dropSecLoop = false;
                boolean dropThLoop = false;
                boolean dropFtLoop = false;

                for (int i = 0; i < mobilityLoop; i++) {
                    switch (move.pawnPossibleKillMoves(pawnMap, pawn).get(pawn)) {
                        case DOWN_LEFT:
                            if (!dropFirstLoop && pawnMap.containsKey(new Position(pawn.getPosition().getX() - delta, pawn.getPosition().getY() + delta))) {
                                if (!pawnMap.containsKey(new Position(pawn.getPosition().getX() - (delta + 1), pawn.getPosition().getY() + (delta + 1)))
                                        && (pawn.getPosition().getX() - (delta + 1)) >= 0 && (pawn.getPosition().getY() + (delta + 1))<= 7) {
                                    if (pawn.getIsRed() == pawnMap.get(new Position(pawn.getPosition().getX() - delta,
                                            pawn.getPosition().getY() + delta)).getIsRed()) {
                                        dropFirstLoop = true;
                                    } else {
                                        dropFirstLoop = true;
                                        newPosition = new Position(pawn.getPosition().getX() - (delta + 1), pawn.getPosition().getY() + (delta + 1));
                                    }
                                } else {
                                    dropFirstLoop = true;
                                }
                            }
                            break;
                        case DOWN_RIGHT:
                            if (!dropSecLoop && pawnMap.containsKey(new Position(pawn.getPosition().getX() + delta, pawn.getPosition().getY() + delta))) {
                                if (!pawnMap.containsKey(new Position(pawn.getPosition().getX() + (delta + 1), pawn.getPosition().getY() + (delta + 1)))
                                        && (pawn.getPosition().getX() + (delta + 1)) <= 7 && (pawn.getPosition().getY() + (delta + 1)) <= 7) {
                                    if (pawn.getIsRed() == pawnMap.get(new Position(pawn.getPosition().getX() + delta, pawn.getPosition().getY() + delta)).getIsRed()) {
                                        dropSecLoop = true;
                                    } else {
                                        dropSecLoop = true;
                                        newPosition = new Position(pawn.getPosition().getX() + (delta + 1), pawn.getPosition().getY() + (delta + 1));
                                    }
                                } else {
                                    dropSecLoop = true;
                                }
                            }

                            break;
                        case UP_LEFT:
                            if (!dropThLoop && pawnMap.containsKey(new Position(pawn.getPosition().getX() - delta, pawn.getPosition().getY() - delta))) {
                                if (!pawnMap.containsKey(new Position(pawn.getPosition().getX() - (delta + 1), pawn.getPosition().getY() - (delta + 1)))
                                        && (pawn.getPosition().getX() - (delta + 1)) >= 0 && (pawn.getPosition().getY() - (delta + 1)) >= 0) {
                                    if (pawn.getIsRed() == pawnMap.get(new Position(pawn.getPosition().getX() - delta, pawn.getPosition().getY() - delta)).getIsRed()) {
                                        dropThLoop = true;
                                    } else {
                                        dropThLoop = true;
                                        newPosition = new Position(pawn.getPosition().getX() - (delta + 1), pawn.getPosition().getY() - (delta + 1));
                                    }
                                } else {
                                    dropThLoop = true;
                                }
                            }

                            break;
                        case UP_RIGHT:
                            if (!dropFtLoop && pawnMap.containsKey(new Position(pawn.getPosition().getX() + delta, pawn.getPosition().getY() - delta))) {
                                if (!pawnMap.containsKey(new Position(pawn.getPosition().getX() + (delta + 1), pawn.getPosition().getY() - (delta + 1)))
                                        && (pawn.getPosition().getX() + (delta + 1)) <= 7 && (pawn.getPosition().getY() - (delta + 1)) >= 0) {
                                    if (pawn.getIsRed() == pawnMap.get(new Position(pawn.getPosition().getX() + delta, pawn.getPosition().getY() - delta)).getIsRed()) {
                                        dropFtLoop = true;
                                    } else {
                                        dropFtLoop = true;
                                        newPosition = new Position(pawn.getPosition().getX() + (delta + 1), pawn.getPosition().getY() - (delta + 1));
                                    }
                                } else {
                                    dropFtLoop = true;
                                }
                            }
                            break;
                        default:
                            break;
                    } delta++;
                }
            } else {
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
        } else if (!computerPawnsReadyToMoveList.isEmpty()) {
            pawn = computerPawnsReadyToMoveList.get(random.nextInt(computerPawnsReadyToMoveList.size()));

            if (pawn.isSuperPawn()) {
                int delta = 1;
                int mobilityLoop = 1;

                if (pawn.isSuperPawn()) {
                    mobilityLoop = 8;
                }

                boolean dropFirstLoop = false;
                boolean dropSecLoop = false;
                boolean dropThLoop = false;
                boolean dropFtLoop = false;

                for (int i = 0; i < mobilityLoop; i++) {
                    switch (move.pawnPossibleMoves(pawnMap, pawn).get(pawn)) {
                        case UP_LEFT:
                            if (!dropFirstLoop && !pawnMap.containsKey(new Position(pawn.getPosition().getX() - delta, pawn.getPosition().getY() - delta))
                                    && (pawn.getPosition().getX() - delta) >= 0 && (pawn.getPosition().getY() - delta) >= 0) {
                                newPosition = new Position(pawn.getPosition().getX() - delta, pawn.getPosition().getY() - delta);
                                if (delta == random.nextInt(8)) {
                                    dropFirstLoop = true;
                                }
                            } else {
                                dropFirstLoop = true;
                            }
                            break;
                        case UP_RIGHT:
                            if (!dropSecLoop && !pawnMap.containsKey(new Position(pawn.getPosition().getX() + delta, pawn.getPosition().getY() - delta))
                                    && (pawn.getPosition().getX() + delta ) <= 7 && (pawn.getPosition().getY() - delta) >= 0) {
                                newPosition = new Position(pawn.getPosition().getX() + delta, pawn.getPosition().getY() - delta);
                                if (delta == random.nextInt(8)) {
                                    dropSecLoop = true;
                                }
                            } else {
                                dropSecLoop = true;
                            }
                            break;
                        case DOWN_LEFT:
                            if (!dropThLoop && !pawnMap.containsKey(new Position(pawn.getPosition().getX() - delta, pawn.getPosition().getY() + delta))
                                    && (pawn.getPosition().getX() - delta) >= 0 && (pawn.getPosition().getY() + delta) <= 7) {
                                newPosition = new Position(pawn.getPosition().getX() - delta, pawn.getPosition().getY() + delta);
                                if (delta == random.nextInt(8)) {
                                    dropThLoop = true;
                                }
                            } else {
                                dropThLoop = true;
                            }
                            break;
                        case DOWN_RIGHT:
                            if (!dropFtLoop && !pawnMap.containsKey(new Position(pawn.getPosition().getX() + delta, pawn.getPosition().getY() + delta))
                                    && (pawn.getPosition().getX() + delta) <= 7 && (pawn.getPosition().getY() + delta) <= 7) {
                                newPosition = new Position(pawn.getPosition().getX() + delta, pawn.getPosition().getY() + delta);
                                if (delta == random.nextInt(8)) {
                                    dropFtLoop = true;
                                }
                            } else {
                                dropFtLoop = true;
                            }
                            break;
                        default:
                            break;
                    }
                    delta++;
                }
            } else {
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

    @Override
    public String toString() {
        return "ComputerPlayer{"+ isRedPlayer +
                '}';
    }
}
