package com.henry.springvalid

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class SpringValidApplication

fun main(args: Array<String>) {

    SpringApplication.run(SpringValidApplication::class.java, *args)
}