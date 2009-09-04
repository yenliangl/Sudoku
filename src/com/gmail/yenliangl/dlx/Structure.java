package com.gmail.yenliangl.dlx;

import java.util.ArrayList;

public class Structure {
    private ColumnNode mRootColumnNode;
    private ArrayList<ColumnNode> mColumnHeader;
    private ArrayList<Node> mRowHeader;

    public Structure(int[][] matrix) {
        mColumnHeader = new ArrayList<ColumnNode>(matrix[0].length);
        mRowHeader = new ArrayList<Node>(matrix.length);
        mRootColumnNode = createRootColumnNode();
        initialize(matrix);
    }

    public ColumnNode getColumnNode(int index) {
        return mColumnHeader.get(index);
    }

    public int getColumnNodeIndex(Node node) {
        return mColumnHeader.lastIndexOf(node);
    }

    public Node getRowNode(int index) {
        return mRowHeader.get(index);
    }

    public int getRowNodeIndex(Node node) {
        return mRowHeader.lastIndexOf(node);
    }

    public ColumnNode getRootColumnNode() {
        return mRootColumnNode;
    }

    // create dancing links structure from matrix
    private void initialize(int[][] matrix) {
        createColumnHeader(matrix[0].length);

        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 1) {
                    ColumnNode columnNode = getColumnNode(j);

                    Node rowNode;
                    try {
                        rowNode = mRowHeader.get(i);
                    } catch(IndexOutOfBoundsException e) {
                        rowNode = new Node();
                        rowNode.columnNode = columnNode;
                        mRowHeader.add(i, rowNode);
                    }
                    Node node = new Node();
                    node.columnNode = columnNode;
                    columnNode.addNode(node);

                    node.left = rowNode.left;
                    node.right = rowNode;
                    rowNode.left.right = node;
                    rowNode.left = node;
                }
            }
        }
    }

    private ColumnNode createRootColumnNode() {
        ColumnNode node = new ColumnNode();
        node.up = null;
        node.down = null;
        return node;
    }

    private void createColumnHeader(int numOfColumns) {
        for(int i = 0; i < numOfColumns; i++) {
            ColumnNode c = new ColumnNode();
            mRootColumnNode.left.right = c;
            c.left = mRootColumnNode.left;
            c.right = mRootColumnNode;
            mRootColumnNode.left = c;

            mColumnHeader.add(c);
        }
    }

    public static void main(String[] args) {
        int[][] A = {{0, 0, 1, 0, 1, 1, 0},
                     {1, 0, 0, 1, 0, 0, 1},
                     {0, 1, 1, 0, 0, 1, 0},
                     {1, 0, 0, 1, 0, 0, 0},
                     {0, 1, 0, 0, 0, 0, 1},
                     {0, 0, 0, 1, 1, 0, 1}};
        Structure dls = new Structure(A);

        if(dls.getRowNode(2).left != dls.getColumnNode(5).up) {
            System.out.println("Node (2,2) fails");
        }

        if(dls.getRowNode(5).left.left != dls.getColumnNode(4).down.down) {
            System.out.println("Node (5,4) fails");
        }

        if(dls.getColumnNode(0).down.down.right != dls.getRowNode(5).left.up.up.left.down) {
            System.out.println("Node (3,3) fails");
        }
    }
}
