package calcmatrix;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Data
public class Query {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private List<Double> matrix;

    private int n;

    public enum Operation {
        DET,
        PWR
    }

    @Enumerated(EnumType.ORDINAL)
    private Operation operation;

    private Integer power;

    @ElementCollection
    private List<Double> powerResult;

    private Double result;
}
