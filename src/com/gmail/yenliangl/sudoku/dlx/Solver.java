package com.gmail.yenliangl.sudoku.dlx;

import java.util.ArrayList;
import java.util.Stack;
import java.util.Iterator;
import java.util.Random;

import com.gmail.yenliangl.sudoku.puzzle.*;

public class Solver {
    public interface Listener {
        void onBegin();
        void onSolved(int[][] answer);
        void onEnd();
    }

    interface SelectColumnNodeStrategy {
        ColumnNode select(Matrix matrix);
    }

    private Listener mListener;
    private SelectColumnNodeStrategy mStrategy;

    public void setListener(Listener listener) {
        mListener = listener;
    }

    void setStrategy(SelectColumnNodeStrategy strategy) {
        mStrategy = strategy;
    }

    public void solve(Puzzle puzzle) {
        if(mListener == null) {
            mListener = createDefaultListener();
        }

        if(mStrategy == null) {
            mStrategy = createDefaultStrategy();
        }

        mListener.onBegin();

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

        solve(matrix, 0, solution);

        mListener.onEnd();
    }

    private Listener createDefaultListener() {
        return new Listener() {
            public void onBegin() {}
            public void onSolved(int[][] answer) {}
            public void onEnd() {}
        };
    }

    private SelectColumnNodeStrategy createDefaultStrategy() {
        return new SelectColumnNodeStrategy() {
            public ColumnNode select(Matrix matrix) {
                ColumnNode root = matrix.getRootColumnNode();

                // just give it an initial value
                ColumnNode selectedNode = (ColumnNode)root.right;
                int s = Integer.MAX_VALUE;
                for(ColumnNode j = (ColumnNode)root.right;
                    j != root; j = (ColumnNode)j.right) {
                    if(j.getSize() < s) {
                        selectedNode = j;
                        s = j.getSize();
                    }
                }
                return selectedNode;
            }
        };
    }

    private void solve(Matrix matrix, int k, Stack<Node> solution) {
        ColumnNode h = matrix.getRootColumnNode();
        if(h.right == h) {
            generateAnswer(solution);
            return;
        }

        ColumnNode c = mStrategy.select(matrix);
        coverColumn(c);

        for(Node r = c.down; r != c; r = r.down) {
            solution.push(r);

            for(Node j = r.right; j != r; j = j.right) {
                coverColumn(j.columnNode);
            }

            solve(matrix, k+1, solution);

            r = solution.pop();

            c = r.columnNode;
            for(Node j = r.left; j != r; j = j.left) {
                uncoverColumn(j.columnNode);
            }
        }
        uncoverColumn(c);
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
    }

    private int[][] generateAnswer(Stack<Node> solutionStack) {
        int dimension = (int)Math.sqrt(solutionStack.size());
        int[][] result = new int[dimension][dimension];

        Iterator<Node> nodes = solutionStack.iterator();
        while(nodes.hasNext()) {
            Node node = nodes.next();
            result[node.rowIndex][node.columnIndex] = node.value;
        }
        mListener.onSolved(result);

        return result;
    }

    public static void main(String[] args) {
        Solver solver = new Solver();
        solver.setListener(new Solver.Listener() {
                private int mNumOfSolutions;

                @Override
                public void onBegin() {
                    mNumOfSolutions = 0;
                }

                @Override
                public void onSolved(int[][] answer) {
                    mNumOfSolutions++;
                    System.out.println("!!!Puzzle solved! " + mNumOfSolutions + " solutions found!!!");
                    for(int i = 0; i < answer.length; i++) {
                        for(int j = 0; j < answer[0].length; j++) {
                            System.out.format("%d ", answer[i][j]);
                        }
                        System.out.println("");
                    }
                }

                @Override
                public void onEnd() {
                    if(mNumOfSolutions == 0) {
                        System.out.println("!!!No solution!!!");
                    }
                }
            });

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
        solver.solve(puzzle);

        puzzle = new StandardPuzzle(
            new int[][] {{0, 5, 3, 0, 2, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0, 9, 4, 0, 1},
                         {0, 6, 0, 7, 0, 0, 0, 0, 3},
                         //------------------------//
                         {0, 8, 0, 0, 7, 0, 6, 0, 0},
                         {4, 0, 0, 8, 0, 3, 0, 0, 5},
                         {0, 0, 5, 0, 9, 0, 0, 4, 0},
                         //------------------------//
                         {8, 0, 0, 0, 0, 5, 0, 2, 0},
                         {2, 0, 9, 1, 0, 0, 0, 0, 0},
                         {0, 0, 0, 0, 3, 0, 9, 8, 0}});
        solver.solve(puzzle);

        puzzle = new StandardPuzzle(
            new int[][] {{0, 5, 3, 0, 0, 0, 0, 0, 0},
                         {0, 0, 0, 0, 0, 9, 4, 0, 0},
                         {0, 6, 0, 7, 0, 0, 0, 0, 3},
                         //------------------------//
                         {0, 8, 0, 0, 7, 0, 6, 0, 0},
                         {4, 0, 0, 8, 0, 3, 0, 0, 0},
                         {0, 0, 5, 0, 9, 0, 0, 4, 0},
                         //------------------------//
                         {8, 0, 0, 0, 0, 0, 0, 2, 0},
                         {2, 0, 9, 1, 0, 0, 0, 0, 0},
                         {0, 0, 0, 0, 3, 0, 9, 8, 0}});
        solver.solve(puzzle);

    }
}
