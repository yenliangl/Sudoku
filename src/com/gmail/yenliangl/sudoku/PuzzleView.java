package com.gmail.yenliangl.sudoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.animation.AnimationUtils;
import android.view.View;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.util.Log;

import java.util.*;

import com.gmail.yenliangl.sudoku.puzzle.*;

class PuzzleView extends View {
    // Debug tag
    private static final String TAG = "PUZZLE";

    //
    private final Game mGame;
    private Puzzle mPuzzle;

    // Constants
    private static final String VIEW_STATE = "VIEW_STATE";
    private static final String VIEW_STATE_SELECTED_TILE_ROW = "VS_SELECTED_TILE_ROW";
    private static final String VIEW_STATE_SELECTED_TILE_COL = "VS_SELECTED_TILE_COL";
    private static final int THICK_LINE_WIDTH = 4;
    private static final int PADDING = 5;

    private float mCellWidth = 0f;
    private float mCellHeight = 0f;
    private int mSelectedCellRow = 0;
    private int mSelectedCellCol = 0;

    public PuzzleView(Context context) {
        super(context);

        mGame = (Game)context;

        System.out.println("PuzzleView(context)");

        // Need to set this view focusable in order to receive key events.
        setLongClickable(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    public PuzzleView(Puzzle puzzle, Context context) {
        this(context);

        System.out.println("PuzzleView(puzzle,context)");

        mPuzzle = puzzle;
    }

    public void startNewPuzzle(Puzzle puzzle) {
        Log.d(TAG, "startNewPuzzle()");
        mPuzzle = puzzle;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mCellWidth = (w - 2 * PADDING) / 9f;
        mCellHeight = (h - 2 * PADDING) / 9f;

        Log.d(TAG, "Cell width=" + mCellWidth + ",height=" + mCellHeight);

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Fill bounding rectangle with defined background
        // R.color.puzzle_background in XML.
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.puzzle_background));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);

        // Draw grid lines. Every block boundary should be drawn
        // thicker than other lines.
        Paint darkPaint = new Paint();
        darkPaint.setColor(getResources().getColor(android.R.color.background_dark));
        darkPaint.setStyle(Paint.Style.STROKE);
        for(int i = 0; i < 10; i++) {
            // Draw thick line for every block's boundary
            if(i % 3 == 0) {
                darkPaint.setStrokeWidth(THICK_LINE_WIDTH);
            } else {
                darkPaint.setStrokeWidth(1);
            }

            float x1 = PADDING, x2 = getWidth() - PADDING;
            float y = PADDING + i * mCellHeight;
            canvas.drawLine(x1, y, x2, y, darkPaint );

            float y1 = PADDING, y2 = getHeight() - PADDING;
            float x = PADDING + i * mCellWidth;
            canvas.drawLine(x, y1, x, y2, darkPaint );
        }

        // Draw selected cell with different color
        Paint hilitePaint = new Paint();
        hilitePaint.setColor(getResources().getColor(R.color.puzzle_selected));
        hilitePaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(
            getSelectedCellRect(mSelectedCellRow, mSelectedCellCol),
            hilitePaint);

        // Draw non-empty cells
        Paint wordPaint = new Paint(Paint.ANTI_ALIAS_FLAG |
                                    Paint.SUBPIXEL_TEXT_FLAG);
        wordPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fm = wordPaint.getFontMetrics();
        float wordHalfHeight =
            (Math.abs(fm.descent) + Math.abs(fm.ascent)) / 2;

