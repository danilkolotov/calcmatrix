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
        if (matrix.size() == 1) {
            return matrix.get(0).get(0);
        }
        double result = 0;
        for (int i = 0; i < matrix.size(); i++) {
            List<List<Double>> current = new ArrayList<>();
            for (int j = 0; j < matrix.size(); j++) {
                if (i == j) continue;
                current.add(matrix.get(j).subList(1, matrix.size()));
            }
            result += matrix.get(i).get(0) * determinantImpl(current);
        }
        return result;
    }

    public static List<Double> power(Query query) {
        List<Double> matrix = query.getMatrix();
        int n = query.getN();
        return powerImpl(toSquare(matrix, n), query.getPower()).stream().flatMap(Collection::stream).toList();
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
                if (i == j) {
                    current.add(1.0);
                } else {
                    current.add(0.0);
                }
            }
            result.add(current);
        }
        return result;
    }

}
