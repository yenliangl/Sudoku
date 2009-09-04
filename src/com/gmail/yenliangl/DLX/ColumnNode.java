package com.gmail.yenliangl.DLX;

public class ColumnNode extends Node {
    public int length;

    public ColumnNode() {
        columnNode = this;
    }

    public void addNode(Node n) {
        n.down = this;
        n.up = this.up;
        this.up.down = n;
        this.up = n;
        length++;
    }

    public String toString() {
        return "length = " + length;
    }
}
