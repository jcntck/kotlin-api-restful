package dev.joaoneto.mercado_livro.extension

import dev.joaoneto.mercado_livro.controller.request.PostBookRequest
import dev.joaoneto.mercado_livro.controller.request.PostCustomerRequest
import dev.joaoneto.mercado_livro.controller.request.PutBookRequest
import dev.joaoneto.mercado_livro.controller.request.PutCustomerRequest
import dev.joaoneto.mercado_livro.controller.response.BookResponse
import dev.joaoneto.mercado_livro.controller.response.CustomerResponse
import dev.joaoneto.mercado_livro.controller.response.PageResponse
import dev.joaoneto.mercado_livro.enums.BookStatus
import dev.joaoneto.mercado_livro.enums.CustomerStatus
import dev.joaoneto.mercado_livro.model.BookModel
import dev.joaoneto.mercado_livro.model.CustomerModel
import org.springframework.data.domain.Page

fun PostCustomerRequest.toCustomerModel(): CustomerModel {
    return CustomerModel(name = this.name, email = this.email, status = CustomerStatus.ATIVO, password = this.password)
}

fun PutCustomerRequest.toCustomerModel(customer: CustomerModel): CustomerModel {
    return CustomerModel(
        id = customer.id,
        name = this.name,
        email = this.email,
        status = customer.status,
        password = customer.password,
        roles = customer.roles
    )
}

fun PostBookRequest.toBookModel(customer: CustomerModel): BookModel {
    return BookModel(name = this.name, price = this.price, status = BookStatus.ATIVO, customer = customer)
}

fun PutBookRequest.toBookModel(book: BookModel): BookModel {
    return BookModel(
        id = book.id,
        name = this.name ?: book.name,
        price = this.price ?: book.price,
        status = book.status,
        customer = book.customer
    )
}


fun CustomerModel.toResponse(): CustomerResponse {
    return CustomerResponse(id = this.id, name = name, email = this.email, status = status)
}

fun BookModel.toResponse(): BookResponse {
    return BookResponse(
        id = this.id,
        name = this.name,
        price = this.price,
        customer = this.customer,
        status = this.status
    )
}

fun <T> Page<T>.toPageResponse(): PageResponse<T> {
    return PageResponse(this.content, this.number, this.totalElements, this.totalPages)
}