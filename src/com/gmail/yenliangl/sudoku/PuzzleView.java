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

class PuzzleView extends View {
    private static final String TAG = "PUZZLE";

    //
    private final Game mGame;

    // View state key
    private static final String VIEW_STATE = "VIEW_STATE";
    private static final String VIEW_STATE_SELECTED_TILE_ROW = "VS_SELECTED_TILE_ROW";
    private static final String VIEW_STATE_SELECTED_TILE_COL = "VS_SELECTED_TILE_COL";

    private int[] puzzle = new int[9*9];

    //
    private float mTileWidth = 0f;
    private float mTileHeight = 0f;
    private int mSelectedTileRow = 0, mSelectedTileCol = 0;
    private static final int PADDING = 5;

    // Should pass the answer of the puzzle into this view for drawing.
    public PuzzleView(Context context) {
        super(context);

        mGame = (Game)context;

        // Need to set this view focusable in order to receive key events.
        setLongClickable(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mTileWidth = (w - 2 * PADDING) / 9f;
        mTileHeight = (h - 2 * PADDING) / 9f;

        Log.d(TAG, "Tile width=" + mTileWidth + ", tile height=" + mTileHeight);

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Fill bounding rectangle with defined background in XML.
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.puzzle_background));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);

        // Draw grid lines
        Paint darkPaint = new Paint();
        darkPaint.setColor(
            getResources().getColor(android.R.color.background_dark));
        darkPaint.setStyle(Paint.Style.STROKE);
        for(int i = 0; i < 10; i++) {
            if(i % 3 == 0) { // draw thick line
                darkPaint.setStrokeWidth(2);
            } else {
                darkPaint.setStrokeWidth(1);
            }

            float x1 = PADDING, x2 = getWidth() - PADDING;
            float y = PADDING + i * mTileHeight;
            canvas.drawLine(x1, y, x2, y, darkPaint );

            float y1 = PADDING, y2 = getHeight() - PADDING;
            float x = PADDING + i * mTileWidth;
            canvas.drawLine(x, y1, x, y2, darkPaint );
        }

        // Paint current selected tile...
        Paint selected = new Paint();
        selected.setColor(getResources().getColor(R.color.puzzle_selected));
        selected.setStyle(Paint.Style.FILL);
        canvas.drawRect(
            getSelectedTileRect(mSelectedTileRow, mSelectedTileCol),
            selected);

        // Draw tiles with values other than zero.
        // @todo: choose hintPaint when hinting is turned on.
        Paint wordPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.SUBPIXEL_TEXT_FLAG);
        wordPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fm = wordPaint.getFontMetrics();
        float wordHalfHeight =
            (Math.abs(fm.descent) + Math.abs(fm.ascent)) / 2;
        for(int r = 0; r < 9; r++) {
            for(int c = 0; c < 9; c++) {
                int index = r * 9 + c;
                if(puzzle[index] != 0) {
                    Rect rect = getSelectedTileRect(r,c);
                    float x = rect.centerX(),
                          y = rect.centerY() + wordHalfHeight;
                    canvas.drawText(Integer.toString(puzzle[index]),
                                    0, 1, x, y,
                                    wordPaint);
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent e) {
        Log.d(TAG, "onKeyDown(" + keyCode + ")");

        switch(keyCode) {
        case KeyEvent.KEYCODE_DPAD_UP:
            selectTile(mSelectedTileRow-1, mSelectedTileCol);
            break;
        case KeyEvent.KEYCODE_DPAD_DOWN:
            selectTile(mSelectedTileRow+1, mSelectedTileCol);
            break;
        case KeyEvent.KEYCODE_DPAD_LEFT:
            selectTile(mSelectedTileRow, mSelectedTileCol-1);
            break;
        case KeyEvent.KEYCODE_DPAD_RIGHT:
            selectTile(mSelectedTileRow, mSelectedTileCol+1);
            break;
        case KeyEvent.KEYCODE_1:
            drawNumberOnTile(mSelectedTileRow, mSelectedTileCol, 1);
            break;
        case KeyEvent.KEYCODE_2:
            drawNumberOnTile(mSelectedTileRow, mSelectedTileCol, 2);
            break;
        case KeyEvent.KEYCODE_3:
            drawNumberOnTile(mSelectedTileRow, mSelectedTileCol, 3);
            break;
        case KeyEvent.KEYCODE_4:
            drawNumberOnTile(mSelectedTileRow, mSelectedTileCol, 4);
            break;
        case KeyEvent.KEYCODE_5:
            drawNumberOnTile(mSelectedTileRow, mSelectedTileCol, 5);
            break;
        case KeyEvent.KEYCODE_6:
            drawNumberOnTile(mSelectedTileRow, mSelectedTileCol, 6);
            break;
        case KeyEvent.KEYCODE_7:
            drawNumberOnTile(mSelectedTileRow, mSelectedTileCol, 7);
            break;
        case KeyEvent.KEYCODE_8:
            drawNumberOnTile(mSelectedTileRow, mSelectedTileCol, 8);
            break;
        case KeyEvent.KEYCODE_9:
            drawNumberOnTile(mSelectedTileRow, mSelectedTileCol, 9);
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
        Log.d(TAG,
              "onTouchEvent(" + e.getAction() + ") at (" +
              e.getX() + ", " + e.getY() + ")");

        float x = e.getX(), y = e.getY();
        int row = (int)((y - PADDING) / mTileHeight);
        int col = (int)((x - PADDING) / mTileWidth);

        selectTile(row, col);
        return true;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable p = super.onSaveInstanceState();
        Log.d(TAG, "onSaveInstanceState()");
        Bundle bundle = new Bundle();
        bundle.putInt(VIEW_STATE_SELECTED_TILE_ROW, mSelectedTileRow);
        bundle.putInt(VIEW_STATE_SELECTED_TILE_COL, mSelectedTileCol);
        bundle.putParcelable(VIEW_STATE, p);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle)state;
        int row = bundle.getInt(VIEW_STATE_SELECTED_TILE_ROW),
            col = bundle.getInt(VIEW_STATE_SELECTED_TILE_COL);
        selectTile(row, col);

        super.onRestoreInstanceState(bundle.getParcelable(VIEW_STATE));
    }

    // Double-buffering
    private void selectTile(int row, int col) {
        if (row < 0 || col < 0 || row > 8 || col > 8) {
            return;
        }

        Log.d(TAG, "Select tile (" + mSelectedTileRow + ", " + mSelectedTileCol + ") -> (" + row + ", " + col + ")" );

        invalidate(getSelectedTileRect(mSelectedTileRow,
                                       mSelectedTileCol));

        mSelectedTileRow = row;
        mSelectedTileCol = col;
        invalidate(getSelectedTileRect(mSelectedTileRow,
                                       mSelectedTileCol));
    }

    private Rect getSelectedTileRect(int row, int col) {
        float left = PADDING + col * mTileWidth,
               top = PADDING + row * mTileHeight;
        return new Rect((int)left, (int)top,
                        (int)(left + mTileWidth), (int)(top + mTileHeight));
    }

    private void drawNumberOnTile(int row, int col, int number) {
        if(number < 1 || number > 9) {
            return;
        }

        int index = row * 9 + col;
        Log.d(TAG, "Draw " + number + "on (" + row + ", " + col + ")");
        puzzle[index] = number;
        invalidate(getSelectedTileRect(row, col));
    }
}
