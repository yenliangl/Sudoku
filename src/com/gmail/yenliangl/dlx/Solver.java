package com.gmail.yenliangl.dlx;

import java.util.ArrayList;
import java.util.Stack;

public class Solver {
    public enum Result { FAILED, SUCCESSFUL };

    public Solver() {
    }

    public void solve(int[] puzzle, int N) {
        Structure dlxStructure = createDlxStructure(puzzle, N);
        Stack<Node> solution;
        solve(dlxStructure, 0, solution);
    }

    protected abstract void onSolved(Stack<Node> solution);
    protected abstract void onUnsolved();
    protected abstract void onCoveredColumn(ColumnNode c);
    protected abstract void onUncoveredColumn(ColumnNode c);

    private void solve(Structure dlxStructure, int k, Stack<Node> solution) {
        ColumnNode h = dlxStructure.getRootColumnNode();
        if(h.right == h) {
            onSolved(solution);
            return;
        }

        ColumnNode c = getLowestNumberColumnNode(dlxStructure.getRootColumnNode());
        coverColumn(c);

        for(Node r = c.down; r != c; r = r.down) {
            solution.push(r);
            for(Node j = r.right; j != r; j = j.right) {
                coverColumn(j);
            }
            solve(dlxStructure, k+1, solution);

            r = solution.pop();
            c = r.columnNode;
            for(Node j = r.left; j != r; j = j.left) {
                uncoverColumn(j.columnNode);
            }
        }
        uncoverColumn(c);

        onUnsolved();
    }

    private Structure createDlxStructure(final int[] puzzle, final int N) {
        Structure dlxStructure = new Structure(        );







        return dlxStructure;
    }

    private ColumnNode getLowestNumberColumnNode(ColumnNode h) {
        ColumnNode result;
        int s = Integer.MAX_VALUE;
        for(Node j = h.right; j != h; j = j.right) {
            if(j.length < s) {
                result = j;
                s = j.length;
            }
        }
        return result;
    }

    private void coverColumn(ColumnNode c) {
        c.right.left = c;
        c.left.right = c.right;

        for(Node i = c.down; i != c; i = i.down ) {
            for(Node j = i.left; j != i; j = j.right ) {
                j.down.up = j.up;
                j.up.down = j.down;
                j.columnNode.length--;
            }
        }

        onCoveredColumn(c);
    }

    private uncoverColumn(ColumnNode c) {
        for(Node i = c.up; i !=c; i = i.up) {
            for(Node j = i.left; j != i; j = j.left) {
                j.columnNode.length++;
                j.down.up = j;
                j.up.down = j;
            }
        }
        c.right.left = c;
        c.left.right = c;

        onUncoveredColumn(c);
    }
}
