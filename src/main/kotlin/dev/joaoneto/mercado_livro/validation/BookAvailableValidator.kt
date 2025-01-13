package dev.joaoneto.mercado_livro.validation

import dev.joaoneto.mercado_livro.enums.BookStatus
import dev.joaoneto.mercado_livro.service.BookService
import dev.joaoneto.mercado_livro.service.CustomerService
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class BookAvailableValidator(private val bookService: BookService) :
    ConstraintValidator<BookAvailable, Set<Int>> {
    override fun isValid(ids: Set<Int>?, context: ConstraintValidatorContext?): Boolean {
        if (ids.isNullOrEmpty()) return false;
        return !bookService.findAllByIds(ids).any { it.status != BookStatus.ATIVO }
    }
}
