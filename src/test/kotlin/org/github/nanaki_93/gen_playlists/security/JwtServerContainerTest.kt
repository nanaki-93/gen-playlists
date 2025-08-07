package org.github.nanaki_93.gen_playlists.security

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.userdetails.User
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.test.assertTrue

@SpringBootTest
@Testcontainers
class JwtServiceContainerTest {

    companion object {
        @Container
        @JvmStatic
        val postgres = PostgreSQLContainer("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass")

        @DynamicPropertySource
        @JvmStatic
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
        }
    }

    @Autowired
    private lateinit var jwtService: JwtService

    @Test
    fun `should work with container database context`() {
        val userDetails = User.builder()
            .username("containertest@example.com")
            .password("password")
            .authorities(emptyList())
            .build()

        val token = jwtService.generateToken(userDetails)

        assertTrue(jwtService.isTokenValid(token, userDetails))
        assertTrue(postgres.isRunning)
    }
}