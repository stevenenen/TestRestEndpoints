package testrestendpoints

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

data class PostUserRequest(
    val username: String,
    @JsonProperty(required = false)
    val age: Int
)

data class PostUserResponse(
    val username: String,
    val age: Int,
    val created: LocalDate,
    @JsonSerialize(using = UserTypeSerializer::class)
    @JsonDeserialize(using = UserTypeDeserializer::class)
    val type: Type
)

class UserTypeSerializer : StdSerializer<Type>(Type::class.java) {
    override fun serialize(value: Type, gen: JsonGenerator, provider: SerializerProvider) {
        with(gen) {
            writeStartObject()
            // upper -> uppercase
            writeStringField("uppercase", value.name.uppercase())
            // lower -> lowercase
            writeStringField("lowercase", value.name.lowercase())
            writeEndObject()
        }
    }
}

class UserTypeDeserializer : StdDeserializer<Type>(Type::class.java) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Type {
        val node = p.readValueAsTree<JsonNode>()
        // upper -> uppercase
        val value = node.get("uppercase").textValue()
        return Type.valueOf(value)
    }
}

@RestController
class UserController {
    @PostMapping("/users")
    fun postUser(@RequestBody request: PostUserRequest): PostUserResponse {
        return PostUserResponse(
            username = request.username,
            age = request.age,
            created = LocalDate.of(2022, 1, 1),
            Type.ADMIN
        )
    }
}

enum class Type {
    ADMIN,
    POWER_USER
}

//
//    @PostMapping("/users")
//    fun postUser(@RequestBody request: PostUserRequest) {
//    }
//
//    data class GetUserResponse(
//        val id: UUID,
//        val username: String
//    )
//
//    @GetMapping("/users/{username}")
//    fun getUser(@PathVariable("username") username: String): GetUserResponse {
//        return GetUserResponse(UUID.randomUUID(), username)
//    }
//}