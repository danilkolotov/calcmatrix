package calcmatrix;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
    void createExisting() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1}")
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createBad() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
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
