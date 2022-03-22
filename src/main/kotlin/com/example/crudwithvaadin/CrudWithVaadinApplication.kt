package com.example.crudwithvaadin

import mu.*
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.*

@Suppress("SpellCheckingInspection")
@SpringBootApplication
class CrudWithVaadinApplication {
    companion object {
        val logger = KotlinLogging.logger {}
    }

    @Bean
    fun loadData(repository: CustomerRepository): CommandLineRunner {
        return CommandLineRunner {
            // save a couple of customers
            repository.save(Customer("Jack", "Bauer"))
            repository.save(Customer("Chloe", "O'Brian"))
            repository.save(Customer("Kim", "Bauer"))
            repository.save(Customer("David", "Palmer"))
            repository.save(Customer("Michelle", "Dessler"))

            //fetch all customers
            logger.info { "Customers found with findAll()" }
            logger.info { "------------------------------" }
            repository.findAll().forEach {
                logger.info { it }
            }
            logger.info { "" }

            // fetch an individual customer by ID
            val customer = repository.findById(1L).get()
            logger.info { "Customer found with findOne(1L):" }
            logger.info { "--------------------------------" }
            logger.info { customer }
            logger.info { "" }

            // fetch customers by last name
            logger.info { "Customer found with findByLastNameStartsWithIgnoreCase('Bauer')" }
            logger.info { "--------------------------------" }
            repository.findByLastNameStartsWithIgnoreCase("Bauer").forEach {
                logger.info { it }
            }
            logger.info { "" }

        }
    }
}

fun main(args: Array<String>) {
    runApplication<CrudWithVaadinApplication>(*args)
}
