package testrestendpoints

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class TestRestEndpoints : WebMvcTest() {

    @Autowired
    private lateinit var userApiTest: UserApiTest

    @Test
    fun `smoke`() {
        // Given
        val userName = "Steven"
        userApiTest.newUser(userName)
        // When
        userApiTest.getUser(userName)
            .andExpect { status { isOk() } }


        // Then

        // And
    }
}