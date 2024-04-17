package calcmatrix;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CalcmatrixApplicationTests {
    @Autowired
    TestRestTemplate restTemplate;
    
    @Test
    void getExistingTest() {
        ResponseEntity<Query> response = restTemplate.getForEntity("/get/1228", Query.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Query result = response.getBody();

        assertEquals(1228, result.getId());
        assertEquals(2, result.getN());
    }
}
