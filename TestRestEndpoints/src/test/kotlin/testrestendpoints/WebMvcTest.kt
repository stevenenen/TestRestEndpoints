package testrestendpoints

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.stereotype.Component
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post


//@ActiveProfiles("test")
//@SpringBootTest()
//@AutoConfigureMockMvc
//@EnableAutoConfiguration
@WebMvcTest
open class WebMvcTest {

    @Autowired
    internal lateinit var userAutowired: UserApiTest
}

@Component
class UserApiTest(
    private val mvc: MockMvc
) {

    fun getUser(userName: String): ResultActionsDsl {
        return mvc.get("/users/{userId}") {
            param("userId", userName.toString())
        }
    }

    fun newUser(userName: String) {
        mvc.post("/users") {
            param("userName", userName)
        }
    }
}
