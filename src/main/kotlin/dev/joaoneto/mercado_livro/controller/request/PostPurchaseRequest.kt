package dev.joaoneto.mercado_livro.controller.request

import com.fasterxml.jackson.annotation.JsonAlias
import dev.joaoneto.mercado_livro.validation.BookAvailable
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class PostPurchaseRequest(

    @field:NotNull
    @field:Positive
    @JsonAlias("customer_id")
    val customerId: Int,

    @field:NotNull
    @BookAvailable
    @JsonAlias("book_ids")
    val bookIds: Set<Int>,
)
