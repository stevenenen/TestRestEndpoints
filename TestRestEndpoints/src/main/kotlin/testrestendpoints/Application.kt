package testrestendpoints

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.EnableScheduling
import javax.annotation.PostConstruct

@SpringBootApplication
//@ComponentScan("testrestendpoints")
@EnableScheduling
class TestRestEndpoints {
    private val log = LoggerFactory.getLogger(TestRestEndpoints::class.java)

    @PostConstruct
    fun init() {
        log.info("Started the Application")
    }
}

fun main(args: Array<String>) {
    runApplication<TestRestEndpoints>(*args)
}