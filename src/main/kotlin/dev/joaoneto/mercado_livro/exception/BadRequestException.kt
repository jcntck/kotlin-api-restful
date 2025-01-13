package dev.joaoneto.mercado_livro.exception

class BadRequestException(override val message: String, val errorCode: String) : Exception() {
}