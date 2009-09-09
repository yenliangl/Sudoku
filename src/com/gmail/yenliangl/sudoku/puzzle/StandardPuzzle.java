package com.gmail.yenliangl.sudoku.puzzle;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Iterator;

public class StandardPuzzle extends Puzzle {
    public StandardPuzzle(final int dimension) {
        super(dimension);
    }
    public StandardPuzzle(int[][] array) {
        super(array);
    }

    @Override
    protected void createPentominoes(ArrayList<Pentomino> pentominoes,
                                     final int dimension) {
        pentominoes.ensureCapacity(dimension);
        for(int i = 0; i < dimension; i++) {
            pentominoes.add(new Pentomino(i));
        }

        final int N = (int)Math.sqrt((double)dimension);
        Iterator<Cell> allCells = getCells();
        while(allCells.hasNext()) {
            Cell cell = allCells.next();

            int row = cell.getRowIndex();
            int column = cell.getColumnIndex();
            int pRow = row / N;
            int pColumn = column / N;
            int pIndex = pRow * N + pColumn;
            pentominoes.get(pIndex).addCell(cell);
        }
    }

    @Override
    public Pentomino getPentomino(int rowIndex, final int columnIndex) {
        int N = (int)Math.sqrt((double)getNumOfPentominoes());
        return getPentomino(rowIndex / N + columnIndex / N);
    }

    public static void main(String[] args) {
        // Make a 9x9 standard puzzle;
        StandardPuzzle puzzle = new StandardPuzzle(9);
        System.out.format("Standard puzzle has 9 rows? %s\n",
                          (puzzle.getNumOfRows() == 9));
        System.out.format("Standard puzzle has 9 columns? %s\n",
                          (puzzle.getNumOfColumns() == 9));
        System.out.format("Standard puzzle has 9 pentominoes? %s\n",
                          puzzle.getNumOfPentominoes() == 9);
        System.out.format("Standard puzzle has 81 columns? %s\n",
                          (puzzle.getNumOfCells() == 81));
        System.out.format("Standard puzzle has dimension 9? %s\n",
                          puzzle.getDimension() == 9);

        // Iterate rows
        Iterator<Row> rows = puzzle.getRows();
        while(rows.hasNext()) {
            Row row = rows.next();
            System.out.format("%2d ", row.getIndex());
            Iterator<Cell> cells = row.getCells();
            while(cells.hasNext()) {
                Cell cell = cells.next();
                System.out.format("%2d ", cell.getValue());
            }
            System.out.println("");
        }

        // Iterator pentominoes
        Iterator<Pentomino> pentominoes = puzzle.getPentominoes();
        while(pentominoes.hasNext()) {
            Pentomino p = pentominoes.next();
            System.out.format("Pentomino %d: ", p.getIndex());

            Iterator<Cell> cells = p.getCells();
            while(cells.hasNext()) {
                Cell cell = cells.next();
                System.out.format("(%d %d) ",
                                  cell.getRowIndex(),
                                  cell.getColumnIndex());
            }
            System.out.println("");
        }
    }
}
