package dev.joaoneto.mercado_livro.controller.request

import jakarta.validation.constraints.NotEmpty
import java.math.BigDecimal

data class PutBookRequest (
    var name: String?,
    var price: BigDecimal?
)
