package org.github.nanaki_93.gen_playlists.model

import org.github.nanaki_93.gen_playlists.repository.UserRepository
import org.springframework.jdbc.core.RowMapper
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.sql.ResultSet
import java.time.OffsetDateTime
import java.util.*

/**
 * A data class representing the 'users' table, suitable for Spring JDBC.
 * It does not use JPA annotations.
 */
data class SecureUser(
    val id: UUID? = null,
    val email: String,
    val passwordHash: String,
    val role: Role,
    val createdAt: OffsetDateTime? = null,
    val updatedAt: OffsetDateTime? = null
) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> = listOf(SimpleGrantedAuthority(role.name))
    override fun getPassword(): String = passwordHash
    override fun getUsername(): String = email
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}

// Role enum for user permissions
enum class Role {
    USER, ADMIN
}


// Model for Spotify tokens, with a User relationship
data class SpotifyToken(
    val id: UUID? = null,
    val user: SecureUser,
    val spotifyId: String,
    val accessToken: String,
    val refreshToken: String,
    val tokenExpirationTime: OffsetDateTime,
    val scope: String,
    val createdAt: OffsetDateTime? = null,
    val updatedAt: OffsetDateTime? = null
)


/**
 * --- RowMappers for Spring JDBC ---
 */

class SecureUserRowMapper : RowMapper<SecureUser> {
    override fun mapRow(rs: ResultSet, rowNum: Int): SecureUser {
        return SecureUser(
            id = rs.getObject("user_id", UUID::class.java),
            email = rs.getString("email"),
            passwordHash = rs.getString("password_hash"), // Assuming a 'password_hash' column
            role = Role.valueOf(rs.getString("role")), // Assuming a 'role' column
            createdAt = rs.getObject("created_at", OffsetDateTime::class.java),
            updatedAt = rs.getObject("updated_at", OffsetDateTime::class.java)
        )
    }
}

class SpotifyTokenRowMapper(private val userRepository: UserRepository) : RowMapper<SpotifyToken> {
    override fun mapRow(rs: ResultSet, rowNum: Int): SpotifyToken {
        val userId = rs.getObject("user_id", UUID::class.java)
        val user = userRepository.findById(userId) ?: throw IllegalStateException("User not found for spotify token")

        return SpotifyToken(
            id = rs.getObject("spotify_token_id", UUID::class.java),
            user = user,
            spotifyId = rs.getString("spotify_id"),
            accessToken = rs.getString("access_token"),
            refreshToken = rs.getString("refresh_token"),
            tokenExpirationTime = rs.getObject("token_expiration_time", OffsetDateTime::class.java),
            scope = rs.getString("scope"),
            createdAt = rs.getObject("created_at", OffsetDateTime::class.java),
            updatedAt = rs.getObject("updated_at", OffsetDateTime::class.java)
        )
    }
}
