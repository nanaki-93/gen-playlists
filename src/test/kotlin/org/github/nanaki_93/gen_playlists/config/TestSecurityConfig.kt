package org.github.nanaki_93.gen_playlists.config

import org.github.nanaki_93.gen_playlists.model.Role
import org.github.nanaki_93.gen_playlists.model.SecureUser
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.security.core.userdetails.UserDetailsService
import java.util.*

@TestConfiguration
class TestConfig {
    @Bean
    @Primary
    fun mockUserDetailsService(): UserDetailsService {
        return UserDetailsService { username ->
            SecureUser(
                id = UUID.randomUUID(),
                email = username,
                passwordHash = "hashedPassword",
                role = Role.USER
            )
        }
    }
}
