package calcmatrix;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface QueryRepository extends CrudRepository<Query, Long>, PagingAndSortingRepository<Query, Long> {
}
