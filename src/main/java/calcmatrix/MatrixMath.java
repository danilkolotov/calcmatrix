package calcmatrix;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MatrixMath {
    private static List<List<Double>> toSquare(List<Double> matrix, int n) {
        if (n * n != matrix.size()) {
            throw new IllegalArgumentException();
        }
        List<List<Double>> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            result.add(matrix.subList(i * n, i * n + n));
        }
        return result;
    }

    public static Double determinant(Query query) {
        List<Double> matrix = query.getMatrix();
        int n = query.getN();
        return determinantImpl(toSquare(matrix, n));
    }

    private static double determinantImpl(List<List<Double>> matrix) {
        List<List<Double>> triangle = new ArrayList<>();
        for (List<Double> row : matrix) {
            triangle.add(new ArrayList<>(row));
        }
        int n = matrix.size();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double mult = -triangle.get(j).get(i) / triangle.get(i).get(i);
                for (int k = i; k < n; k++) {
                    triangle.get(j).set(k, triangle.get(j).get(k) + mult * triangle.get(i).get(k));
                }
            }
        }
        double result = 1;
        for (int i = 0; i < n; i++) {
            result *= triangle.get(i).get(i);
        }
        return result;
    }

    public static List<Double> power(Query query) {
        List<Double> matrix = query.getMatrix();
        return powerImpl(toSquare(matrix, query.getN()), query.getPower()).stream().flatMap(Collection::stream).toList();
    }

    private static List<List<Double>> powerImpl(List<List<Double>> matrix, int power) {
        if (power == 0) {
            return identityMatrix(matrix.size());
        }
        List<List<Double>> result = powerImpl(matrix, power / 2);
        result = multiply(result, result);
        if (power % 2 == 1) {
            return multiply(result, matrix);
        }
        return result;
    }

    private static List<List<Double>> multiply(List<List<Double>> a, List<List<Double>> b) {
        int n = a.size();
        List<List<Double>> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            result.add(new ArrayList<>());
            for (int j = 0; j < n; j++) {
                result.getLast().add(0.0);
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    result.get(i).set(j, result.get(i).get(j) + a.get(i).get(k) * b.get(k).get(j));
                }
            }
        }
        return result;
    }

    private static List<List<Double>> identityMatrix(int n) {
        List<List<Double>> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<Double> current = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                current.add(i == j ? 1.0 : 0.0);
            }
            result.add(current);
        }
        return result;
    }

}
