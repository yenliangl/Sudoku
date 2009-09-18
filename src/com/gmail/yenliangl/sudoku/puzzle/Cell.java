package com.gmail.yenliangl.sudoku.puzzle;

public class Cell {
    interface OnValueChangedListener {
        void onValueChanged(int oldVal, int newVal);
    }

    private OnValueChangedListener mValueChangedListener;
    private int mValue;
    private final int mRowIndex;
    private final int mColumnIndex;
    private int mPentominoIndex;

    Cell(final int rowIndex, final int colIndex) {
        mRowIndex = rowIndex;
        mColumnIndex = colIndex;
    }

    public void setValueChangedListener(OnValueChangedListener listener) {
        mValueChangedListener = listener;
    }

    public void setPentominoIndex(int index) {
        mPentominoIndex = index;
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

    public int getPentominoIndex() {
        return mPentominoIndex;
    }

    public int getColumnIndex() {
        return mColumnIndex;
    }

}