        if(mPuzzle != null) {
            Iterator<Row> rows = mPuzzle.getRows();
            while(rows.hasNext()) {
                Row row = rows.next();

                Iterator<Cell> cells = row.getCells();
                while(cells.hasNext()) {
                    Cell cell = cells.next();
                    int value = cell.getValue();
                    if(value != 0) {
                        Rect rect =
                            getSelectedCellRect(cell.getRowIndex(),
                                                cell.getColumnIndex());
                        float x = rect.centerX();
                        float y = rect.centerY() + wordHalfHeight;

                        canvas.drawText(Integer.toString(value),
                                        0, 1, x, y,
                                        wordPaint);
                        Log.d(TAG,
                              "Drawing cell(" + cell.getRowIndex() + "," +
                              cell.getColumnIndex() + ")");
                    }
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent e) {
        Log.d(TAG, "onKeyDown(" + keyCode + ")");

        switch(keyCode) {
        case KeyEvent.KEYCODE_DPAD_UP:
            selectCell(mSelectedCellRow-1, mSelectedCellCol);
            break;
        case KeyEvent.KEYCODE_DPAD_DOWN:
            selectCell(mSelectedCellRow+1, mSelectedCellCol);
            break;
        case KeyEvent.KEYCODE_DPAD_LEFT:
            selectCell(mSelectedCellRow, mSelectedCellCol-1);
            break;
        case KeyEvent.KEYCODE_DPAD_RIGHT:
            selectCell(mSelectedCellRow, mSelectedCellCol+1);
            break;
        case KeyEvent.KEYCODE_1:
            drawNumberOnCell(mSelectedCellRow, mSelectedCellCol, 1);
            break;
        case KeyEvent.KEYCODE_2:
            drawNumberOnCell(mSelectedCellRow, mSelectedCellCol, 2);
            break;
        case KeyEvent.KEYCODE_3:
            drawNumberOnCell(mSelectedCellRow, mSelectedCellCol, 3);
            break;
        case KeyEvent.KEYCODE_4:
            drawNumberOnCell(mSelectedCellRow, mSelectedCellCol, 4);
            break;
        case KeyEvent.KEYCODE_5:
            drawNumberOnCell(mSelectedCellRow, mSelectedCellCol, 5);
            break;
        case KeyEvent.KEYCODE_6:
            drawNumberOnCell(mSelectedCellRow, mSelectedCellCol, 6);
            break;
        case KeyEvent.KEYCODE_7:
            drawNumberOnCell(mSelectedCellRow, mSelectedCellCol, 7);
            break;
        case KeyEvent.KEYCODE_8:
            drawNumberOnCell(mSelectedCellRow, mSelectedCellCol, 8);
            break;
        case KeyEvent.KEYCODE_9:
            drawNumberOnCell(mSelectedCellRow, mSelectedCellCol, 9);
            break;

            // Press s to shake the PuzzleView.
        case KeyEvent.KEYCODE_S:
            startAnimation(
                AnimationUtils.loadAnimation(mGame, R.anim.shake));
            break;

        default:
            return false;
        }

        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        Log.d(TAG, "onTouchEvent(" + e.getAction() + ") at (" +
              e.getX() + ", " + e.getY() + ")");

        float x = e.getX(), y = e.getY();
        int row = (int)((y - PADDING) / mCellHeight);
        int col = (int)((x - PADDING) / mCellWidth);

        selectCell(row, col);
        return true;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Log.d(TAG, "onSaveInstanceState()");

        Parcelable p = super.onSaveInstanceState();
        Bundle bundle = new Bundle();
        bundle.putInt(VIEW_STATE_SELECTED_TILE_ROW, mSelectedCellRow);
        bundle.putInt(VIEW_STATE_SELECTED_TILE_COL, mSelectedCellCol);
        bundle.putParcelable(VIEW_STATE, p);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle)state;
        int row = bundle.getInt(VIEW_STATE_SELECTED_TILE_ROW),
            col = bundle.getInt(VIEW_STATE_SELECTED_TILE_COL);
        selectCell(row, col);

        super.onRestoreInstanceState(bundle.getParcelable(VIEW_STATE));
    }

    private void selectCell(int row, int col) {
        if (row < 0 || col < 0 || row > 8 || col > 8) {
            return;
        }

        Log.d(TAG, "Select cell (" + mSelectedCellRow + ", " + mSelectedCellCol + ") -> (" + row + ", " + col + ")" );

        invalidate(getSelectedCellRect(mSelectedCellRow,
                                       mSelectedCellCol));

        mSelectedCellRow = row;
        mSelectedCellCol = col;
        invalidate(getSelectedCellRect(mSelectedCellRow,
                                       mSelectedCellCol));
    }

    private Rect getSelectedCellRect(int row, int col) {
        float left = PADDING + col * mCellWidth,
               top = PADDING + row * mCellHeight;
        return new Rect((int)left, (int)top,
                        (int)(left + mCellWidth), (int)(top + mCellHeight));
    }

    private void drawNumberOnCell(int row, int col, int number) {
        if(number < 1 || number > 9) {
            return;
        }

        Log.d(TAG, "Draw " + number + " on (" + row + ", " + col + ")");
        Cell cell = mPuzzle.getCell(row, col);
        cell.setValue(number);
        invalidate(getSelectedCellRect(row, col));
    }
}
