package com.gmail.yenliangl.sudoku.generator;

import com.gmail.yenliangl.sudoku.dlx.*;
import com.gmail.yenliangl.sudoku.puzzle.*;
import java.util.*;

public class StandardSudokuGenerator implements Generator {
    private int mNumOfSolutions;
    private Solver mSolver;
    private Listener mListener;

    private class Listener implements Solver.Listener {
        public void onBegin() {
            mNumOfSolutions = 0;
        }

        public void onSolved(int[][] answer) {
            mNumOfSolutions++;
        }

        public void onEnd() {
        }
    }

    StandardSudokuGenerator() {
        mSolver = new Solver();
        mListener = new Listener();
        mSolver.setListener(mListener);
    }

    @Override
    public Puzzle generate(int level) {
        StandardPuzzle puzzle = new StandardPuzzle(generateSudokuTemplate());

        Random rand = new Random();
        int count = 0;
        // This number is used to distinguish difficulty level
        while (count < 58) {
            // Pick up a cell randomly in a block
            // int index = rand.nextInt(9) * 9 + rand.nextInt(9);

            // Pick up a cell randomly by a row and a column in a block
            int index = rand.nextInt(9) * 9 + rand.nextInt(3) * 3 + rand.nextInt(3);
            int rowIndex = index / 9;
            int columnIndex = index % 9;

            // Unmark a random cell and try to solve it
            Cell cell = puzzle.getCell(rowIndex, columnIndex);
            int value = cell.getValue();
            if(value != 0) {
                count++;
                cell.setValue(0);
                mSolver.solve(puzzle);

                // Put this cell back
                if(mNumOfSolutions == 0) {
                    cell.setValue(value);
                    break;
                }
            }
        }

        return puzzle;
    }

