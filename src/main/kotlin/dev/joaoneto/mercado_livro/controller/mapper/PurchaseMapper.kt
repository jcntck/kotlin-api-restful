package dev.joaoneto.mercado_livro.controller.mapper

import dev.joaoneto.mercado_livro.controller.request.PostPurchaseRequest
import dev.joaoneto.mercado_livro.model.PurchaseModel
import dev.joaoneto.mercado_livro.service.BookService
import dev.joaoneto.mercado_livro.service.CustomerService
import org.springframework.stereotype.Component

@Component
class PurchaseMapper(
    private val bookService: BookService,
    private val customerService: CustomerService
) {

    fun toModel(request: PostPurchaseRequest): PurchaseModel {
        val customer = customerService.findById(request.customerId)
        val books = bookService.findAllByIds(request.bookIds)
        return PurchaseModel(
            customer = customer,
            books = books.toMutableList(),
            price = books.sumOf { it.price }
        )
    }
}