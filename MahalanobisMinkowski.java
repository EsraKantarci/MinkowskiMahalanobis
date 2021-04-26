package distance;

/**
 *
 * @author Selam Adım Esra
 */
public class MahalanobisMinkowski {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        double arr[][] = {
            {64.0, 580.0, 29.0},
            {66.0, 570.0, 33.0},
            {68.0, 590.0, 37.0},
            {69.0, 660.0, 46.0},
            {73.0, 600.0, 55.0}
        };
        /*
        // Reference: https://www.geeksforgeeks.org/multidimensional-arrays-in-java/
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                System.out.println("arr[" + i + "][" + j + "] = "
                        + arr[i][j]);
            }
        }

        double[] p1 = arr[0];
        double[] p2 = arr[1];
        for (int j = 0; j < p1.length; j++) {
            System.out.println(p1[j] + ",");
        }
        meanVector(arr);
        covariance(arr, arr[0], arr[1]);
        */
        
        int P1 = 0;
        int P2 = 1;
        int p = 2;
        System.out.println("Minkowski distance between " + P1 + 
                " and " + P2 + " is: " + calculateMinkowski(arr, 0, 1, 2) 
                + " (p = " + p + ").");
        calculateMahalanobis(arr, P1, P2);
        
    }

    static double calculateMinkowski(double arr[][], int P1, int P2, int R) {
        // I removed x[] and y[] in parameters, because I am already
        // holding respective columns and rows in the same 2d array arr.
       
        double[] x = arr[P1];
        double[] y = arr[P2];
        double distance = 0;

        for (int i = 0; i < x.length; i++) {
            distance += Math.pow(x[i] - y[i], R);
            // System.out.println(distance + " step " + i);
        }

        //System.out.println("distance: " + distance);
        distance = Math.pow(distance, 1.0 / R);
        return distance;
    }

    // https://stackoverflow.com/questions/49971823/how-to-find-the-average-of-every-column-in-a-2d-array
    public static double[] meanVector(double arr[][]) {

        double meanVal;
        double[] mean = new double[arr[0].length];
        for (int col = 0; col < arr[col].length; col++) {
            double sum = 0;
            for (int row = 0; row < arr.length; row++) {
                sum += arr[row][col];

                meanVal = sum / arr.length;
                mean[col] = meanVal;
            }

            // System.out.println("mean value of col " + col + " is " + mean[col]);
            // System.out.println();

        }
        return mean;
    }

    // https://github.com/mlnorman/Intro-To-Java-Programming/blob/master/Chp8/Exercise_08_06.java
    private static double[][] matrixMultiply(double[][] a, double[][] b) {
        double[][] matrix = new double[a.length][b[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                double sum = 0;
                for (int k = 0; k < a[i].length; k++) {
                    sum += a[i][k] * b[k][j];
                }
                matrix[i][j] = sum;
            }
        }

        return matrix;
    }

    static double[][] covariance(double arr[][], double x[], double y[]) {
        double[][] cov = {{0, 0}, {0, 0}};
        double[] mean = meanVector(arr);
        int n = arr.length;
        // System.out.println("n is " + n);

        double[][] subx = new double[x.length][1];
        double[][] suby = new double[y.length][1];

        for (int i = 0; i < x.length; i++) {
            subx[i][0] = x[i] - mean[i];
            // System.out.println("subx[" + i + "] : " + subx[i][0]);
        }
        for (int i = 0; i < y.length; i++) {
            suby[i][0] = y[i] - mean[i];
           // System.out.println("suby[" + i + "] : " + suby[i][0]);
        }

        double covxy = 0;
        double covxx = 0;
        double covyy = 0;
        for (int i = 0; i < subx.length; i++) {
            covxy += subx[i][0] * suby[i][0];
            covxx += subx[i][0] * subx[i][0];
            covyy += suby[i][0] * suby[i][0];
        }
        covxy = covxy / n;
        covxx = covxx / n;
        covyy = covyy / n;

        cov[0][0] = covxx;
        cov[0][1] = covxy;
        cov[1][0] = covxy;
        cov[1][1] = covyy;

        return cov;

    }

    static double[][] inverseCov(double[][] cov) {
        double det = cov[0][0] * cov[1][1] - cov[0][1] * cov[1][0];
        double[][] invCov = 
            {{(1 / det) * cov[1][1], (-1 / det) * cov[0][1]}, 
            {(1 / det) * cov[0][0], (-1 / det) * cov[1][0]}};
        return invCov;
    }

    static double calculateMahalanobis(double arr[][], int P1, int P2) {
        // I removed x[] and y[] again because
        // they are redundant as it was in Minkowski 
        double[] x = arr[P1];
        double[] y = arr[P2];
        double[][] distance = new double[0][0];

        double[] mean = meanVector(arr);
        double[][] sigma = covariance(arr, x, y);
        double[][] invSigma = inverseCov(sigma);
        //System.out.println("Inverse operation is completed");
        
        double[][] subx = new double[x.length][1];
        double[][] suby = new double[y.length][1];

        for (int i = 0; i < x.length; i++) {
            subx[i][0] = x[i] - mean[i];        }
        for (int i = 0; i < y.length; i++) {
            suby[i][0] = y[i] - mean[i];
        }
        distance = matrixMultiply(matrixMultiply(subx, invSigma), suby);;
        double distanceVal = Math.pow(Math.abs(distance[0][0]), 1.0 / 2);
        System.out.println("Mahalanobis distance value after "
                + "taking the root is: " + distanceVal);
        return distanceVal;
    }

}
