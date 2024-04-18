package calcmatrix;

import org.hibernate.query.results.Builders;
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

import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class CalcmatrixApplicationTests {
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void getExistingTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/get/2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.n").value(2))
                .andExpect(jsonPath("$.operation").value("PWR"));
    }

    @Test
    void notFoundTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/get/0"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void createTest() throws Exception {
        String createdURI = mockMvc.perform(MockMvcRequestBuilders
                        .post("/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"matrix\":[1.0, 2.0, 3.0, 4.0], \"operation\": \"DET\", \"n\": 2}")
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn().getResponse().getHeader("Location");
        mockMvc.perform(MockMvcRequestBuilders.get(createdURI))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.operation").value("DET"))
                .andExpect(jsonPath("$.n").value(2))
                .andExpect(jsonPath("$.result").value(-2))
                .andExpect(jsonPath("$.matrix").isArray())
                .andExpect(jsonPath("$.matrix").value(contains(1.0, 2.0, 3.0, 4.0)));
    }


}
