/**
 *  JSoko - A Java implementation of the game of Sokoban
 *  Copyright (c) 2012 by Matthias Meger, Germany
 *
 *  This file is part of JSoko.
 *
 *	JSoko is free software; you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation; either version 2 of the License, or
 *	(at your option) any later version.
 *
 *	This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.sokoban_online.jsoko.solver;

import java.util.Comparator;
import java.util.PriorityQueue;

import de.sokoban_online.jsoko.JSoko;
import de.sokoban_online.jsoko.boardpositions.IBoardPosition;
import de.sokoban_online.jsoko.boardpositions.IBoardPositionMoves;
import de.sokoban_online.jsoko.boardpositions.RelativeBoardPositionMoves;
import de.sokoban_online.jsoko.pushesLowerBoundCalculation.LowerBoundCalculation;
import de.sokoban_online.jsoko.resourceHandling.Texts;
import de.sokoban_online.jsoko.utilities.Debug;
import de.sokoban_online.jsoko.utilities.Utilities;


/**
 * Solver which solves a level move optimally with minimal pushes.
 */
public class SolverAStarMovesPushes extends SolverIDAStarPushesMoves {

    // Comparator for this solver: first moves, second pushes.
    private final Comparator<IBoardPositionMoves> MOVES_PUSHES_COMPARATOR = (o1, o2) -> {

        int o1MoveCount = o1.getTotalMovesCount();
        int o2MoveCount = o2.getTotalMovesCount();

        if(o1MoveCount != o2MoveCount) {
            return o1MoveCount < o2MoveCount ? -1 : 1;
        }

        int o1PushCount = o1.getPushesCount();
        int o2PushCount = o2.getPushesCount();

        if(o1PushCount != o2PushCount) {
            return o1PushCount < o2PushCount ? -1 : 1;
        }

        // Moves and pushes are equal -> order doesn't matter.
        // Note: we break the equals contract here, but equal
        // board positions regarding the positions will never be added
        // since this is avoided by using a transposition table.
        return 1;
    };

    private final PriorityQueue<IBoardPositionMoves> openQueue = new PriorityQueue<>(100000, MOVES_PUSHES_COMPARATOR);

    private int moveCountBestSolution = Integer.MAX_VALUE;
    private int pushCountBestSolution = Integer.MAX_VALUE;

    private int searchDepth = 0;

    /**
     * Number of pushes of all board positions generated by pushing a box
     * in the board position taken from the open queue.
     * -1 => not yet calculated
     */
    private final int NOT_CALCULATED_YET = -1;
    private int pushCountCurrentBoardPosition = NOT_CALCULATED_YET;

    private IBoardPositionMoves currentBoardPosition;


    /**
     * Creates a solver for solving a level moves optimal with best pushes.
     *
     * @param application  Reference to the main object
     * @param solverGUI reference to the GUI of this solver
     */
    public SolverAStarMovesPushes(JSoko application, SolverGUI solverGUI) {
        super(application, solverGUI);
    }

    /**
     * Tries to solve the level by generating all possible no-deadlock board positions and
     * returns the solution path via a global variable.
     */
    protected final void forwardSearch() {

        // Number of moves and pushes of the currently best known solution.
        moveCountBestSolution  = Integer.MAX_VALUE;
        pushCountBestSolution = Integer.MAX_VALUE;

        searchDepth = 0;
        boardPositionsCount = 0;

        openQueue.add(getBestBoardPosition());  // add the initial board position we get from the super class

        // The board position with the lowest estimated solution path length is analyzed further next.
        while((currentBoardPosition = openQueue.poll()) != null && !isCancelled()) {

            pushCountCurrentBoardPosition = NOT_CALCULATED_YET;

            board.setBoardPosition(currentBoardPosition);

            // Determine the reachable squares of the player. These squares are used even after
            // the deadlock detection, hence they are calculated in an extra object.
            playersReachableSquaresMoves.update();

            int lastPushedBoxNo = currentBoardPosition.getBoxNo();

            // If the box is in a tunnel only pushes of this box have to be considered!
            if(lastPushedBoxNo != NO_BOX_PUSHED && isBoxInATunnel(lastPushedBoxNo, currentBoardPosition.getDirection())) {
                generateSuccessorsByPushingBox(lastPushedBoxNo);
            } else {
                for(int boxNo=0; boxNo < board.boxCount; boxNo++) {
                    generateSuccessorsByPushingBox(boxNo);
                }
            }
        }
    }

