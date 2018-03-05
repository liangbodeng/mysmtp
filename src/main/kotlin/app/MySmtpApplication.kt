package app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MySmtpApplication

fun main(args: Array<String>) {
    runApplication<MySmtpApplication>(*args)
}
