package dev.joaoneto.mercado_livro.security

import dev.joaoneto.mercado_livro.enums.CustomerStatus
import dev.joaoneto.mercado_livro.model.CustomerModel
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserCustomDetails(private val customerModel: CustomerModel) : UserDetails {
    val id = customerModel.id
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        customerModel.roles.map { SimpleGrantedAuthority(it.description) }.toMutableList()
    override fun getPassword(): String = customerModel.password
    override fun getUsername(): String = customerModel.id.toString()
    override fun isEnabled(): Boolean = customerModel.status == CustomerStatus.ATIVO
}