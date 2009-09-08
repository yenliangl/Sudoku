package com.gmail.yenliangl.sudoku.dlx;

import com.gmail.yenliangl.sudoku.puzzle.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Matrix {
    private final Puzzle mPuzzle;
    private ColumnNode mRootColumnNode;
    private ArrayList<ColumnNode> mColumnHeader;
    private ArrayList<Node> mRowHeader;

    /**
     * Create dancing links matrix from a sudoku puzzle.
     *
     * @param puzzle
     *
     */
    public Matrix(final Puzzle puzzle) {
        mPuzzle = puzzle;
        createColumnHeader();

        mRowHeader = new ArrayList<Node>();
        createNodes();
    }

    /**
     * Return root node of column header.
     *
     * @return ColumnNode
     */
    public ColumnNode getRootColumnNode() {
        return mRootColumnNode;
    }

    /**
     * Return a column node in column header from its index.
     *
     * @param index
     *
     * @return
     */
    public ColumnNode getColumnNode(int index) {
        return mColumnHeader.get(index);
    }

    /**
     *
     *
     * @param index
     *
     * @return
     */
    public Node getRowNode(int index) {
        return mRowHeader.get(index);
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
        Iterator<Row> rows = mPuzzle.getRows();
        while(rows.hasNext()) {
            Row row = rows.next();
            int rowIndex = row.getIndex();

            Iterator<Column> columns = mPuzzle.getColumns();
            while(columns.hasNext()) {
                int columnIndex = columns.next().getIndex();
                int columnNodeIndex = rowIndex * dimension +
                                      columnIndex;

                ColumnNode columnNode = getColumnNode(columnNodeIndex);

                for(int value = 1; value <= 9; value++) {
                    Node node = new Node();
                    node.columnNode = columnNode;
                    node.extra = calculateRowIndex(rowIndex,
                                                   columnIndex,
                                                   value);

                    addToRow(node);
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

            Iterator<Column> columns = mPuzzle.getColumns();
            while(columns.hasNext()) {
                Column column = columns.next();
                int columnIndex = column.getIndex();

                for(int value = 1; value <= 9; value++) {
                    int rowNodeIndex = calculateRowIndex(rowIndex,
                                                         columnIndex,
                                                         value);

                    int columnNodeIndex = rowIndex * dimension +
                                          (value - 1) +
                                          startColumnNodeIndex;

                    Node node = new Node();
                    node.columnNode = getColumnNode(columnNodeIndex);;
                    node.extra = rowNodeIndex;
                    node.columnNode.addNode(node);
                    addToRow(node);
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

            Iterator<Column> columns = mPuzzle.getColumns();
            while(columns.hasNext()) {
                Column column = columns.next();
                int columnIndex = column.getIndex();

                for(int value = 1; value <= 9; value++) {
                    int rowNodeIndex = calculateRowIndex(rowIndex,
                                                         columnIndex,
                                                         value);

                    int columnNodeIndex = columnIndex * dimension +
                                          (value - 1) +
                                          startColumnNodeIndex;
                    ColumnNode columnNode = getColumnNode(columnNodeIndex);

                    Node node = new Node();
                    node.columnNode = getColumnNode(columnNodeIndex);;
                    node.extra = rowNodeIndex;
                    node.columnNode.addNode(node);
                    addToRow(node);
                }
            }
        }

        return startColumnNodeIndex + dimension * 9;
    }

    private int createPentominoNumberNodes(final int startColumnIndex) {
        final int dimension = mPuzzle.getDimension();

        Iterator<Row> rows = mPuzzle.getRows();
        while(rows.hasNext()) {
            Row row = rows.next();
            int rowIndex = row.getIndex();

            Iterator<Column> columns = mPuzzle.getColumns();
            while(columns.hasNext()) {
                Column column = columns.next();
                int columnIndex = column.getIndex();

                for(int value = 1; value <= 9; value++) {
                    int rowNodeIndex = calculateRowIndex(rowIndex,
                                                         columnIndex,
                                                         value);
                    int columnNodeIndex =
                        mPuzzle.getPentomino(rowIndex, columnIndex).getIndex() * dimension +
                        value - 1;
                    ColumnNode columnNode = getColumnNode(columnNodeIndex);

                    Node node = new Node();
                    node.columnNode = getColumnNode(columnNodeIndex);;
                    node.extra = rowNodeIndex;
                    node.columnNode.addNode(node);
                    addToRow(node);
                }
            }
        }
        return startColumnIndex + mPuzzle.getNumOfPentominoes() * 9;
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
            mRowHeader.add(node);
        }
    }

    /**
     *
     *
     * @param args
     */
    public static void main(String[] args) {
        StandardPuzzle puzzle =
            new StandardPuzzle(
                new int[][] {{0, 2, 3, 4, 0, 0, 7, 0, 0},
                             {4, 5, 6, 0, 8, 9, 1, 0, 0},
                             {7, 8, 9, 1, 0, 3, 0, 0, 0},
                             {0, 2, 0, 4, 5, 0, 0, 8, 9},
                             {1, 2, 0, 0, 5, 6, 0, 8, 0},
                             {1, 2, 0, 4, 0, 0, 0, 8, 9},
                             {0, 2, 0, 4, 5, 6, 0, 0, 9},
                             {1, 2, 3, 0, 5, 0, 7, 0, 0},
                             {0, 0, 0, 4, 0, 6, 0, 0, 9}});
        Matrix matrix = new Matrix(puzzle);

        // Start to check if the dancing links matrix is valid

    }
}
