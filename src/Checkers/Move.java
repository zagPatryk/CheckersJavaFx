package Checkers;

import Checkers.BoardComponents.Pawn;
import Checkers.StoringData.Direction;
import Checkers.StoringData.MoveType;
import Checkers.StoringData.Position;

import java.util.*;

import static Checkers.StoringData.MoveType.*;
import static java.lang.Math.abs;

public class Move{
    private final int boardWidth = 8;
    private final int boardHeight = 8;

    public MoveType moveType(Map<Position, Pawn> pawnMap, Pawn pawn, Position newPosition) {
        if ((newPosition.getX() < 0 || newPosition.getX() >= boardWidth || newPosition.getY() < 0 || newPosition.getY() >= boardHeight )) {
            return NONE;
        }

        if ((newPosition.getX() + newPosition.getY()) % 2 == 0) {
            return NONE;
        }

        if (pawnMap.get(newPosition) != null) {
            return NONE;
        }

        if (abs(newPosition.getY() - pawn.getPosition().getY()) != abs(newPosition.getX()- pawn.getPosition().getX())
                || newPosition.getX() == pawn.getPosition().getX()) {
            return NONE;
        }

        if (pawn.isSuperPawn()) {
            if (abs(newPosition.getY() - pawn.getPosition().getY()) == 2
                    && abs(newPosition.getX() - pawn.getPosition().getX()) == 2
                    && pawnMap.containsKey(new Position(pawn.getPosition().getX() + ((newPosition.getX() - pawn.getPosition().getX()) / 2),
                    pawn.getPosition().getY() + ((newPosition.getY() - pawn.getPosition().getY()) / 2)))) {
                if (pawnMap.get(new Position(pawn.getPosition().getX() + ((newPosition.getX() - pawn.getPosition().getX()) / 2),
                        pawn.getPosition().getY() + ((newPosition.getY() - pawn.getPosition().getY()) / 2))).getIsRed() != pawn.getIsRed()) {
                    return KILL;
                } else if (pawnMap.get(new Position(pawn.getPosition().getX() + ((newPosition.getX() - pawn.getPosition().getX()) / 2),
                        pawn.getPosition().getY() + ((newPosition.getY() - pawn.getPosition().getY()) / 2))).getIsRed() == pawn.getIsRed()) {
                    return NONE;
                }
            }

            int deltaX = 0;
            int deltaY = 0;

            int loop = abs(pawn.getPosition().getX() - newPosition.getX()) - 1;
            int enemyPawn = 0;

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
                if (abs(newPosition.getY() - pawn.getPosition().getY()) == abs(newPosition.getX() - pawn.getPosition().getX())
                        && pawnMap.containsKey(new Position(pawn.getPosition().getX() + deltaX,
                        pawn.getPosition().getY() + deltaY))) {
                    if (pawnMap.get(new Position(pawn.getPosition().getX() + deltaX,
                            pawn.getPosition().getY() + deltaY)).getIsRed() != pawn.getIsRed()) {
                        enemyPawn++;
                    } else if (pawnMap.get(new Position(pawn.getPosition().getX() + deltaX,
                            pawn.getPosition().getY() + deltaY)).getIsRed() == pawn.getIsRed()) {
                        return NONE;
                    }
               }
            }

            if (enemyPawn == 1) {
               return KILL;
            } else if (enemyPawn > 1) {
               return NONE;
            } else {
                if (pawn.isNextMoveKill()) {
                    return NONE;
                } else {
                    return MOVE;
                }
            }
        }

        if (abs(newPosition.getY() - pawn.getPosition().getY()) == 2
                && abs(newPosition.getX() - pawn.getPosition().getX()) == 2
                && pawnMap.containsKey(new Position(pawn.getPosition().getX() + ((newPosition.getX() - pawn.getPosition().getX()) / 2),
                pawn.getPosition().getY() + ((newPosition.getY() - pawn.getPosition().getY()) / 2)))) {
            if (pawnMap.get(new Position(pawn.getPosition().getX() + ((newPosition.getX() - pawn.getPosition().getX()) / 2),
                    pawn.getPosition().getY() + ((newPosition.getY() - pawn.getPosition().getY()) / 2))).getIsRed() != pawn.getIsRed()) {
                return KILL;
            } else if (pawnMap.get(new Position(pawn.getPosition().getX() + ((newPosition.getX() - pawn.getPosition().getX()) / 2),
                    pawn.getPosition().getY() + ((newPosition.getY() - pawn.getPosition().getY()) / 2))).getIsRed() == pawn.getIsRed()) {
                return NONE;
            }
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

        if (abs(newPosition.getY()- pawn.getPosition().getY()) != 1
                || newPosition.getX() == pawn.getPosition().getX()
                || abs(newPosition.getX()- pawn.getPosition().getX()) != 1) {
            return NONE;
        }

        if (pawn.isNextMoveKill()) {
            return NONE;
        }

        return MOVE;
    }

