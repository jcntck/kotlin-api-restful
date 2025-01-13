package dev.joaoneto.mercado_livro.events.listener

import dev.joaoneto.mercado_livro.events.PurchaseEvent
import dev.joaoneto.mercado_livro.service.BookService
import dev.joaoneto.mercado_livro.service.PurchaseService
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UpdateSoldBookListener(
    private val bookService: BookService
) {

    @Async
    @EventListener
    fun listen(purchaseEvent: PurchaseEvent) {
        println("Atualizando status dos livros")
        bookService.purchase(purchaseEvent.purchaseModel.books)
    }
}