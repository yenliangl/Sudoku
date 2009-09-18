package com.gmail.yenliangl.sudoku.puzzle;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Iterator;

public class StandardPuzzle extends Puzzle {
    private class StandardLayoutStrategy extends LayoutStrategy {
        private final int mSqrtOfDimension;

        public StandardLayoutStrategy(int dimension) {
            mSqrtOfDimension = (int)Math.sqrt((double)dimension);
        }

        @Override
        protected void layoutPentominoes(Puzzle puzzle) {
            Iterator<Cell> allCells = puzzle.getCells();
            while(allCells.hasNext()) {
                Cell cell = allCells.next();
                int pentominoIndex = generatePentominoIndexFor(cell);
                puzzle.getPentomino(pentominoIndex).addCell(cell);
            }
        }

        @Override
        protected int generatePentominoIndexFor(Cell cell) {
            return
                (cell.getRowIndex() / mSqrtOfDimension) * mSqrtOfDimension +
                cell.getColumnIndex() / mSqrtOfDimension;
        }
    }

    public StandardPuzzle(final int dimension) {
        super(dimension);

        layout(new StandardLayoutStrategy(dimension));
    }

    public StandardPuzzle(final int[][] array) {
        super(array);

        layout(new StandardLayoutStrategy(array.length));
    }
}
