package hw3.puzzle;

import java.util.LinkedList;

public class Board implements WorldState {
    private int[][] board;
    private int[][] goal;
    private int size;

    /* Creates a N by N board where tiles[i][j] correspond to row i, col j */
    public Board(int[][] tiles) {
        size = tiles.length;
        board = new int[size][size];
        goal = new int[size][size];

        int k = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = tiles[i][j];
                goal[i][j] = k;
                k += 1;
            }
        }
        goal[size - 1][size - 1] = 0;
    }

    /* Returns tile at row i, column j of the board */
    public int tileAt(int i, int j) {
        if ((i < 0) || (j < 0) || (i > size - 1) || (j > size - 1)) {
            throw new IndexOutOfBoundsException("i and j must be between 0 and size - 1");
        }
        return board[i][j];
    }

    public int size() {
        return size;
    }

    /* NOTE: Used staff solution */
    /* Returns an iterable of the next possible moves from a current Board */
    @Override
    public Iterable<WorldState> neighbors() {
        LinkedList<WorldState> neighbors = new LinkedList<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == 0) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = 0;
                    Board neighbor = new Board(ili1li1);
                    neighbors.add(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = 0;
                }
            }
        }
        return neighbors;
    }

    /* Returns number of tiles in the wrong position */
    public int hamming() {
        int h = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int num = tileAt(i, j);
                if ((num != 0) && (num != goal[i][j])) {
                    h += 1;
                }
            }
        }
        return h;
    }

    /* Returns expected row for a given number */
    private int computeRow(int num) {
        return (num - 1) / size;
    }

    /* Returns expected column for a given number */
    private int computeCol(int num) {
        return (num - 1) % size;
    }

    /* Returns sum of vertical and horizontal distances each
       tile is from their correct position */
    public int manhattan() {
        int m = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int num = tileAt(i, j);
                if ((num != 0) && (num != goal[i][j])) {
                    int expRow = computeRow(num);
                    int expCol = computeCol(num);
                    m += Math.abs(expRow - i);
                    m += Math.abs(expCol - j);
                }
            }
        }
        return m;
    }

    /* Returns estimated number of moves required to get to the goal */
    @Override
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    /* Returns true if this Board is equal to the goal */
    @Override
    public boolean isGoal() {
        return estimatedDistanceToGoal() == 0;
    }

    /* Returns true if the tiles of this board are in the same positions
       as that of the other board */

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        Board other = (Board) o;
        if (this.size() != other.size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (this.tileAt(i, j) != other.tileAt(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    /* Returns the string representation of the board. */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

    public static void main(String[] args) {
        int[][] t = {{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};
        Board b = new Board(t);
        System.out.println(b.manhattan());
    }
}
