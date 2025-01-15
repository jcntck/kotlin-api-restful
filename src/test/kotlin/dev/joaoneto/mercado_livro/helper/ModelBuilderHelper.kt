package dev.joaoneto.mercado_livro.helper

import dev.joaoneto.mercado_livro.enums.CustomerStatus
import dev.joaoneto.mercado_livro.enums.Role
import dev.joaoneto.mercado_livro.model.BookModel
import dev.joaoneto.mercado_livro.model.CustomerModel
import dev.joaoneto.mercado_livro.model.PurchaseModel
import java.awt.print.Book
import java.math.BigDecimal
import java.util.*


fun buildCustomer(
    id: Int? = null,
    name: String = "customer name",
    email: String = "${UUID.randomUUID()}@email.com",
    password: String = "password"
) = CustomerModel(
    id = id,
    name = name,
    email = email,
    status = CustomerStatus.ATIVO,
    password = password,
    roles = setOf(Role.CUSTOMER)
)

fun buildPurchsase(
    id: Int? = null,
    customer: CustomerModel = buildCustomer(),
    books: MutableList<BookModel> = mutableListOf(),
    nfe: String? = UUID.randomUUID().toString(),
    price: BigDecimal = BigDecimal.TEN
) = PurchaseModel(
    id = id,
    customer = customer,
    books = books,
    nfe = nfe,
    price = price
)