    private void generateSuccessorsByPushingBox(int boxNo) {

        int boxPosition = board.boxData.getBoxPosition(boxNo);

        // Push the box to every direction possible.
        for(int direction = 0; direction < 4; direction++) {

            int newBoxPosition           = boxPosition + offset[direction];
            int playerPositionToPushFrom = boxPosition - offset[direction];

            // Immediately continue with the next direction if the player can't reach the correct
            // position for pushing or the new box position isn't accessible.
            if(!playersReachableSquaresMoves.isSquareReachable(playerPositionToPushFrom)
               || !board.isAccessibleBox(newBoxPosition)) {
                continue;
            }

            // Do push.
            board.pushBox(boxPosition, newBoxPosition);
            board.playerPosition = boxPosition;

            int moveCountNewBoardPosition = (short) (currentBoardPosition.getTotalMovesCount() + playersReachableSquaresMoves.getDistance(playerPositionToPushFrom) + 1);

            // Immediately continue with the next direction if the the board position isn't
            // reached better than the current best solution.
            if(isWorseOrEqualThanCurrentBestSolution(moveCountNewBoardPosition, getPushCountNewBoardPosition())) {
                board.pushBoxUndo(newBoxPosition, boxPosition);
                continue;
            }

            IBoardPositionMoves newBoardPosition = new RelativeBoardPositionMoves(board, boxNo, direction, currentBoardPosition);

            newBoardPosition.setMovesCount(moveCountNewBoardPosition);

            IBoardPosition oldBoardPosition = positionStorage.getBoardPosition(newBoardPosition);

            // If the board position had already been saved in the hash table it must be checked
            // for being better than the one in the hash table (it may have already been reached
            // by the corral detection hence there has to be a check for a SearchBoardPosition!)
            if(oldBoardPosition instanceof IBoardPositionMoves) {

                IBoardPositionMoves oldBoardPositionWithMoves = (IBoardPositionMoves) oldBoardPosition;

                int moveCountOldBoardPosition  = oldBoardPositionWithMoves.getTotalMovesCount();

                // If the current board position has been reached better than the one in the
                // hash table it has to be saved / used instead of the old one.
                if(moveCountNewBoardPosition < moveCountOldBoardPosition ||
                   moveCountNewBoardPosition == moveCountOldBoardPosition &&
                   getPushCountNewBoardPosition() < oldBoardPositionWithMoves.getPushesCount()) {
                    positionStorage.storeBoardPosition(newBoardPosition);
                    openQueue.add(newBoardPosition);
                }

                // Undo push and continue with next direction.
                board.pushBoxUndo(newBoxPosition, boxPosition);
                continue;
            }


            /*
             * The board position hasn't already been in the hash table, hence it is a new one.
             */

            int currentBoardPositionLowerBound = lowerBoundCalcuation.calculatePushesLowerBound(newBoxPosition);

            // Undo push (the player is new positioned for the next board position anyway)
            board.pushBoxUndo(newBoxPosition, boxPosition);

            // Immediately continue with the next direction if the current board position
            // can't be a part of a new best solution.
            if(currentBoardPositionLowerBound == LowerBoundCalculation.DEADLOCK ||
               isWorseOrEqualThanCurrentBestSolution(moveCountNewBoardPosition + currentBoardPositionLowerBound,
                                                     getPushCountNewBoardPosition() + currentBoardPositionLowerBound)) {
                continue;
            }

            boardPositionsCount++;  // number of unique no deadlock board positions reached during the search

            if(currentBoardPositionLowerBound == 0) {
                setNewBestSolution(newBoardPosition, moveCountNewBoardPosition, getPushCountNewBoardPosition());
            } else {
                informUserAboutProgress();

                positionStorage.storeBoardPosition(newBoardPosition);
                openQueue.add(newBoardPosition);
            }
        }
    }

    private boolean isWorseOrEqualThanCurrentBestSolution(int moveCount, int pushCount) {
        return moveCount > moveCountBestSolution ||
               moveCount == moveCountBestSolution && pushCount >= pushCountBestSolution;
    }

    /** Set the passed board position as new best solution. */
    private void setNewBestSolution(IBoardPositionMoves boardPosition, int moveCount, int pushCount) {
        moveCountBestSolution = moveCount;
        pushCountBestSolution = pushCount;
        solutionBoardPosition = boardPosition;
        if(Debug.isDebugModeActivated) {
            System.out.println("Solution Found "+"Moves/Pushes: "+boardPosition.getTotalMovesCount()+"/"+boardPosition.getPushesCount());
        }
    }

    /**
     * Display info about the search (every 5000 board positions and
     * every time the search depths has been increased).
     */
    private void informUserAboutProgress() {

        if(boardPositionsCount%5000 == 0 || currentBoardPosition.getTotalMovesCount() > searchDepth) {
            searchDepth = currentBoardPosition.getTotalMovesCount();

            publish(Texts.getText("numberofpositions") + boardPositionsCount
                    + ", " +
                    Texts.getText("searchdepth")       + searchDepth + " "+Texts.getText("moves"));

            // Throw "out of memory" if less than 15MB RAM is free.
            if(Utilities.getMaxUsableRAMinMiB() <= 15) {
                isSolverStoppedDueToOutOfMemory = true;
                cancel(true);
            }
        }
    }

    /**
     * For a better performance the push count is only
     * calculated if needed and then cashed.
     */
    private int getPushCountNewBoardPosition() {

        if(pushCountCurrentBoardPosition == NOT_CALCULATED_YET) {
            pushCountCurrentBoardPosition = currentBoardPosition.getPushesCount() + 1;
        }

        return pushCountCurrentBoardPosition;
    }
}