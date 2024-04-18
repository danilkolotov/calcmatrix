package calcmatrix;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CalcmatrixApplicationTests {
    @Autowired
    TestRestTemplate restTemplate;
    
    @Test
    void getExistingTest() {
        ResponseEntity<Query> response = restTemplate.getForEntity("/get/2", Query.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Query result = response.getBody();

        assertEquals(2, result.getId());
        assertEquals(2, result.getN());
    }

    @Test
    void notFoundTest() {
        ResponseEntity<Query> response = restTemplate.getForEntity("/get/0", Query.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void postTest() {
        Query query = new Query();
        query.setMatrix(List.of(1.0));
        query.setOperation(Query.Operation.DET);
        query.setN(1);
        ResponseEntity<Void> response = restTemplate.postForEntity("/new", query, Void.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ResponseEntity<Query> created = restTemplate.getForEntity(response.getHeaders().getLocation(), Query.class);
        assertEquals(HttpStatus.OK, created.getStatusCode());
        Query got = created.getBody();
        assertIterableEquals(query.getMatrix(), got.getMatrix());
        assertEquals(query.getOperation(), got.getOperation());
        assertEquals(1.0, got.getResult());
    }


}
