package dev.joaoneto.mercado_livro.controller

import com.fasterxml.jackson.databind.ObjectMapper
import dev.joaoneto.mercado_livro.controller.request.PostCustomerRequest
import dev.joaoneto.mercado_livro.controller.request.PutCustomerRequest
import dev.joaoneto.mercado_livro.enums.CustomerStatus
import dev.joaoneto.mercado_livro.helper.buildCustomer
import dev.joaoneto.mercado_livro.repository.CustomerRepository
import dev.joaoneto.mercado_livro.security.UserCustomDetails
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.random.Random
import kotlin.test.assertEquals

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@ActiveProfiles("test")
@WithMockUser
class CustomerControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setup() = customerRepository.deleteAll()

    @AfterEach
    fun tearDown() = customerRepository.deleteAll()

    @Test
    fun `should return all customers`() {
        val customer1 = customerRepository.save(buildCustomer())
        val customer2 = customerRepository.save(buildCustomer())

        mockMvc.perform(get("/customers"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].id").value(customer1.id))
            .andExpect(jsonPath("$[0].name").value(customer1.name))
            .andExpect(jsonPath("$[0].email").value(customer1.email))
            .andExpect(jsonPath("$[0].status").value(customer1.status.name))
            .andExpect(jsonPath("$[1].id").value(customer2.id))
            .andExpect(jsonPath("$[1].name").value(customer2.name))
            .andExpect(jsonPath("$[1].email").value(customer2.email))
            .andExpect(jsonPath("$[1].status").value(customer2.status.name))
    }

    @Test
    fun `should filter all customers by name when get all`() {
        val customer1 = customerRepository.save(buildCustomer(name = "Foo"))
        customerRepository.save(buildCustomer(name = "John"))

        mockMvc.perform(get("/customers?name=Fo"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].id").value(customer1.id))
            .andExpect(jsonPath("$[0].name").value(customer1.name))
            .andExpect(jsonPath("$[0].email").value(customer1.email))
            .andExpect(jsonPath("$[0].status").value(customer1.status.name))
    }

    @Test
    fun `should create customer`() {
        val request = PostCustomerRequest("fake name", "${Random.nextInt()}@fakeemail.com", "fakepassword")

        mockMvc.perform(
            post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)

        val customers = customerRepository.findAll()
        assertEquals(1, customers.size)
        assertEquals(request.name, customers[0].name)
        assertEquals(request.email, customers[0].email)
    }

    @Test
    fun `should throw error when create customer has invalid information`() {
        val request = PostCustomerRequest("", "${Random.nextInt()}@fakeemail.com", "fakepassword")

        mockMvc.perform(
            post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isUnprocessableEntity)
    }

    @Test
    fun `should get user by id when user has the same id`() {
        val customer = customerRepository.save(buildCustomer())

        mockMvc.perform(get("/customers/${customer.id}").with(user(UserCustomDetails(customer))))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(customer.id))
            .andExpect(jsonPath("$.name").value(customer.name))
            .andExpect(jsonPath("$.email").value(customer.email))
            .andExpect(jsonPath("$.status").value(customer.status.name))
    }

    @Test
    fun `should return forbidden when user has different id`() {
        val customer = customerRepository.save(buildCustomer())

        mockMvc.perform(get("/customers/0").with(user(UserCustomDetails(customer))))
            .andExpect(status().isForbidden)
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `should get user by id when user is admin`() {
        val customer = customerRepository.save(buildCustomer())

        mockMvc.perform(get("/customers/${customer.id}"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(customer.id))
            .andExpect(jsonPath("$.name").value(customer.name))
            .andExpect(jsonPath("$.email").value(customer.email))
            .andExpect(jsonPath("$.status").value(customer.status.name))
    }

    @Test
    fun `should update customer`() {
        val customer = customerRepository.save(buildCustomer())
        val request = PutCustomerRequest("John Foe", "email@emailupdate.com")

        mockMvc.perform(
            put("/customers/${customer.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isNoContent)

        val customers = customerRepository.findAll()
        assertEquals(1, customers.size)
        assertEquals(request.name, customers[0].name)
        assertEquals(request.email, customers[0].email)
    }

    @Test
    fun `should return not found when update customer not exists`() {
        val request = PutCustomerRequest("John Foe", "email@emailupdate.com")

        mockMvc.perform(
            put("/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isNotFound)
    }

    @Test
    fun `should throw error when update customer has invalid information`() {
        val request = PutCustomerRequest("", "${Random.nextInt()}@fakeemail.com")

        mockMvc.perform(
            put("/customers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isUnprocessableEntity)
    }

    @Test
    fun `should delete customer`() {
        val customer = customerRepository.save(buildCustomer())
        mockMvc.perform(delete("/customers/${customer.id}")).andExpect(status().isNoContent)
        val customerDeleted = customerRepository.findById(customer.id!!)
        assertEquals(CustomerStatus.INATIVO, customerDeleted.get().status)
    }

    @Test
    fun `should return not found when delete customer not exists`() {
        mockMvc.perform(delete("/customers/1")).andExpect(status().isNotFound)
    }
}