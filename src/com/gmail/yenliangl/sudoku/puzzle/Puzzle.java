package com.gmail.yenliang.sudoku.puzzle;

import java.util.ArrayList;

public abstract class Puzzle {
    private final int mDimension;
    private ArrayList<Cell> mCells;
    private ArrayList<Row> mRows;
    private ArrayList<Column> mColumns;
    private ArrayList<Pentomino> mPentominoes;

    Puzzle(final int dimension) {
        mDimension = dimension;
        mCells.ensureCapacity(dimension * dimension);
        mRows.ensureCapacity(dimension);
        mColumns.ensureCapacity(dimension);

        createCells(dimension);
        createRows(dimension);
        createColumns(dimension);
        createPentominoes(mPentominoes, dimension);
    }

    public int getNumOfRows() {
        return mRows.size();
    }

    public int getNumOfColumns() {
        return mColumns.size();
    }

    public int getNumOfPentominoes() {
        return mPentominoes.size();
    }

    public int getDimension() {
        return mDimension;
    }

    private void createCells(final int dimension) {
        for(int r = 0; r < dimension; r++) {
            for(int c = 0; c < dimension; c++) {
                Cell cell = new Cell(r, c);
                mCells.add(cell);
            }
        }
    }

    private void createRows(final int dimension) {
        for(int r = 0; r < dimension; r++) {
            Row row = new Row(r);
            mRows.add(row);

            for(int c = 0; c < dimension; c++) {
                int index = r * dimension + c;
                row.add(mCells.get(index));
            }
        }
    }

    private void createColumns(final int dimension) {
        for(int c = 0; c < dimension; c++) {
            Column column = new Column(c);
            mColumns.add(column);

            for(int r = 0; r < dimension; r++) {
                int index = r * dimension + c;
                column.add(mCells.get(index));
            }
        }
    }

    protected abstract void createPentominoes(ArrayList<Pentomino> pentominoes, final int dimension);

    public Iterator<Cell> getCells() {
        return mCells.iterator();
    }

    public Iterator<Row> getRows() {
        return mRows.iterator();
    }

    public Iterator<Column> getColumns() {
        return mColumns.iterator();
    }

    public Iterator<Pentomino> getPentominos() {
        return mPentominoes.iterator();
    }
}
