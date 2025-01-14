package dev.joaoneto.mercado_livro.controller

import dev.joaoneto.mercado_livro.controller.request.PostCustomerRequest
import dev.joaoneto.mercado_livro.controller.request.PutCustomerRequest
import dev.joaoneto.mercado_livro.controller.response.CustomerResponse
import dev.joaoneto.mercado_livro.controller.response.PageResponse
import dev.joaoneto.mercado_livro.extension.toCustomerModel
import dev.joaoneto.mercado_livro.extension.toPageResponse
import dev.joaoneto.mercado_livro.extension.toResponse
import dev.joaoneto.mercado_livro.security.UserCanOnlyAccessTheirOwnResource
import dev.joaoneto.mercado_livro.service.CustomerService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("customers")
class CustomerController(
    private val customerService: CustomerService
) {

    @GetMapping
    fun getAll(
        @RequestParam name: String?,
        @PageableDefault(page = 0, size = 10) pageable: Pageable
    ): PageResponse<CustomerResponse> {
        return customerService.getAll(name, pageable).map { it.toResponse() }.toPageResponse()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid customer: PostCustomerRequest) {
        customerService.create(customer.toCustomerModel())
    }

    @GetMapping("/{id}")
    @UserCanOnlyAccessTheirOwnResource
    fun getCustomer(@PathVariable id: Int): CustomerResponse? {
        return customerService.findById(id).toResponse()
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(@PathVariable id: Int, @RequestBody @Valid customer: PutCustomerRequest) {
        val customerSaved = customerService.findById(id)
        customerService.update(customer.toCustomerModel(customerSaved))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Int) {
        customerService.delete(id)
    }
}