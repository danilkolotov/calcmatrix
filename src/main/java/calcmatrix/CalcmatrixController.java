package calcmatrix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

import static calcmatrix.MatrixMath.determinant;
import static calcmatrix.MatrixMath.power;

@Controller
@RequestMapping(path = "/")
public class CalcmatrixController {
    @Autowired
    private QueryRepository repository;

    @PostMapping(path = "/new")
    private @ResponseBody ResponseEntity<Void> newQuery(@RequestBody Query query, UriComponentsBuilder ucb) {
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
//        return ResponseEntity.created(
//                repository.save(query).getId());
        return ResponseEntity.created(ucb
                .path("/get/{id}")
                .buildAndExpand(repository.save(query).getId())
                .toUri()
        ).build();
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
