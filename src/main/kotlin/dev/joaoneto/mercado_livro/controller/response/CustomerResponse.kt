package dev.joaoneto.mercado_livro.controller.response

import dev.joaoneto.mercado_livro.enums.CustomerStatus

data class CustomerResponse (
    var id: Int? = null,
    var name: String,
    var email: String,
    var status: CustomerStatus
)
