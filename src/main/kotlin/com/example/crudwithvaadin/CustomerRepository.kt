package com.example.crudwithvaadin

import org.springframework.data.jpa.repository.JpaRepository

@Suppress("unused")
interface CustomerRepository : JpaRepository<Customer, Long> {

    fun findByLastNameStartsWithIgnoreCase(lastName: String): List<Customer>
}