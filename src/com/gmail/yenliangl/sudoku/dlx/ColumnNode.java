package com.gmail.yenliangl.sudoku.dlx;

// public class ColumnNode extends Node {
class ColumnNode extends Node {
    private int mLength;

    public ColumnNode() {
        columnNode = this;
    }

    public void addNode(Node n) {
        n.down = this;
        n.up = this.up;
        this.up.down = n;
        this.up = n;
        mLength++;
    }

    public int getSize() {
        return mLength;
    }

    public void decrementSize() {
        mLength--;
    }

    public void incrementSize() {
        mLength++;
    }

    // public String toString() {
    //     return "length = " + length;
    // }
}
