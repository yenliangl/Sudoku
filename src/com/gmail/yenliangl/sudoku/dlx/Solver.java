package com.gmail.yenliangl.sudoku.dlx;

import java.util.ArrayList;
import java.util.Stack;

import com.gmail.yenliangl.sudoku.puzzle.*;

public abstract class Solver {
    // public enum Result { FAILED, SUCCESSFUL };

    public Solver() {}

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

    protected abstract void onSolved(Puzzle answer);
    protected abstract void onUnsolved();
    protected abstract void onCoverColumn(ColumnNode c);
    protected abstract void onUncoverColumn(ColumnNode c);
    protected abstract void onPushRowToSolution(Node r);
    protected abstract void onPopRowFromSoluton(Node r);

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
            onPushRowToSolution(r);

            for(Node j = r.right; j != r; j = j.right) {
                coverColumn(j);
            }
            solve(k+1, solution);

            r = solution.pop();
            onPopRowFromSoluton(r);

            c = r.columnNode;
            for(Node j = r.left; j != r; j = j.left) {
                uncoverColumn(j.columnNode);
            }
        }
        uncoverColumn(c);

        onUnsolved();
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
        for(Node j = h.right; j != h; j = j.right) {
            if(j.length < s) {
                result = j;
                s = j.length;
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
                j.columnNode.length--;
            }
        }
        onCoverColumn(c);
    }

    /**
     * Uncover the column represented by column node @c c
     *
     * @param c Column node.
     */
    private void uncoverColumn(ColumnNode c) {
        for(Node i = c.up; i !=c; i = i.up) {
            for(Node j = i.left; j != i; j = j.left) {
                j.columnNode.length++;
                j.down.up = j;
                j.up.down = j;
            }
        }
        c.right.left = c;
        c.left.right = c;
        onUncoverColumn(c);
    }

    /**
     *
     *
     * @param solutionStack
     */
    private void generateAnswer(Stack<Node> solutionStack) {
        StandardPuzzle answer = new StandardPuzzle( 9 /* @todo */ );
        onSolved(answer);
    }


    /**
     *
     *
     *
     *
     *
     */
    public static void main(String[] args) {



    }
}
