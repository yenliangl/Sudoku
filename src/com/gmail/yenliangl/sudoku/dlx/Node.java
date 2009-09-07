package com.gmail.yenliangl.dlx;

public class Node {
    Node columnNode = null;
    Node left = this;
    Node right = this;
    Node up = this;
    Node down = this;

    // Extra application data
    int extra = -1;
}
