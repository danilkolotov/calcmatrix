package calcmatrix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

import static calcmatrix.MatrixMath.determinant;
import static calcmatrix.MatrixMath.power;

@Controller
@RequestMapping(path = "/")
public class CalcmatrixController {
    private final QueryRepository repository;

    public CalcmatrixController(QueryRepository repository) {
        this.repository = repository;
    }

    @PostMapping(path = "/new")
    private @ResponseBody ResponseEntity<Void> newQuery(@RequestBody Query query, UriComponentsBuilder ucb) {
        if (query.getOperation() == null ||
                Optional.ofNullable(query.getId()).map(id -> repository.existsById(id)).orElse(false)) {
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
        return ResponseEntity.created(ucb
                .path("/get/{id}")
                .buildAndExpand(repository.save(query).getId())
                .toUri()
        ).build();
    }

    @GetMapping()
    private @ResponseBody ResponseEntity<Iterable<Query>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping(path = "/get/{id}")
    private @ResponseBody ResponseEntity<Query> getById(@PathVariable Long id) {
        Optional<Query> result = repository.findById(id);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(path="/error")
    private String error() {
        return "error";
    }

    @GetMapping(path="/index")
    private String index() {
        return "index";
    }
}
