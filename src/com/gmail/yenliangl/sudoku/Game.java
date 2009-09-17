package com.gmail.yenliangl.sudoku;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.util.Log;

import com.gmail.yenliangl.sudoku.generator.*;


public class Game extends Activity {
    private static final String TAG = "SUDOKU";
    private PuzzleView mPuzzleView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        StandardSudokuGenerator generator = new StandardSudokuGenerator();

        mPuzzleView = new PuzzleView(generator.generate(50),
                                     this);
        setContentView(mPuzzleView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
        case R.id.menu_item_new_game:
            new AlertDialog.Builder(Game.this)
                .setTitle(R.string.level_dialog_title)
                .setItems(
                    R.array.level,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            Log.d(TAG, "Selected " + which);

                            // TODO: Generate a new game with selected
                            // difficulty level and tell PuzzleView to
                            // refresh
                        }
                    })
                .show();
            return true;

        case R.id.menu_item_preferences:
            Log.d(TAG, "Menu item - Preferences");
            startActivity(new Intent(this, Preferences.class));
            return true;

        case R.id.menu_item_help:
            Log.d(TAG, "Menu item - Help");

            new AlertDialog.Builder(Game.this)
                .setTitle(R.string.help_dialog_title)
                .setItems(
                    R.array.help,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            switch(which) {
                            case 0: // Rules
                                new AlertDialog.Builder(Game.this)
                                    .setTitle("Rules")
                                    .setMessage(R.string.rules_text)
                                    .show();
                                break;
                            case 1: // About
                                new AlertDialog.Builder(Game.this)
                                    .setTitle("About this application")
                                    .setMessage(R.string.about_text)
                                    .show();
                                break;
                            }
                        }
                    })
                .show();
            return true;
        }

        return false;
    }

    // private int[] generatePuzzle(int level) {
    // }
}
