package testrestendpoints

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest(UserController::class)
class TestRestEndpoints {

    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    fun `smoke`() {
        // Given
        val username = "Steven"

        mvc.post("/users?username=$username")
            .andExpect {
                status { isOk() }
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