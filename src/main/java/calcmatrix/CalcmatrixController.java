package calcmatrix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static calcmatrix.MatrixMath.determinant;
import static calcmatrix.MatrixMath.power;

@Controller
@RequestMapping(path = "/")
public class CalcmatrixController {
    @Autowired
    private QueryRepository repository;

    @PostMapping(path = "/new")
    private @ResponseBody ResponseEntity<Long> newQuery(@RequestBody Query query) {
        if (query.getOperation() == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            switch (query.getOperation()) {
                case DET -> query.setResult(determinant(query));
                case PWR -> query.setPowerResult(power(query));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repository.save(query).getId());
    }

    @GetMapping(path = "/getall")
    private @ResponseBody Iterable<Query> getAll() {
        return repository.findAll();
    }

    @GetMapping(path = "/get/{id}")
    private @ResponseBody ResponseEntity<Query> getById(@PathVariable Long id) {
        Optional<Query> result = repository.findById(id);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
