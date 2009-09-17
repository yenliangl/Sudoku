package com.gmail.yenliangl.sudoku.generator;

import com.gmail.yenliangl.sudoku.puzzle.*;

interface Generator {
    Puzzle generate(int level);
}
