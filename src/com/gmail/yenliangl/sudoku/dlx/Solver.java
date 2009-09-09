package com.gmail.yenliangl.sudoku.dlx;

import java.util.ArrayList;
import java.util.Stack;

import com.gmail.yenliangl.sudoku.puzzle.*;

public class Solver {

    interface Listener {
        void onSolved(Puzzle answer);
        void onUnsolved();
        void onCoverColumn(ColumnNode c);
        void onUncoverColumn(ColumnNode c);
        void onPushRowToSolution(Node r);
        void onPopRowFromSoluton(Node r);
    }

    private Listener mListener;

    public Solver(Listener listener) {
        mListener = listener;
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    /**
     *
     *
     * @param puzzle
     */
    public void solve(Puzzle puzzle) {
        Matrix matrix = new Matrix(puzzle);

        Stack<Node> solution;

        // Extract solved cells in the puzzle and add them into DLX
        // matrix as solved rows;
        int dimension = puzzle.getDimension();
        Iterator<Cell> allCells = puzzle.getCells();
        while(allCells.hasNext()) {
            Cell cell = allCells.next();
            int value = cell.getValue();
            if(value != 0) {    // This is a solved cell.
                int r = cell.getRow(), c = cell.getColumn();
                int rowIndexInMatrix = r * dimension * dimension +
                                       c * dimension + value - 1;
                addRowToSolution(matrix, rowIndexInMatrix, solution);
            }
        }

        solve(matrix, 0, solution); // k = 0
    }

    /**
     *
     *
     * @param matrix
     * @param k
     * @param solution
     */
    private void solve(Matrix matrix, int k, Stack<Node> solution) {
        ColumnNode h = matrix.getRootColumnNode();
        if(h.right == h) {
            generateAnswer(solution);
            return;
        }

        ColumnNode c =
            getColumnNodeWithFewestNodes(matrix.getRootColumnNode());
        coverColumn(c);

        for(Node r = c.down; r != c; r = r.down) {
            solution.push(r);
            mListener.onPushRowToSolution(r);

            for(Node j = r.right; j != r; j = j.right) {
                coverColumn(j);
            }
            solve(k+1, solution);

            r = solution.pop();
            mListener.onPopRowFromSoluton(r);

            c = r.columnNode;
            for(Node j = r.left; j != r; j = j.left) {
                uncoverColumn(j.columnNode);
            }
        }
        uncoverColumn(c);

        mListener.onUnsolved();
    }

    /**
     *
     *
     * @param matrix
     * @param rowIndex
     * @param solution
     */
    private void addRowToSolution(Matrix matrix,
                                  final int rowIndex,
                                  Stack<Node> solution) {
        Node start = matrix.getRowNode(rowIndex);
        for(Node n = start; n != start; n=n.right) {
            coverColumn(n.columnNode);
        }
        solution.push(start);
        mListener.onPushRowToSolution(start);
    }

    /**
     *
     *
     * @param h
     *
     * @return
     */
    private ColumnNode getColumnNodeWithFewestNodes(ColumnNode h) {
        ColumnNode result;
        int s = Integer.MAX_VALUE;
        for(ColumnNode j = h.right; j != h; j = j.right) {
            if(j.getSize() < s) {
                result = j;
                s = j.getSize()
            }
        }
        return result;
    }

    /**
     * Cover the column from column node @p c.
     *
     * @param c Column node.
     */
    private void coverColumn(ColumnNode c) {
        c.right.left = c;
        c.left.right = c.right;
        for(Node i = c.down; i != c; i = i.down ) {
            for(Node j = i.left; j != i; j = j.right ) {
                j.down.up = j.up;
                j.up.down = j.down;
                j.columnNode.decrementSize();
            }
        }
        mListener.onCoverColumn(c);
    }

    /**
     * Uncover the column represented by column node @c c
     *
     * @param c Column node.
     */
    private void uncoverColumn(ColumnNode c) {
        for(Node i = c.up; i !=c; i = i.up) {
            for(Node j = i.left; j != i; j = j.left) {
                j.columnNode.incrementSize();
                j.down.up = j;
                j.up.down = j;
            }
        }
        c.right.left = c;
        c.left.right = c;
        mListener.onUncoverColumn(c);
    }

    /**
     *
     *
     * @param solutionStack
     */
    private void generateAnswer(Stack<Node> solutionStack) {
        StandardPuzzle answer = new StandardPuzzle( 9 /* @todo */ );
        mListener.onSolved(answer);
    }


    /**
     *
     *
     *
     *
     *
     */
    public static void main(String[] args) {
        StandardPuzzle puzzle =
            new StandardPuzzle(
                new int[][] {{4, 0, 0, 3, 0, 1, 0, 8, 0},
                             {6, 0, 1, 0, 2, 0, 0, 0, 0},
                             {0, 3, 9, 0, 7, 0, 0, 0, 0},
                             //------------------------//
                             {9, 2, 0, 0, 0, 0, 0, 4, 0},
                             {0, 0, 0, 7, 0, 5, 0, 0, 0},
                             {0, 5, 0, 4, 0, 0, 0, 6, 3},
                             //------------------------//
                             {0, 0, 0, 0, 1, 0, 2, 3, 0},
                             {0, 0, 0, 0, 6, 0, 9, 0, 8},
                             {0, 1, 0, 9, 0, 8, 0, 0, 5}});
        Matrix matrix = new Matrix(puzzle);
        Solver solver = new Solver(
            new Solver.Listener() {
                @Override
                public void onSolved() {
                }

                @Override
                public void onUnsolved() {
                }


            });

    }
}
