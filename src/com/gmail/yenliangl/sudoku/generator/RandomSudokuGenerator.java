package com.gmail.yenliangl.sudoku.generator;

import com.gmail.yenliangl.sudoku.dlx.*;
import com.gmail.yenliangl.sudoku.puzzle.*;
import java.util.Random;
import java.util.Iterator;

public class RandomSudokuGenerator extends Solver implements Generator {
    RandomSudokuGenerator() {
        setStrategy(new SelectColumnNodeStrategy() {
                @Override
                public ColumnNode select(Matrix matrix) {
                    ColumnNode root = matrix.getRootColumnNode();
                    int count = 0;
                    ColumnNode selectedNode = (ColumnNode)root.getRight();
                    for(ColumnNode j = (ColumnNode)root.getRight();
                        j != root; j = (ColumnNode)j.getRight()) {
                        selectedNode = j;
                        if (count > 5) {
                            break;
                        }
                        count++;
                    }
                    return selectedNode;
                }
            });
    }

    @Override
    public boolean generate(int[][] result) {
        int dimension = result.length;

        // TODO: The layout that distinguish standard and jigsaw
        // puzzle should be encapsulated.
        // {
        StandardPuzzle puzzle = new StandardPuzzle(dimension);
        StandardPuzzle answer = new StandardPuzzle(dimension);

        // }

        if(solve(puzzle, answer)) {
            result = new int[dimension][dimension];

            Iterator<Row> rows = answer.getRows();
            while(rows.hasNext()) {
                Row row = rows.next();
                int rowIndex = row.getIndex();

                Iterator<Cell> cells = row.getCells();
                while(cells.hasNext()) {
                    Cell cell = cells.next();
                    int columnIndex = cell.getColumnIndex();
                    result[rowIndex][columnIndex] = cell.getValue();
                }
            }
        }

        return true;
    }

    public static void main(String[] args) {
        RandomSudokuGenerator generator = new RandomSudokuGenerator();

        int[][] puzzle = new int[9][9];
        generator.generate(puzzle);

        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                System.out.format("%d ", puzzle[i][j]);
            }
            System.out.println("");
        }
    }
}
