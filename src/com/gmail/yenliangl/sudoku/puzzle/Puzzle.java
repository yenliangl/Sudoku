package com.gmail.yenliangl.sudoku.puzzle;

import java.util.ArrayList;
import java.util.Iterator;

public class Puzzle {
    private final int mDimension;
    private ArrayList<Cell> mCells;
    private ArrayList<Row> mRows;
    private ArrayList<Column> mColumns;
    private ArrayList<Pentomino> mPentominoes;

    protected Puzzle(final int dimension) {
        mDimension = dimension;

        createCells();
        createRows();
        createColumns();
        createPentominoes();
    }

    protected Puzzle(final int[][] array) {
        this(array.length);

        for(int i = 0; i < array.length; i++) {
            for(int j = 0; j < array[i].length; j++) {
                getCell(i, j).setValue(array[i][j]);
            }
        }
    }

    public void layout(LayoutStrategy strategy) {
        // Erase current layout
        Iterator<Row> rows = getRows();
        while(rows.hasNext()) {
            Row row = rows.next();
            row.clear();
        }

        Iterator<Column> columns = getColumns();
        while(columns.hasNext()) {
            Column column = columns.next();
            column.clear();
        }

        Iterator<Pentomino> pentominoes = getPentominoes();
        while(pentominoes.hasNext()) {
            Pentomino pentomino = pentominoes.next();
            pentomino.clear();
        }

        strategy.layout(this);
    }

    public int getNumOfRows() {
        return mDimension;
    }

    public int getNumOfColumns() {
        return mDimension;
    }

    public int getNumOfCells() {
        return mDimension;
    }

    public int getNumOfPentominoes() {
        return mDimension;
    }

    public int getDimension() {
        return mDimension;
    }

    public Cell getCell(final int rowIndex, final int columnIndex) {
        int cellIndex = calculateCellIndex(rowIndex, columnIndex);
        return mCells.get(cellIndex);
    }

    public Iterator<Cell> getCells() {
        return mCells.iterator();
    }

    public Iterator<Row> getRows() {
        return mRows.iterator();
    }

    public Iterator<Column> getColumns() {
        return mColumns.iterator();
    }

    public Iterator<Pentomino> getPentominoes() {
        return mPentominoes.iterator();
    }

    public Row getRow(final int rowIndex) {
        return mRows.get(rowIndex);
    }

    public Column getColumn(final int columnIndex) {
        return mColumns.get(columnIndex);
    }

    public Pentomino getPentomino(final int rowIndex,
                                  final int columnIndex) {
        return mPentominoes.get(
            getCell(rowIndex, columnIndex).getPentominoIndex());
    }

    public Pentomino getPentomino(final int pentominoIndex) {
        return mPentominoes.get(pentominoIndex);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<Row> rows = getRows();
        while(rows.hasNext()) {
            Row row = rows.next();

            Iterator<Cell> cells = row.getCells();
            while(cells.hasNext()) {
                Cell cell = cells.next();
                if(cell.getValue() == 0) {
                    sb.append("  ");
                } else {
                    sb.append(cell.getValue());
                    sb.append(" ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private void createCells() {
        mCells = new ArrayList<Cell>(mDimension * mDimension);
        for(int i = 0; i < mDimension; i++) {
            for(int j = 0; j < mDimension; j++) {
                mCells.add(new Cell(i, j));
            }
        }
    }

    private void createRows() {
        mRows = new ArrayList<Row>(mDimension);
        for(int i = 0; i < mDimension; i++) {
            mRows.add(new Row(i));
        }
    }

    private void createColumns() {
        mColumns = new ArrayList<Column>(mDimension);
        for(int i = 0; i < mDimension; i++) {
            mColumns.add(new Column(i));
        }
    }

    private void createPentominoes() {
        mPentominoes = new ArrayList<Pentomino>(mDimension);
        for(int i = 0; i < mDimension; i++) {
            mPentominoes.add(new Pentomino(i));
        }
    }

    private int calculateCellIndex(final int rowIndex,
                                   final int columnIndex) {
        return rowIndex * mDimension + columnIndex;
    }
}