    public Map<Pawn, Direction> pawnPossibleKillMoves(Map<Position, Pawn> pawnMap, Pawn pawn) {
        Map<Pawn, Direction> pawnPossibleKillMoves = new HashMap<>();

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
            if (!dropFirstLoop && pawnMap.containsKey(new Position(pawn.getPosition().getX() - delta, pawn.getPosition().getY() + delta))
                    && !pawnMap.containsKey(new Position(pawn.getPosition().getX() - (delta + 1), pawn.getPosition().getY() + (delta + 1)))
                    && (pawn.getPosition().getX() - (delta + 1)) >= 0 && (pawn.getPosition().getY() + (delta + 1))<= 7) {
                if (pawn.getIsRed() == pawnMap.get(new Position(pawn.getPosition().getX() - delta,
                        pawn.getPosition().getY() + delta)).getIsRed()) {
                    dropFirstLoop = true;
                } else {
                    dropFirstLoop = true;
                    pawnPossibleKillMoves.put(pawn, Direction.DOWN_LEFT);
                }
            }

            if (!dropSecLoop && pawnMap.containsKey(new Position(pawn.getPosition().getX() + delta, pawn.getPosition().getY() + delta))
                    && !pawnMap.containsKey(new Position(pawn.getPosition().getX() + (delta + 1), pawn.getPosition().getY() + (delta + 1)))
                    && (pawn.getPosition().getX() + (delta + 1)) <= 7 && (pawn.getPosition().getY() + (delta + 1)) <= 7) {
                if (pawn.getIsRed() == pawnMap.get(new Position(pawn.getPosition().getX() + delta, pawn.getPosition().getY() + delta)).getIsRed()) {
                    dropSecLoop = true;
                } else {
                    dropSecLoop = true;
                    pawnPossibleKillMoves.put(pawn, Direction.DOWN_RIGHT);
                }
            }

            if (!dropThLoop && pawnMap.containsKey(new Position(pawn.getPosition().getX() - delta, pawn.getPosition().getY() - delta))
                    && !pawnMap.containsKey(new Position(pawn.getPosition().getX() - (delta + 1), pawn.getPosition().getY() - (delta + 1)))
                    && (pawn.getPosition().getX() - (delta + 1)) >= 0 && (pawn.getPosition().getY() - (delta + 1)) >= 0) {
                if (pawn.getIsRed() == pawnMap.get(new Position(pawn.getPosition().getX() - delta, pawn.getPosition().getY() - delta)).getIsRed()) {
                    dropThLoop = true;
                } else {
                    dropThLoop = true;
                    pawnPossibleKillMoves.put(pawn, Direction.UP_LEFT);
                }
            }

            if (!dropFtLoop && pawnMap.containsKey(new Position(pawn.getPosition().getX() + delta, pawn.getPosition().getY() - delta))
                    && !pawnMap.containsKey(new Position(pawn.getPosition().getX() + (delta + 1), pawn.getPosition().getY() - (delta + 1)))
                    && (pawn.getPosition().getX() + (delta + 1)) <= 7 && (pawn.getPosition().getY() - (delta + 1)) >= 0) {
                if (pawn.getIsRed() == pawnMap.get(new Position(pawn.getPosition().getX() + delta, pawn.getPosition().getY() - delta)).getIsRed()) {
                    dropFtLoop = true;
                } else {
                    dropFtLoop = true;
                    pawnPossibleKillMoves.put(pawn, Direction.UP_RIGHT);
                }
            }
            delta++;
        }
        return pawnPossibleKillMoves;
    }

    public Map<Pawn, Direction> pawnPossibleMoves(Map<Position, Pawn> pawnMap, Pawn pawn) {
        Map<Pawn, Direction> pawnPossibleMoves = new HashMap<>();

        if (!pawnMap.containsKey(new Position(pawn.getPosition().getX() - 1, pawn.getPosition().getY() + 1))
                && (pawn.getPosition().getX() - 1) >= 0 && (pawn.getPosition().getY() + 1) <= 7) {
            pawnPossibleMoves.put(pawn, Direction.DOWN_LEFT);
        }

        if (!pawnMap.containsKey(new Position(pawn.getPosition().getX() + 1, pawn.getPosition().getY() + 1))
                && (pawn.getPosition().getX() + 1) <= 7 && (pawn.getPosition().getY() + 1) <= 7) {
            pawnPossibleMoves.put(pawn, Direction.DOWN_RIGHT);
        }

        if (!pawnMap.containsKey(new Position(pawn.getPosition().getX() - 1, pawn.getPosition().getY() - 1))
                && (pawn.getPosition().getX() - 1) >= 0 && (pawn.getPosition().getY() - 1) >= 0) {
            pawnPossibleMoves.put(pawn, Direction.UP_LEFT);
        }

        if (!pawnMap.containsKey(new Position(pawn.getPosition().getX() + 1, pawn.getPosition().getY() - 1))
                && (pawn.getPosition().getX() + 1 ) <= 7 && (pawn.getPosition().getY() - 1) >= 0) {
            pawnPossibleMoves.put(pawn, Direction.UP_RIGHT);
        }

        return pawnPossibleMoves;
    }

    public List<Pawn> allPawnsReadyToKill(Map<Position, Pawn> pawnMap) {
        List<Pawn> allPawnsReadyToKill = new ArrayList<>();
        for (Map.Entry<Position, Pawn> singlePawnPossibleMoves : pawnMap.entrySet()) {
            if (!pawnPossibleKillMoves(pawnMap, singlePawnPossibleMoves.getValue()).isEmpty()) {
                allPawnsReadyToKill.add(singlePawnPossibleMoves.getValue());
            }
        }
        return allPawnsReadyToKill;
    }

    public List<Pawn> allPawnsReadyToMove(Map<Position, Pawn> pawnMap) {
        List<Pawn> allPawnsReadyToMove = new ArrayList<>();
        for (Map.Entry<Position, Pawn> singlePawnPossibleMoves : pawnMap.entrySet()) {
            if (!pawnPossibleMoves(pawnMap, singlePawnPossibleMoves.getValue()).isEmpty()) {
                allPawnsReadyToMove.add(singlePawnPossibleMoves.getValue());
            }
        }
        return allPawnsReadyToMove;
    }


}





