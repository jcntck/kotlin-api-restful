package dev.joaoneto.mercado_livro.repository

import dev.joaoneto.mercado_livro.enums.BookStatus
import dev.joaoneto.mercado_livro.model.BookModel
import dev.joaoneto.mercado_livro.model.CustomerModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository

interface BookRepository : JpaRepository<BookModel, Int> {
    fun findByStatus(status: BookStatus, pageable: Pageable): Page<BookModel>
    fun findByCustomer(customer: CustomerModel): List<BookModel>
}