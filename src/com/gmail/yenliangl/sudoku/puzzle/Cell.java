package com.gmail.yenliangl.sudoku.puzzle;

public class Cell {
    private OnValueChangedListener mValueChangedListener;
    private int mValue;
    private final int mRow;
    private final int mColumn;

    PuzzleCell(final int row, final int col) {
        mRow = row;
        mColumn = mCol;
        mValue = 0;
    }

    public class OnValueChangedListener {
        protected abstract void onValueChanged(int oldVal, int newVal);
    }

    public void setValueChangedListener(OnValueChangedListener listener) {
        mValueChangedListener = listener;
    }

    public void setValue(final int value) {
        if(value < 0 && value > 9) {
            return;
        }

        if(value != mValue && mValueChangedListener) {
            mValueChangedListener.onValueChanged(mValue, value);
            mValue = value;
        }
    }

    public int getValue() {
        return mValue;
    }

    public int getRow() {
        return mRow;
    }

    public int getColumn() {
        return mColumn;
    }

}
