package hw2;                       

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int sitesOpen = 0;
    private int N;
    private boolean[] siteStatus;
    private WeightedQuickUnionUF percSet;
    private WeightedQuickUnionUF fullSet;
    private int percVT;
    private int percVB;
    private int fullVT;

    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N must be greater than 0");
        }
        this.N = N;
        siteStatus = new boolean[N * N];
        fullSet = new WeightedQuickUnionUF(N * N + 1);
        percSet = new WeightedQuickUnionUF(N * N + 2);
        fullVT = N * N;
        percVT = N * N;
        percVB = N * N + 1;
    }

    private boolean checkInBounds(int x, int y) {
        if ((x >= N) || (y >= N)) {
            throw new IndexOutOfBoundsException("Given column or row is outside the N x N grid");
        }
        return true;
    }

    /* Cnnverts 2D coordinates to 1D index */
    private int xyTo1D(int x, int y) {
        return x * N + y;
    }

    // todo: four if statements to open; connect to vT/vB for top/bot opens
    /* Opens site at row, col if site is not open already */
    public void open(int row, int col) {
        checkInBounds(row, col);
        if (!isOpen(row, col)) {
            int oneDim = xyTo1D(row, col);
            siteStatus[oneDim] = true;
            sitesOpen += 1;

            // Top edge
            if (row == 0) {
                percSet.union(percVT, oneDim);
                fullSet.union(fullVT, oneDim);
            }

            // Bottom edge
            if (row == N - 1) {
                percSet.union(percVB, oneDim);
            }

            // Neighboring cells
            if (row != 0) {
                if (isOpen(row - 1, col)) {
                    percSet.union(oneDim, xyTo1D(row - 1, col));
                    fullSet.union(oneDim, xyTo1D(row - 1, col));
                }
            }

            if (col != 0) {
                if (isOpen(row, col - 1)) {
                    percSet.union(oneDim, xyTo1D(row, col - 1));
                    fullSet.union(oneDim, xyTo1D(row, col - 1));
                }
            }

            if (row != N - 1) {
                if (isOpen(row + 1, col)) {
                    percSet.union(oneDim, xyTo1D(row + 1, col));
                    fullSet.union(oneDim, xyTo1D(row + 1, col));
                }
            }

            if (col != N - 1) {
                if (isOpen(row, col + 1)) {
                    percSet.union(oneDim, xyTo1D(row, col + 1));
                    fullSet.union(oneDim, xyTo1D(row, col + 1));
                }
            }
        }
    }

    /* Returns true if the site at row, col is open */
    public boolean isOpen(int row, int col) {
        checkInBounds(row, col);
        return siteStatus[xyTo1D(row, col)];
    }

    /* Returns true if the site at row, col is full */
    public boolean isFull(int row, int col) {
        checkInBounds(row, col);
        int oneDim = xyTo1D(row, col);
        return fullSet.connected(oneDim, fullVT);
    }

    /* Returns number of open sites */
    public int numberOfOpenSites() {
        return sitesOpen;
    }

    /* Returns true if the system percolates: a bottom row site is connected to a top row site */
    public boolean percolates() {
        return percSet.connected(percVB, percVT);
    }

    public static void main(String[] args) {
    }
}                       
