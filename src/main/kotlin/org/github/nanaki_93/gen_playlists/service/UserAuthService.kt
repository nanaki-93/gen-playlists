package org.github.nanaki_93.gen_playlists.service

import org.github.nanaki_93.gen_playlists.model.User
import org.github.nanaki_93.gen_playlists.model.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserAuthService(
    private val userRepository: UserRepository,
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails? {
        return userRepository.findByEmail(username ?: "") ?: throw IllegalStateException("User not found")
    }

    fun toUserDetail(user: User): UserDetails {
        return org.springframework.security.core.userdetails.User(
            user.email,
            user.passwordHash,
            true,
            true,
            true,
            true,
            mutableListOf<GrantedAuthority>(SimpleGrantedAuthority(user.role.name))
        )
    }
}