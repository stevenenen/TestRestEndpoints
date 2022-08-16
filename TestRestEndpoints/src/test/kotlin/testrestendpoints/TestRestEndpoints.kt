package testrestendpoints

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.core.IsEqual
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate

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

        objectMapper = ObjectMapper()
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true)
            .registerModule(kotlinModule)
            .registerModule(JavaTimeModule())
    }

    @Test
    fun `with object mapper`() {
        val username = "Steven"
        val age = 1

        // Object -> JSON String
        val request = PostUserRequest(username = username, age)
        val json = objectMapper.writeValueAsString(request)

        // POST /users
        val response = mvc.perform(
            post("/users")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andReturn().response.contentAsString

        // JSON String -> Object
        val actual = objectMapper.readValue(
            response,
            PostUserResponse::class.java
        )

        // Verify
        val expected = PostUserResponse(username, age, LocalDate.of(2022, 1, 1), Type.ADMIN)
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `with string literal`() {
        val username = "Steven"
        val body = """
            {
                "username": "$username"
            }
        """.trimIndent()

        // POST /users
        mvc.post("/users") {
            contentType = MediaType.APPLICATION_JSON
            content = body
        }.andExpect {
            status { isOk() }
            content {
                jsonPath("$.username") { value(username) }
                jsonPath("$.type.upper", IsEqual("ADMIN"))
                jsonPath("$.type.lower", IsEqual("admin"))
            }
        }
    }
}
