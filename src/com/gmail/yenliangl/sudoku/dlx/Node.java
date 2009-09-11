package com.gmail.yenliangl.sudoku.dlx;

public class Node {
    ColumnNode columnNode = null;
    Node left = this;
    Node right = this;
    Node up = this;
    Node down = this;

    // Extra application data
    int rowIndex = -1;
    int columnIndex = -1;
    int value = 0;

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public Node getUp() {
        return right;
    }

    public Node getDown() {
        return right;
    }

}
