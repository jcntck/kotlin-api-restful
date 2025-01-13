package dev.joaoneto.mercado_livro.exception

class AuthenticationException(override val message: String, val errorCode: String) : Exception() {
}