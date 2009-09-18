package com.gmail.yenliangl.sudoku.puzzle;

abstract class LayoutStrategy {
    protected LayoutStrategy() {}

    public void layout(Puzzle puzzle) {
        layoutRows(puzzle);
        layoutColumns(puzzle);
        layoutPentominoes(puzzle);
    }

    protected abstract void layoutPentominoes(Puzzle puzzle);
    protected abstract int generatePentominoIndexFor(Cell cell);

    protected void layoutRows(Puzzle puzzle) {
        final int dimension = puzzle.getDimension();
        for(int rowIndex = 0; rowIndex < dimension; rowIndex++) {
            Row row = puzzle.getRow(rowIndex);
            for(int columnIndex = 0; columnIndex < dimension; columnIndex++) {
                row.addCell(puzzle.getCell(rowIndex, columnIndex));
            }
        }
    }

    protected void layoutColumns(Puzzle puzzle) {
        final int dimension = puzzle.getDimension();
        for(int columnIndex = 0; columnIndex < dimension; columnIndex++) {
            Column column = puzzle.getColumn(columnIndex);
            for(int rowIndex = 0; rowIndex < dimension; rowIndex++) {
                column.addCell(puzzle.getCell(rowIndex, columnIndex));
            }
        }
    }
}

