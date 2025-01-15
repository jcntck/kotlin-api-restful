package dev.joaoneto.mercado_livro.repository

import dev.joaoneto.mercado_livro.helper.buildCustomer
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import kotlin.test.*

@ExtendWith(MockKExtension::class)
@ActiveProfiles("test")
@SpringBootTest
class CustomerRepositoryTest {

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @BeforeEach
    fun setup() = customerRepository.deleteAll()

    @Test
    fun `should return name containing`() {
        val name1 = customerRepository.save(buildCustomer(name = "Name 01"))
        val name2 = customerRepository.save(buildCustomer(name = "Name 02"))
        customerRepository.save(buildCustomer(name = "Foo"))

        val customers = customerRepository.findByNameContaining("Na")

        assertEquals(listOf(name1, name2), customers)
    }

    @Nested
    inner class `exists by email` {
        @Test
        fun `should return true when email exists`() {
            val email = "email@teste.com"
            customerRepository.save(buildCustomer(email = email))

            val exists = customerRepository.existsByEmail(email)

            assertTrue(exists)

        }

        @Test
        fun `should return false when email do not exists`() {
            val email = "nonexistingemail@teste.com"

            val exists = customerRepository.existsByEmail(email)

            assertFalse(exists)
        }
    }

    @Nested
    inner class `find by email` {
        @Test
        fun `should return customer when email exists`() {
            val email = "email@teste.com"
            val customer = customerRepository.save(buildCustomer(email = email))

            val result = customerRepository.findByEmail(email)

            assertNotNull(result)
            assertEquals(customer, result)
        }

        @Test
        fun `should return null when email do not exists`() {
            val email = "nonexistingemail@teste.com"

            val result = customerRepository.findByEmail(email)

            assertNull(result)
        }
    }
}