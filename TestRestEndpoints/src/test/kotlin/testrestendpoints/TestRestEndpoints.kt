package testrestendpoints

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import testrestendpoints.UserController.GetUserResponse

@WebMvcTest(UserController::class)
class TestRestEndpoints {

    @Autowired
    private lateinit var mvc: MockMvc

    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setup() {
        val kotlinModule = KotlinModule
            .Builder()
            .build()

        objectMapper = ObjectMapper().registerModule(kotlinModule)
    }

    @Test
    fun `with object mapper`() {
        val username = "Steven"

        // POST /users
        val contentAsString = objectMapper.writeValueAsString(UserController.PostUserRequest("Steven"))

        mvc.perform(
            post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(contentAsString)
        ).andExpect(status().isOk)

        // GET /users
        val response = mvc.get("/users/$username")
            .andExpect {
                status { isOk() }
            }.andReturn().response.contentAsString

        // Verify
        val responseObject = objectMapper.readValue(response, GetUserResponse::class.java)
        assertThat(responseObject.username).isEqualTo(username)
    }

    @Test
    fun `with string literal`() {
        val username = "Steven"

        // POST /users
        mvc.perform(
            post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                        "username": "$username"
                    }
                """.trimIndent()
                )
        ).andExpect(status().isOk())

        // GET /users
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