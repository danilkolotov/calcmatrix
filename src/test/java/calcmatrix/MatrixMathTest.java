package calcmatrix;

import org.junit.jupiter.api.Test;

import java.util.List;

import static calcmatrix.MatrixMath.determinant;
import static calcmatrix.MatrixMath.power;
import static org.junit.jupiter.api.Assertions.*;

public class MatrixMathTest {
    private static final List<Double> matrix = List.of(1.0, 2.0, 3.0, 4.0);

    private static Query make(int n) {
        Query result = new Query();
        result.setMatrix(matrix);
        result.setN(2);
        result.setPower(n);
        return result;
    }

    private static Query make(List<Double> matrix, int n) {
        Query result = new Query();
        result.setMatrix(matrix);
        result.setN(2);
        result.setPower(n);
        return result;
    }


    @Test
    public void powTest() {
        assertIterableEquals(List.of(1.0, 0.0, 0.0, 1.0), power(make(0)));
        assertIterableEquals(List.of(1.0, 2.0, 3.0, 4.0), power(make(1)));
        assertIterableEquals(List.of(7.0, 10.0, 15.0, 22.0), power(make(2)));
    }

    @Test
    public void invalid() {
        assertThrows(IllegalArgumentException.class, () -> power(make(List.of(1.0, 1.0), 1)));
    }

    @Test
    public void detTest() {
        assertEquals(-2, determinant(make(-1)));
        assertEquals(-24, determinant(make(List.of(1.0, 4.0, 8.0, 8.0), -1)));
    }
}
