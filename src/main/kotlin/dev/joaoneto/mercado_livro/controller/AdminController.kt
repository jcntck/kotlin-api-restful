package dev.joaoneto.mercado_livro.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("admin")
class AdminController() {

    @GetMapping("/report")
    fun getAll(): String {
        return "This is a Report. Only Admin can see it"
    }
}