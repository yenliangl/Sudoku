package com.gmail.yenliangl.sudoku.generator;

interface Generator {
    interface Listener {
        void onBegin();
        void onGenerated();
        void onEnd();
    }

    boolean generate(int[][] result);
}
