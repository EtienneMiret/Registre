package io.miret.registre.back

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["io.miret.registre.back"])
class Application

fun main(args: Array<String>) {
  runApplication<Application>(*args)
}
