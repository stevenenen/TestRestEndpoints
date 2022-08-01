package testrestendpoints

import org.springframework.web.bind.annotation.*
import java.util.*

@RestController()
class UserController {

    @PostMapping("/users")
    fun postUser(@RequestParam("username", required = true) userName: String) {
        userName
    }

    @GetMapping("/users/{username}")
    fun getUser(@PathVariable("username") username: String): UserView {
        return UserView(UUID.randomUUID(), username)
    }
}

data class UserView(
    val id: UUID,
    val username: String
)