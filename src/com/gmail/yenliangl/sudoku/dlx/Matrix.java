package com.gmail.yenliangl.sudoku.dlx;

import com.gmail.yenliangl.sudoku.puzzle.*;
import java.util.ArrayList;

public class Matrix {
    private final Puzzle mPuzzle;
    private ColumnNode mRootColumnNode;
    private ArrayList<ColumnNode> mColumnHeader;
    private ArrayList<Node> mRowHeader;

    // Create DLX matrix from a sudoku puzzle
    public Matrix(final Puzzle puzzle) {
        mPuzzle = puzzle;
        createColumnHeader();
        createNodes();
    }

    public ColumnNode getRootColumnNode() {
        return mRootColumnNode;
    }

    public ColumnNode getColumnNode(int index) {
        return mColumnHeader.get(index);
    }

    public Node getRowNode(int index) {
        return mRowHeader.get(index);
    }

    protected int calculateSizeOfColumnHeader() {
        final int dimension = mPuzzle.getDimension();

        return dimension * dimension +
            dimension * 9 +
            dimension * 9 +
            mPuzzle.getNumOfPentominoes() * 9;
    }

    private void createColumnHeader() {
        mRootColumnNode = createRootColumnNode();

        // TODO: Calculate how many column nodes should this matrix has?
        // It should be obtained from physical puzzle.
        final int numOfColumns = calculateSizeOfColumnHeader();

        for(int i = 0; i < numOfColumns; i++) {
            ColumnNode columnNode = new ColumnNode();
            columnNode.extra = i;

            mRootColumnNode.left.right = columnNode;
            columnNode.left = mRootColumnNode.left;
            columnNode.right = mRootColumnNode;
            mRootColumnNode.left = columnNode;
            mColumnHeader.add(columnNode);
        }
    }

    private ColumnNode createRootColumnNode() {
        ColumnNode node = new ColumnNode();
        node.up = null;
        node.down = null;
        return node;
    }

    private void createNodes() {
        int nextStartColumnIndex = createRowColumnNodes();

        nextStartColumnIndex = createRowNumberNodes(nextStartColumnIndex);
        nextStartColumnIndex = createColumnNumberNodes(nextStartColumnIndex);

        createPentominoNumberNodes(nextStartColumnIndex);
    }

    private int createRowColumnNodes() {
        final int dimension = mPuzzle.getDimension();
        for(int row = 0; i < dimension; i++) {
            for(int col = 0; j < dimension; j++) {
                int columnIndex = row * dimension + col;
                ColumnNode columnNode = getColumnNode(columnIndex);

                for(int value = 1; value <= 9; value++) {
                    Node node = new Node();
                    node.columnNode = columnNode;
                    node.extra = calculateRowIndex(row, col, value);

                    addToRow(node);
                }
            }
        }

        return dimension * dimension;
    }

    private int createRowNumberNodes(final int startColumnIndex) {
        final int dimension = mPuzzle.getDimension();
        for(int row = 0; row < dimension; row++) {
            for(int col = 0; col < dimension; col++) {
                for(int value = 1; value <= 9; value++) {
                    int rowIndex = calculateRowIndex(row, col, value);
                    int columnIndex =
                        row * dimension + (value - 1) + startColumnIndex;

                    Node node = new Node();
                    node.columnNode = getColumnNode(columnIndex);;
                    node.extra = rowIndex;
                    node.columnNode.addNode(node);
                    addToRow(node);
                }
            }
        }

        return startColumnIndex + dimension * 9;
    }

    private int createColumnNumberNodes(final int startColumnIndex) {
        for(int row = 0; row < dimension; row++) {
            for(int col = 0; col < dimension; col++) {
                for(int value = 1; value <= 9; value++) {

                    int rowIndex = calculateRowIndex(row, col, value);
                    int columnIndex =
                        col * dimension + (value - 1) + startColumnIndex;

                    ColumnNode columnNode = getColumnNode(columnIndex);
                    Node node = new Node();
                    node.columnNode = getColumnNode(columnIndex);
                    node.extra = rowIndex;
                    node.columnNode.addNode(node);
                    addToRow(node);
                }
            }
        }

        return startColumnIndex + dimension * 9;
    }

    private int createPentominoNumberNodes(final int startColumnIndex) {



        return startColumnIndex + 9 * 9;
    }

    private int calculateRowIndex(final int row,
                                  final int col,
                                  final int value) {
        final int dimension = mPuzzle.getDimension();
        return row * dimension * dimension + col * dimension + value - 1;
    }

    private void addToRow(Node node) {
        Node rootNode;
        try {
            rootNode = mRowHeader.get(node.extra);
            rootNode.left.right = node;
            node.left = rootNode.left;
            node.right = rootNode;
            rootNode.left = node;
        } catch(IndexOutOfBoundsException e) {
            mRowHeader.addNode(node);
        }
    }















    public static void main(String[] args) {
        int[][] A = {{0, 0, 1, 0, 1, 1, 0},
                     {1, 0, 0, 1, 0, 0, 1},
                     {0, 1, 1, 0, 0, 1, 0},
                     {1, 0, 0, 1, 0, 0, 0},
                     {0, 1, 0, 0, 0, 0, 1},
                     {0, 0, 0, 1, 1, 0, 1}};
        Matrix dls = new Matrix(A);

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
