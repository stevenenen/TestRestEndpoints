package testrestendpoints

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(UserController::class)
class TestRestEndpoints {

    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    fun `with object mapper`() {
        // Given
        val username = "Steven"
        val mapper = ObjectMapper()

        mvc.perform(
            post("/mockmvc/validate")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(UserController.PostUserRequest("Steven")))
        )
            .andExpect {
                status().isOk
            }

        mvc.get("/users/$username")
            .andExpect {
                status { isOk() }
                content {
                    jsonPath("$.id") { exists() }
                    jsonPath("$.username") { value(username) }
                }
            }
    }
}