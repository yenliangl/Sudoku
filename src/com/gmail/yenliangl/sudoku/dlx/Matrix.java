package com.gmail.yenliangl.sudoku.dlx;

import com.gmail.yenliangl.sudoku.puzzle.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.*;

class Matrix {
    private final Puzzle mPuzzle;
    private ColumnNode mRootColumnNode;
    private ArrayList<ColumnNode> mColumnHeader;
    private ArrayList<Node> mRowHeader;
    private Listener mListener;

    interface Listener {
        void onCreateRowColumnNode(String row, String column);
        void onCreateRowNumberNode(String row, String column);
        void onCreateColumnNumberNode(String row, String column);
        void onCreatePentominoNumberNode(String row, String column);
    }

    public Matrix(final Puzzle puzzle) {
        mPuzzle = puzzle;
        createColumnHeader();

        mRowHeader = new ArrayList<Node>();
        createNodes();
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public ColumnNode getRootColumnNode() {
        return mRootColumnNode;
    }

    public ColumnNode getColumnNode(int index) {
        return mColumnHeader.get(index);
    }

    public ColumnNode danceToColumnNode(int steps) {
        ColumnNode root = getRootColumnNode();
        Node node = root.right;
        int count = 0;
        while (node != root) {
            count++;
            if(count > steps) {
                break;
            }
            node = node.right;
        }
        return (ColumnNode)node;
    }

    public int getColumnNodeIndex(ColumnNode columnNode) {
        return mColumnHeader.lastIndexOf(columnNode);
    }

    public int getSizeOfColumnHeader() {
        return mColumnHeader.size();
    }

    public Node getRowNode(int index) {
        return mRowHeader.get(index);
    }

    public int getRowNodeIndex(Node rowNode) {
        return mRowHeader.lastIndexOf(rowNode);
    }

    public int calculateRowNodeIndex(final int row,
                                     final int col,
                                     final int value) {
        final int dimension = mPuzzle.getDimension();
        return row * dimension * dimension + col * dimension + value - 1;
    }

    private int calculateSizeOfColumnHeader() {
        final int dimension = mPuzzle.getDimension();

        return dimension * dimension +
            dimension * 9 +
            dimension * 9 +
            mPuzzle.getNumOfPentominoes() * 9;
    }

    private void createColumnHeader() {
        mRootColumnNode = createRootColumnNode();

        final int numOfColumns = calculateSizeOfColumnHeader();
        mColumnHeader = new ArrayList<ColumnNode>(numOfColumns);

        for(int i = 0; i < numOfColumns; i++) {
            ColumnNode columnNode = new ColumnNode();
            // columnNode.extra = i;

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
        int startColumnIndex = createRowColumnNodes();
        startColumnIndex = createRowNumberNodes(startColumnIndex);
        startColumnIndex = createColumnNumberNodes(startColumnIndex);
        createPentominoNumberNodes(startColumnIndex);
    }

    private int createRowColumnNodes() {
        final int dimension = mPuzzle.getDimension();
        Iterator<Row> rows = mPuzzle.getRows();
        while(rows.hasNext()) {
            Row row = rows.next();
            int rowIndex = row.getIndex();

            Iterator<Cell> cells = row.getCells();
            while(cells.hasNext()) {
                int columnIndex = cells.next().getColumnIndex();
                int columnNodeIndex = rowIndex * dimension + columnIndex;

                ColumnNode columnNode = getColumnNode(columnNodeIndex);
                for(int value = 1; value <= 9; value++) {
                    int rowNodeIndex = calculateRowNodeIndex(rowIndex,
                                                             columnIndex,
                                                             value);
                    Node node = new Node();
                    node.columnNode = columnNode;
                    node.rowIndex = rowIndex;
                    node.columnIndex = columnIndex;
                    node.value = value;
                    columnNode.addNode(node);
                    addToRow(rowNodeIndex, node);
                    // if(mListener) {
                    //     mListener.onCreateRowColumnNode();
                    // }
                }
            }
        }

        return dimension * dimension;
    }

    private int createRowNumberNodes(final int startColumnNodeIndex) {
        final int dimension = mPuzzle.getDimension();

        Iterator<Row> rows = mPuzzle.getRows();
        while(rows.hasNext()) {
            Row row = rows.next();
            int rowIndex = row.getIndex();

            Iterator<Cell> cells = row.getCells();
            while(cells.hasNext()) {
                int columnIndex = cells.next().getColumnIndex();

                for(int value = 1; value <= 9; value++) {
                    int rowNodeIndex = calculateRowNodeIndex(rowIndex,
                                                             columnIndex,
                                                             value);
                    int columnNodeIndex = rowIndex * dimension +
                                          (value - 1) +
                                          startColumnNodeIndex;

                    Node node = new Node();
                    node.columnNode = getColumnNode(columnNodeIndex);
                    node.rowIndex = rowIndex;
                    node.columnIndex = columnIndex;
                    node.value = value;
                    node.columnNode.addNode(node);
                    addToRow(rowNodeIndex, node);

                    // System.out.format(
                    //     "Add node to (R%dC%d#%d[%d],R%d#%d[%d,%s])\n",
                    //     rowIndex+1, columnIndex+1, value, node.extra,
                    //     rowIndex+1, value, columnNodeIndex, node.columnNode);
                }
            }
        }

        return startColumnNodeIndex + dimension * 9;
    }

    private int createColumnNumberNodes(final int startColumnNodeIndex) {
        final int dimension = mPuzzle.getDimension();

        Iterator<Row> rows = mPuzzle.getRows();
        while(rows.hasNext()) {
            Row row = rows.next();
            int rowIndex = row.getIndex();

            Iterator<Cell> cells = row.getCells();
            while(cells.hasNext()) {
                int columnIndex = cells.next().getColumnIndex();

                for(int value = 1; value <= 9; value++) {
                    int rowNodeIndex = calculateRowNodeIndex(rowIndex,
                                                             columnIndex,
                                                             value);

                    int columnNodeIndex = columnIndex * dimension +
                                          (value - 1) +
                                          startColumnNodeIndex;
                    ColumnNode columnNode = getColumnNode(columnNodeIndex);

                    Node node = new Node();
                    node.columnNode = getColumnNode(columnNodeIndex);;
                    node.rowIndex = rowIndex;
                    node.columnIndex = columnIndex;
                    node.value = value;
                    node.columnNode.addNode(node);
                    addToRow(rowNodeIndex, node);

                    // System.out.format(
                    //     "Add node to (R%dC%d#%d[%d],C%d#%d[%d,%s])\n",
                    //     rowIndex+1, columnIndex+1, value, node.extra,
                    //     columnIndex+1, value, columnNodeIndex, node.columnNode);
                }
            }
        }

        return startColumnNodeIndex + dimension * 9;
    }

    private int createPentominoNumberNodes(final int startColumnNodeIndex) {
        final int dimension = mPuzzle.getDimension();

        Iterator<Row> rows = mPuzzle.getRows();
        while(rows.hasNext()) {
            Row row = rows.next();
            int rowIndex = row.getIndex();

            Iterator<Cell> cells = row.getCells();
            while(cells.hasNext()) {
                int columnIndex = cells.next().getColumnIndex();

                for(int value = 1; value <= 9; value++) {
                    int rowNodeIndex = calculateRowNodeIndex(rowIndex,
                                                             columnIndex,
                                                             value);
                    int columnNodeIndex =
                        mPuzzle.getPentomino(rowIndex, columnIndex).getIndex() * dimension + value - 1 + startColumnNodeIndex;

                    ColumnNode columnNode = getColumnNode(columnNodeIndex);

                    Node node = new Node();
                    node.columnNode = getColumnNode(columnNodeIndex);
                    node.rowIndex = rowIndex;
                    node.columnIndex = columnIndex;
                    node.value = value;
                    node.columnNode.addNode(node);
                    addToRow(rowNodeIndex, node);

                    // System.out.format(
                    //     "Add node to (R%dC%d#%d[%d],B%d#%d[%d,%s])\n",
                    //     rowIndex+1, columnIndex+1, value, node.extra,
                    //     mPuzzle.getPentomino(rowIndex,columnIndex).getIndex()+1,
                    //     value, columnNodeIndex, node.columnNode);
                }
            }
        }
        return startColumnNodeIndex + mPuzzle.getNumOfPentominoes() * 9;
    }

    private void addToRow(int rowNodeIndex, Node node) {
        Node rootNode;
        try {
            rootNode = mRowHeader.get(rowNodeIndex);
            rootNode.left.right = node;
            node.left = rootNode.left;
            node.right = rootNode;
            rootNode.left = node;
        } catch(IndexOutOfBoundsException e) {
            mRowHeader.add(node);
        }
    }

    public static void main(String[] args) {
        int numOfFails = 0;

        // Should only make sure that Matrix generates good DLX
        // matrix.
        StandardPuzzle puzzle = new StandardPuzzle(9);
        Matrix matrix = new Matrix(new StandardPuzzle(9));

        // Start to check if the dancing links matrix is valid
        if(matrix.getSizeOfColumnHeader() != 324) {
            System.out.format("Check column header has 324 nodes? %s\n",
                              matrix.getSizeOfColumnHeader() == 324);
            numOfFails++;
        }

        // Check Row-Column constraint nodes
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                int columnNodeIndex = i * 9 + j;
                ColumnNode columnNode = matrix.getColumnNode(columnNodeIndex);
                if(columnNode.getSize() != 9) {
                    System.out.format("Check column R%dC%d has 9 nodes? %s\n",
                                      i+1, j+1, columnNode.getSize() == 9);
                    numOfFails++;
                }

                Node node = columnNode.down;
                for(int value=1; value <= 9; value++) {
                    int rowNodeIndex = matrix.calculateRowNodeIndex(i, j, value);
                    Node rowNode = matrix.getRowNode(rowNodeIndex);
                    if(rowNode != node) {
                        System.out.format(
                            "Check (R%dC%d#%d[%d],R%dC%d[%d]) has node? %s\n",
                            i+1, j+1, value, rowNodeIndex,
                            i+1, j+1, columnNodeIndex,
                            (rowNode == node));
                        numOfFails++;
                    }
                    node = node.down;
                }
            }
        }

        // @todo: Check Row-Number constraint nodes;
        // @todo: Check Column-Number constraint nodes;
        // @todo: Check Box-Number constraint nodes;

        String className = matrix.getClass().getName();
        if(numOfFails == 0) {
            System.out.println(className + ": All checks passed!!!");
        } else {
            System.out.format("%s: %d checks failed", className, numOfFails);
        }
    }
}
