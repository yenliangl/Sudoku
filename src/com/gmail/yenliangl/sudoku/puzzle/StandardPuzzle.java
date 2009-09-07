package com.gmail.yenliangl.sudoku.puzzle;

import java.lang.Math;
import java.util.ArrayList;

public class StandardPuzzle extends Puzzle {
    StandardPuzzle(final int dimension) {
        super(dimension);
    }

    @Override
    private void createPentominoes(ArrayList<Pentomino> pentominoes,
                                   final int dimension) {
        pentominoes.ensureCapacity(dimension);
        for(int i = 0; i < dimension; i++) {
            pentominoes.add(new Pentomino(i));
        }

        final int N = (int)Math.sqrt((double)dimension);
        Iterator<Cell> allCells = getCells();
        while(allCells.hasNext()) {
            Cell cell = allCells.next();

            int row = cell.getRow();
            int column = cell.getColumn();
            int pRow = row / N;
            int pColumn = column / N;
            int pIndex = pRow * N + pColumn;
            pentominoes.get(pIndex).addCell(cell);
        }
    }
}
