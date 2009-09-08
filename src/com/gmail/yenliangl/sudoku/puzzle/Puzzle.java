package com.gmail.yenliangl.sudoku.puzzle;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class Puzzle {
    private final int mDimension;
    private ArrayList<Cell> mCells;
    private ArrayList<Row> mRows;
    private ArrayList<Column> mColumns;
    private ArrayList<Pentomino> mPentominoes;

    Puzzle(final int dimension) {
        mDimension = dimension;

        createCells(dimension);
        createRows(dimension);
        createColumns(dimension);

        mPentominoes = new ArrayList<Pentomino>(dimension);
        createPentominoes(mPentominoes, dimension);
    }

    Puzzle(int[][] array) {
        this(array.length);

        for(int i = 0; i < array.length; i++) {
            for(int j = 0; j < array[i].length; j++) {
                getCell(i, j).setValue(array[i][j]);
            }
        }
    }

    // Puzzle(int dimension, String puzzleString) {
    // }

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

    public Cell getCell(int rowIndex, int columnIndex) {
        int cellIndex = calculateCellIndex(rowIndex, columnIndex);
        return mCells.get(cellIndex);
    }

    private void createCells(final int dimension) {
        mCells = new ArrayList<Cell>(dimension * dimension);

        for(int r = 0; r < dimension; r++) {
            for(int c = 0; c < dimension; c++) {
                Cell cell = new Cell(r, c);
                mCells.add(cell);
            }
        }
    }

    private void createRows(final int dimension) {
        mRows = new ArrayList<Row>(dimension);

        for(int r = 0; r < dimension; r++) {
            Row row = new Row(r);
            mRows.add(row);

            for(int c = 0; c < dimension; c++) {
                int index = r * dimension + c;
                row.addCell(mCells.get(index));
            }
        }
    }

    private void createColumns(final int dimension) {
        mColumns = new ArrayList<Column>(dimension);

        for(int c = 0; c < dimension; c++) {
            Column column = new Column(c);
            mColumns.add(column);

            for(int r = 0; r < dimension; r++) {
                int index = r * dimension + c;
                column.addCell(mCells.get(index));
            }
        }
    }

    protected Pentomino getPentomino(int index) {
        return mPentominoes.get(index);
    }

    protected abstract void createPentominoes(ArrayList<Pentomino> pentominoes, final int dimension);

    public abstract Pentomino getPentomino(int rowIndex, int columnIndex);

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

    private int calculateCellIndex(final int rowIndex, final int columnIndex) {
        return rowIndex * mDimension + columnIndex;
    }
}
