package calcmatrix;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Setter
public class Query {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    private int power = 0;

    @ElementCollection
    private List<Double> powerResult;

    private Double result;
}
