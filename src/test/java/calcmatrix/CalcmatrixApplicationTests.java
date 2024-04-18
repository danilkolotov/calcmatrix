package calcmatrix;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class CalcmatrixApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getExistingTest() throws Exception {
        mockMvc.perform(get("/get/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.n").value(2))
                .andExpect(jsonPath("$.operation").value("PWR"));
    }

    @Test
    void notFoundTest() throws Exception {
        mockMvc.perform(get("/get/0"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllTest() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(
                    content().json(
                        "[{\"n\": 2,\"operation\": \"DET\"}," +
                        "{\"n\": 2,\"operation\": \"PWR\"}," +
                        "{\"n\": 2,\"operation\": \"PWR\"}]"
                    )
                );
    }

    @Test
    @DirtiesContext
    void createTest() throws Exception {
        String createdURI = mockMvc.perform(post("/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"matrix\":[1.0, 2.0, 3.0, 4.0], \"operation\": \"DET\", \"n\": 2}")
                )
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn().getResponse().getHeader("Location");
        mockMvc.perform(get(createdURI))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.operation").value("DET"))
                .andExpect(jsonPath("$.n").value(2))
                .andExpect(jsonPath("$.result").value(-2))
                .andExpect(jsonPath("$.matrix").isArray())
                .andExpect(jsonPath("$.matrix").value(contains(1.0, 2.0, 3.0, 4.0)));
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4));
    }

    @Test
    void createExisting() throws Exception {
        mockMvc.perform(post("/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1}")
                )
                .andExpect(status().isBadRequest()
        );
    }

    @Test
    void createBad() throws Exception {
        mockMvc.perform(post("/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                )
                .andExpect(status().isBadRequest()
        );
    }
}
