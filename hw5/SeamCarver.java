import edu.princeton.cs.algs4.Picture;

import java.awt.Color;


/**
 * Created by XWEN on 4/23/2017.
 */
public class SeamCarver {
    private Picture pic;
    private double[][] energyPaths;
    private int[][] xPaths;

    public SeamCarver(Picture picture) {
        pic = new Picture(picture);
    }

    // Current picture
    public Picture picture() {
        return new Picture(pic);
    }

    // Width of current picture
    public int width() {
        return pic.width();
    }

    // Height of current picture
    public int height() {
        return pic.height();
    }

    // Energy of pixel at column x, row y
    public double energy(int x, int y) {
        // Getting horizontal
        Color adjLeft = pic.get(Math.floorMod(x - 1, width()), y);
        Color adjRight = pic.get(Math.floorMod(x + 1, width()), y);
        double xCentralRed = Math.abs(adjLeft.getRed() - adjRight.getRed());
        double xCentralGreen = Math.abs(adjLeft.getGreen() - adjRight.getGreen());
        double xCentralBlue = Math.abs(adjLeft.getBlue() - adjRight.getBlue());
        double xGrad = xCentralRed * xCentralRed
                + xCentralGreen * xCentralGreen
                + xCentralBlue * xCentralBlue;

        // Getting vertical
        Color adjUp = pic.get(x, Math.floorMod(y - 1, height()));
        Color adjDown = pic.get(x, Math.floorMod(y + 1, height()));
        double yCentralRed = Math.abs(adjUp.getRed() - adjDown.getRed());
        double yCentralGreen = Math.abs(adjUp.getGreen() - adjDown.getGreen());
        double yCentralBlue = Math.abs(adjUp.getBlue() - adjDown.getBlue());
        double yGrad = yCentralRed * yCentralRed
                + yCentralGreen * yCentralGreen
                + yCentralBlue * yCentralBlue;

        return xGrad + yGrad;
    }

    // Sequence of indices for horizontal seam with minimum energy
    public int[] findHorizontalSeam() {
        transpose();
        int[] seam = findVerticalSeam();
        transpose();
        return seam;
    }

    // Sequence of indices for vertical seam with minimum energy
    public int[] findVerticalSeam() {
        xPaths = new int[height()][width()];
        energyPaths = new double[height()][width()];

        for (int r = 0; r < height(); r++) {
            for (int c = 0; c < width(); c++) {
                energyPaths[r][c] = Double.POSITIVE_INFINITY;
            }
        }

        for (int r = 0; r < height(); r++) {
            for (int c = 0; c < width(); c++) {
                relaxMin(r, c);
            }
        }

        double minEnergy = Double.POSITIVE_INFINITY;
        int lastX = -1;
        for (int c = 0; c < width(); c++) {
            if (energyPaths[height() - 1][c] < minEnergy) {
                lastX = c;
                minEnergy = energyPaths[height() - 1][c];
            }
        }

        if (lastX == -1) {
            return null;
        }

        int[] seam = new int[height()];
        seam[height() - 1] = lastX;
        int pathTo = xPaths[height() - 1][lastX];
        for (int r = height() - 2; r >= 0; r--) {
            seam[r] = pathTo;
            pathTo = xPaths[r][pathTo];
        }

        return seam;
    }

    private void relaxMin(int y, int x) {
        if (y == 0) {
            energyPaths[0][x] = energy(x, y);
        } else {
            double minEnergy = energyPaths[y][x];
            for (int i = x - 1; i <= x + 1; i++) {
                if (i >= 0 && i < width()) {
                    if (minEnergy > energyPaths[y - 1][i] + energy(x, y)) {
                        minEnergy = energyPaths[y - 1][i] + energy(x, y);
                        energyPaths[y][x] = minEnergy;
                        xPaths[y][x] = i;
                    }
                }
            }
        }
    }

    // Remove horizontal seam from picture
    public void removeHorizontalSeam(int[] seam) {

    }

    // Remove vertical seam from picture
    public void removeVerticalSeam(int[] seam) {

    }

    // Flips picture for finding horizontal seam
    private void transpose() {
        Picture old = pic;
        Picture transposed = new Picture(old.height(), old.width());
        for (int i = 0; i < transposed.height(); i++) {
            for (int j = 0; j < transposed.width(); j++) {
                transposed.set(j, i, old.get(i, j));
            }
        }
        pic = transposed;
    }

}
