package org.github.nanaki_93.gen_playlists.security

import org.github.nanaki_93.gen_playlists.model.Role
import org.github.nanaki_93.gen_playlists.model.SecureUser
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class JwtServiceTest {

    private lateinit var jwtService: JwtService
    private lateinit var userDetails: SecureUser

    private val testSecret = "dGVzdC1zZWNyZXQtZm9yLWp3dC10ZXN0aW5nLW11c3QtYmUtYXQtbGVhc3QtMjU2LWJpdHMtbG9uZw=="
    private val testExpiration = 3600000L

    @BeforeEach
    fun setUp() {
        jwtService = JwtService(testSecret, testExpiration)
        userDetails = SecureUser(
            id = UUID.randomUUID(),
            email = "testuser@example.com",
            passwordHash = "hashedPassword",
            role = Role.USER
        )
    }

    @Test
    fun `should generate valid JWT token`() {
        val token = jwtService.generateToken(userDetails)

        assertNotNull(token)
        assertTrue(token.isNotEmpty())
        assertEquals(3, token.split(".").size)
    }

    @Test
    fun `should extract username from valid token`() {
        val token = jwtService.generateToken(userDetails)

        val extractedUsername = jwtService.extractUsername(token)

        assertEquals(userDetails.username, extractedUsername)
    }

    @Test
    fun `should validate token for correct user`() {
        val token = jwtService.generateToken(userDetails)

        assertTrue(jwtService.isTokenValid(token, userDetails))
    }

    @Test
    fun `should reject token for different user`() {
        val token = jwtService.generateToken(userDetails)
        val differentUser = SecureUser(
            id = UUID.randomUUID(),
            email = "different@example.com",
            passwordHash = "hashedPassword",
            role = Role.USER
        )

        assertFalse(jwtService.isTokenValid(token, differentUser))
    }

    @Test
    fun `should work with admin role`() {
        val adminUser = SecureUser(
            id = UUID.randomUUID(),
            email = "admin@example.com",
            passwordHash = "hashedPassword",
            role = Role.ADMIN
        )

        val token = jwtService.generateToken(adminUser)
        assertTrue(jwtService.isTokenValid(token, adminUser))
        assertEquals("admin@example.com", jwtService.extractUsername(token))
    }
}