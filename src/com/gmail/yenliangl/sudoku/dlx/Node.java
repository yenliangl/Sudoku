package com.gmail.yenliangl.sudoku.dlx;

public class Node {
    ColumnNode columnNode = null;
    Node left = this;
    Node right = this;
    Node up = this;
    Node down = this;

    // Extra application data
    int extra = -1;
}
