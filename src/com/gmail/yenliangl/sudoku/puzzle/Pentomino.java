package com.gmail.yenliangl.sudoku.puzzle;

import java.util.ArrayList;
import java.util.Iterator;

public class Pentomino {
    private final int mIndex;
    private ArrayList<Cell> mCells;

    Pentomino(final int index) {
        mIndex = index;

        mCells = new ArrayList<Cell>();
    }

    public int getIndex() {
        return mIndex;
    }

    public void addCell(Cell cell) {
        mCells.add(cell);
    }

    public Iterator<Cell> getCells() {
        return mCells.iterator();
    }
}
