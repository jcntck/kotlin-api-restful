package dev.joaoneto.mercado_livro.events

import dev.joaoneto.mercado_livro.model.PurchaseModel
import org.springframework.context.ApplicationEvent


class PurchaseEvent (
    source: Any,
    val purchaseModel: PurchaseModel
) : ApplicationEvent(source)