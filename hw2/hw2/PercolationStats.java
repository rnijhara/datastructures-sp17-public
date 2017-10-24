package hw2;
import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    private int N;
    private int T;
    private double[] fractions;
    private double mean;
    private double stddev;
    private double cLow;
    private double cHigh;

    public PercolationStats(int N, int T) {
        if ((N <= 0 || T <= 0)) {
            throw new IllegalArgumentException("N and T must be greater than zero.");
        }
        this.N = N;
        this.T = T;
        fractions = new double[T];
        for (int i = 0; i < T; i++) {
            Percolation perc = new Percolation(N);
            while (!perc.percolates()) {
                int x = StdRandom.uniform(0, N);
                int y = StdRandom.uniform(0, N);
                if (!perc.isOpen(x, y)) {
                    perc.open(x, y);
                }
            }
            fractions[i] = (double) perc.numberOfOpenSites() / (N * N);
        }
        double sqrtT = Math.sqrt(T);
        mean = StdStats.mean(fractions);
        stddev = StdStats.stddev(fractions);
        cLow = mean - (1.96 * stddev) / sqrtT;
        cHigh = mean + (1.96 * stddev) / sqrtT;
    }

    public double mean() {
        return mean;
    }

    public double stddev() {
        return stddev;
    }

    public double confidenceLow() {
        return cLow;
    }

    public double confidenceHigh() {
        return cHigh;
    }

    public static void main(String[] args) {
        PercolationStats ps = new PercolationStats(10, 10);
        System.out.println("hello");
    }
}                       
