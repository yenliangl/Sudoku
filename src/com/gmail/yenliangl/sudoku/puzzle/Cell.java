package com.gmail.yenliangl.sudoku.puzzle;

public class Cell {
    private OnValueChangedListener mValueChangedListener;
    private int mValue;
    private final int mRowIndex;
    private final int mColumnIndex;

    Cell(final int rowIndex, final int colIndex) {
        mRowIndex = rowIndex;
        mColumnIndex = colIndex;
        mValue = 0;
    }

    interface OnValueChangedListener {
        void onValueChanged(int oldVal, int newVal);
    }

    public void setValueChangedListener(OnValueChangedListener listener) {
        mValueChangedListener = listener;
    }

    public void setValue(final int value) {
        if(value < 0 && value > 9) {
            return;
        }

        mValue = value;
        if(mValueChangedListener != null) {
            mValueChangedListener.onValueChanged(mValue, value);
        }
    }

    public int getValue() {
        return mValue;
    }

    public int getRowIndex() {
        return mRowIndex;
    }

    public int getColumnIndex() {
        return mColumnIndex;
    }

}
