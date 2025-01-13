package dev.joaoneto.mercado_livro.exception

class NotFoundException(override val message: String, val errorCode: String) : Exception() {
}