package testrestendpoints

import org.springframework.web.bind.annotation.*
import java.util.*

@RestController()
class UserController {

    data class PostUserRequest(
        val username: String
    )

    @PostMapping("/users")
    fun postUser(@RequestBody request: PostUserRequest) {

    }

    data class GetUserResponse(
        val id: UUID,
        val username: String
    )

    @GetMapping("/users/{username}")
    fun getUser(@PathVariable("username") username: String): GetUserResponse {
        return GetUserResponse(UUID.randomUUID(), username)
    }
}