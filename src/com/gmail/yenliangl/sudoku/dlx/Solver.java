package com.gmail.yenliangl.sudoku.dlx;

import java.util.ArrayList;
import java.util.Stack;
import java.util.Iterator;

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
        if(listener != null) {
            mListener = listener;
        }
    }

    public void solve(Puzzle puzzle, Puzzle answer) {
        Matrix matrix = new Matrix(puzzle);

        int dimension = puzzle.getDimension();
        Stack<Node> solution = new Stack<Node>();
        solution.ensureCapacity(dimension * dimension);

        // Get solved cells in the puzzle and add them into DLX
        // matrix as solved rows
        Iterator<Cell> allCells = puzzle.getCells();
        while(allCells.hasNext()) {
            Cell cell = allCells.next();
            int value = cell.getValue();
            if(value != 0) {    // This is a solved cell.
                int rowIndex = cell.getRowIndex();
                int columnIndex = cell.getColumnIndex();
                int rowNodeIndex =
                    matrix.calculateRowNodeIndex(rowIndex,
                                                 columnIndex,
                                                 value);
                addRowNodeToSolution(matrix, rowNodeIndex, solution);
            }
        }

        if(solve(matrix, 0, solution)) {
            generateAnswer(solution, answer);
        } else {
            mListener.onUnsolved();
        }
    }

    private boolean solve(Matrix matrix, int k, Stack<Node> solution) {
        ColumnNode h = matrix.getRootColumnNode();
        if(h.right == h) {
            // TODO: Found a solution. Prints the solution and goes on
            // to find another possible solution?
            return true;
        }

        ColumnNode c = getColumnNodeWithFewestNodes(matrix.getRootColumnNode());
        coverColumn(c);

        for(Node r = c.down; r != c; r = r.down) {
            solution.push(r);
            mListener.onPushRowToSolution(r);

            for(Node j = r.right; j != r; j = j.right) {
                coverColumn(j.columnNode);
            }

            if(solve(matrix, k+1, solution)) {
                return true;
            }

            solve(matrix, k+1, solution);

            r = solution.pop();
            mListener.onPopRowFromSoluton(r);

            c = r.columnNode;
            for(Node j = r.left; j != r; j = j.left) {
                uncoverColumn(j.columnNode);
            }
        }
        uncoverColumn(c);

        return false;
    }

    private void addRowNodeToSolution(Matrix matrix,
                                      final int rowNodeIndex,
                                      Stack<Node> solution) {
        Node start = matrix.getRowNode(rowNodeIndex);
        Node node = start;
        do {
            coverColumn(node.columnNode);
            node = node.right;
        } while(node != start);
        solution.push(start);
        mListener.onPushRowToSolution(start);
    }

    private ColumnNode getColumnNodeWithFewestNodes(ColumnNode h) {
        ColumnNode result = (ColumnNode)h.right;
        int s = Integer.MAX_VALUE;
        for(ColumnNode j = (ColumnNode)h.right;
            j != h; j = (ColumnNode)j.right) {
            if(j.getSize() < s) {
                result = j;
                s = j.getSize();
            }
        }
        return result;
    }

    private void coverColumn(ColumnNode c) {
        c.right.left = c.left;
        c.left.right = c.right;
        for(Node i = c.down; i != c; i = i.down ) {
            for(Node j = i.right; j != i; j = j.right ) {
                j.down.up = j.up;
                j.up.down = j.down;
                j.columnNode.decrementSize();
            }
        }
        mListener.onCoverColumn(c);
    }

    private void uncoverColumn(ColumnNode c) {
        for(Node i = c.up; i != c; i = i.up) {
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

    private void generateAnswer(Stack<Node> solutionStack,
                                Puzzle answer) {
        Iterator<Node> nodes = solutionStack.iterator();
        while(nodes.hasNext()) {
            Node node = nodes.next();
            Cell cell = answer.getCell(node.rowIndex,
                                       node.columnIndex);
            cell.setValue(node.value);
        }
        mListener.onSolved(answer);
    }

    public static void main(String[] args) {
        Solver solver = new Solver(
            new Solver.Listener() {
                @Override
                public void onSolved(Puzzle answer) {
                    System.out.println(answer);
                    System.out.println("!!!Puzzle solved!!!!");
                }

                @Override
                public void onUnsolved() {
                    System.out.println("!!!Puzzle unsolved!!!!");
                }

                public void onCoverColumn(ColumnNode c) {}
                public void onUncoverColumn(ColumnNode c) {}
                public void onPushRowToSolution(Node r) {}
                public void onPopRowFromSoluton(Node r) {}
            });

        // Generate a random sudoku.
        StandardPuzzle answer = new StandardPuzzle(9);
        // solver.solve(new StandardPuzzle(9), answer);

        StandardPuzzle puzzle = new StandardPuzzle(
            new int[][] {{4, 0, 0, 3, 0, 1, 0, 8, 0},
                         {6, 0, 1, 0, 2, 0, 0, 0, 0},
                         {0, 3, 9, 0, 7, 0, 0, 0, 0},
                         //------------------------//
                         {9, 2, 0, 0, 0, 0, 0, 4, 0},
                         {0, 0, 0, 7, 0, 5, 0, 0, 0},
                         {0, 5, 0, 0, 0, 0, 0, 6, 3},
                         //------------------------//
                         {0, 0, 0, 0, 1, 0, 2, 3, 0},
                         {0, 0, 0, 0, 6, 0, 9, 0, 8},
                         {0, 1, 0, 9, 0, 8, 0, 0, 5}});
        solver.solve(puzzle, answer);
    }
}
