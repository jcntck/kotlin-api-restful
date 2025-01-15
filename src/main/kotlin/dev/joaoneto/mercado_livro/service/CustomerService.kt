package dev.joaoneto.mercado_livro.service

import dev.joaoneto.mercado_livro.enums.CustomerStatus
import dev.joaoneto.mercado_livro.enums.Errors
import dev.joaoneto.mercado_livro.enums.Role
import dev.joaoneto.mercado_livro.exception.NotFoundException
import dev.joaoneto.mercado_livro.model.CustomerModel
import dev.joaoneto.mercado_livro.repository.CustomerRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class CustomerService(
    private val customerRepository: CustomerRepository,
    private val bookService: BookService,
    private val bCrypt: BCryptPasswordEncoder
) {

    fun getAll(name: String?): List<CustomerModel> {
        name?.let {
            return customerRepository.findByNameContaining(it)
        }
        return customerRepository.findAll().toList()
    }

    fun create(customer: CustomerModel) {
        val customerCopy = customer.copy(
            roles = setOf(Role.CUSTOMER),
            password = bCrypt.encode(customer.password)
        )
        customerRepository.save(customerCopy)
    }

    fun findById(id: Int): CustomerModel {
        return customerRepository.findById(id).orElseThrow{ NotFoundException(Errors.ML201.message.format(id), Errors.ML201.code) }
    }

    fun update(customer: CustomerModel) {
        if (!customerRepository.existsById(customer.id!!)) {
            throw NotFoundException(Errors.ML201.message.format(customer.id), Errors.ML201.code)
        }
        customerRepository.save(customer)
    }

    fun delete(id: Int) {
        val customer = findById(id)
        bookService.deleteByCustomer(customer)
        customer.status = CustomerStatus.INATIVO
        customerRepository.save(customer)
    }

    fun emailAvailable(email: String): Boolean {
        return !customerRepository.existsByEmail(email)
    }
}