package dev.joaoneto.mercado_livro.controller.response

data class PageResponse<T>(
    var items: List<T>,
    var currentPage: Int,
    var totalItems: Long,
    var totalPages: Int,
)