    protected int[][] generateSudokuTemplate() {
        int[][][] goldenTemplates =
            {{{4, 5, 6, 7, 9, 8, 2, 3, 1},
              {1, 2, 3, 4, 6, 5, 8, 9, 7},
              {7, 8, 9, 1, 3, 2, 5, 6, 4},
              {9, 1, 2, 3, 5, 4, 7, 8, 6},
              {6, 7, 8, 9, 2, 1, 4, 5, 3},
              {3, 4, 5, 6, 8, 7, 1, 2, 9},
              {8, 9, 1, 2, 4, 3, 6, 7, 5},
              {2, 3, 4, 5, 7, 6, 9, 1, 8},
              {5, 6, 7, 8, 1, 9, 3, 4, 2}},

             {{9, 1, 2, 7, 8, 6, 3, 5, 4},
              {3, 4, 5, 1, 2, 9, 6, 8, 7},
              {6, 7, 8, 4, 5, 3, 9, 2, 1},
              {8, 9, 1, 6, 7, 5, 2, 4, 3},
              {2, 3, 4, 9, 1, 8, 5, 7, 6},
              {5, 6, 7, 3, 4, 2, 8, 1, 9},
              {4, 5, 6, 2, 3, 1, 7, 9, 8},
              {1, 2, 3, 8, 9, 7, 4, 6, 5},
              {7, 8, 9, 5, 6, 4, 1, 3, 2}},

             {{6, 7, 8, 3, 5, 4, 9, 1, 2},
              {9, 1, 2, 6, 8, 7, 3, 4, 5},
              {3, 4, 5, 9, 2, 1, 6, 7, 8},
              {1, 2, 3, 7, 9, 8, 4, 5, 6},
              {7, 8, 9, 4, 6, 5, 1, 2, 3},
              {4, 5, 6, 1, 3, 2, 7, 8, 9},
              {5, 6, 7, 2, 4, 3, 8, 9, 1},
              {8, 9, 1, 5, 7, 6, 2, 3, 4},
              {2, 3, 4, 8, 1, 9, 5, 6, 7}},

             {{7, 8, 6, 3, 4, 5, 9, 2, 1},
              {1, 2, 9, 6, 7, 8, 3, 5, 4},
              {4, 5, 3, 9, 1, 2, 6, 8, 7},
              {5, 6, 4, 1, 2, 3, 7, 9, 8},
              {2, 3, 1, 7, 8, 9, 4, 6, 5},
              {8, 9, 7, 4, 5, 6, 1, 3, 2},
              {9, 1, 8, 5, 6, 7, 2, 4, 3},
              {3, 4, 2, 8, 9, 1, 5, 7, 6},
              {6, 7, 5, 2, 3, 4, 8, 1, 9}},

             {{3, 5, 4, 6, 8, 7, 2, 9, 1},
              {6, 8, 7, 9, 2, 1, 5, 3, 4},
              {9, 2, 1, 3, 5, 4, 8, 6, 7},
              {8, 1, 9, 2, 4, 3, 7, 5, 6},
              {2, 4, 3, 5, 7, 6, 1, 8, 9},
              {5, 7, 6, 8, 1, 9, 4, 2, 3},
              {4, 6, 5, 7, 9, 8, 3, 1, 2},
              {1, 3, 2, 4, 6, 5, 9, 7, 8},
              {7, 9, 8, 1, 3, 2, 6, 4, 5}},

             {{5, 3, 4, 1, 9, 2, 7, 8, 6},
              {2, 9, 1, 7, 6, 8, 4, 5, 3},
              {8, 6, 7, 4, 3, 5, 1, 2, 9},
              {3, 1, 2, 8, 7, 9, 5, 6, 4},
              {6, 4, 5, 2, 1, 3, 8, 9, 7},
              {9, 7, 8, 5, 4, 6, 2, 3, 1},
              {7, 5, 6, 3, 2, 4, 9, 1, 8},
              {1, 8, 9, 6, 5, 7, 3, 4, 2},
              {4, 2, 3, 9, 8, 1, 6, 7, 5}}
            };

        Random rand = new Random();
        int[][] golden = goldenTemplates[rand.nextInt(goldenTemplates.length)];

        exchangeBigRows(rand, golden);
        exchangeBigColumns(rand, golden);
        exchangeRowsInBigRow(rand, golden);
        exchangeColumnsInBigColumn(rand, golden);

        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                System.out.format("%d ", golden[i][j]);
            }
            System.out.println("");
        }
        return golden;
    }

    private void exchangeBigRows(Random rand, int[][] puzzle) {
        final int count = rand.nextInt(3) + 1;

        for(int n = 0; n < count; n++) {
            int[] indexes = generateRandomIntPair(rand);
            int bigRowIndex0 = indexes[0],
                bigRowIndex1 = indexes[1];
            for(int r=0; r < 3; r++) {
                int rowIndex0 = bigRowIndex0 * 3 + r,
                    rowIndex1 = bigRowIndex1 * 3 + r;
                exchangeRows(rowIndex0, rowIndex1, puzzle);
            }
        }
    }

    private void exchangeBigColumns(Random rand, int[][] puzzle) {
        final int count = rand.nextInt(3) + 1;

        for(int n = 0; n < count; n++) {
            int[] indexes = generateRandomIntPair(rand);
            int bigColumnIndex0 = indexes[0],
                bigColumnIndex1 = indexes[1];
            for(int c=0; c < 3; c++) {
                int columnIndex0 = bigColumnIndex0 * 3 + c,
                    columnIndex1 = bigColumnIndex1 * 3 + c;
                exchangeColumns(columnIndex0, columnIndex1, puzzle);
            }
        }
    }

    private void exchangeRowsInBigRow(Random rand, int[][] puzzle) {
        for(int count = 0; count < 3; count++) {
            final int bigRowIndex = rand.nextInt(3);

            for(int n = 0; n < rand.nextInt(3); n++) {
                int[] indexes = generateRandomIntPair(rand);
                int row0 = indexes[0], row1 = indexes[1];

                int rowIndex0 = bigRowIndex * 3 + row0;
                int rowIndex1 = bigRowIndex * 3 + row1;
                exchangeRows(rowIndex0, rowIndex1, puzzle);
            }
        }
    }

    private void exchangeColumnsInBigColumn(Random rand, int[][] puzzle) {
        for(int count = 0; count < 3; count++) {
            final int bigColumnIndex = rand.nextInt(3);

            for(int n = 0; n < rand.nextInt(3); n++) {
                int[] indexes = generateRandomIntPair(rand);
                int column0 = indexes[0], column1 = indexes[1];

                int columnIndex0 = bigColumnIndex * 3 + column0;
                int columnIndex1 = bigColumnIndex * 3 + column1;

                exchangeColumns(columnIndex0, columnIndex1, puzzle);
            }
        }
    }

    private void exchangeRows(final int rowIndex0,
                              final int rowIndex1,
                              int[][] puzzle) {
        int[] temp = puzzle[rowIndex0];
        puzzle[rowIndex0] = puzzle[rowIndex1];
        puzzle[rowIndex1] = temp;
    }

    private void exchangeColumns(final int columnIndex0,
                                 final int columnIndex1,
                                 int[][] puzzle) {
        for(int rowIndex = 0; rowIndex < 9; rowIndex++) {
            int temp = puzzle[rowIndex][columnIndex0];
            puzzle[rowIndex][columnIndex0] = puzzle[rowIndex][columnIndex1];
            puzzle[rowIndex][columnIndex1] = temp;
        }
    }

    private int[] generateRandomIntPair(Random rand) {
        int index0 = rand.nextInt(3);
        int index1 = rand.nextInt(3);
        if(index0 == index1) {
            if(index0 == 2) {
                index0--;
            } else {
                index0++;
            }
        }
        return new int[] {index0, index1};
    }

    public static void main(String[] args) {
        StandardSudokuGenerator generator = new StandardSudokuGenerator();

        Puzzle puzzle = generator.generate(0);

        System.out.println("======Sudoku======");
        System.out.println(puzzle);
        System.out.println("==================");
    }
}
