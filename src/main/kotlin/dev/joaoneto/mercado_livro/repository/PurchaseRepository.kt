package dev.joaoneto.mercado_livro.repository

import dev.joaoneto.mercado_livro.model.PurchaseModel
import org.springframework.data.repository.CrudRepository

interface PurchaseRepository: CrudRepository<PurchaseModel, Int> {
}
