package calcmatrix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(path = "/")
public class CalcmatrixController {
    @Autowired
    private QueryRepository repository;

    @PostMapping(path = "/new")
    private @ResponseBody Long newQuery(@RequestBody Query query) {
        query.setResult((double) 111);
        repository.save(query);
        return query.getId();
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
