package dev.joaoneto.mercado_livro.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [BookAvailableValidator::class])
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class BookAvailable(
    val message: String = "Não é possível comprar um livro com STATUS diferente de ATIVO",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
