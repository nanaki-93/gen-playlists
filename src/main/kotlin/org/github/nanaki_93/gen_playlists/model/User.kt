package org.github.nanaki_93.gen_playlists.model

import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.time.OffsetDateTime
import java.util.*


data class User(
    val id: UUID? = null,
    val email: String,
    val passwordHash: String,
    val createdAt: OffsetDateTime? = null,
    val updatedAt: OffsetDateTime? = null,
    val spotifyUser: SpotifyUser? = null,
    val role: Role = Role.USER,
)

enum class Role {
    USER, PREMIUM
}


// Model for Spotify tokens, with a User relationship
data class SpotifyUser(
    val id: UUID? = null,
    val spotifyId: String,
    val role: Role = Role.USER,
    val accessToken: String,
    val refreshToken: String,
    val tokenExpirationTime: OffsetDateTime,
    val scope: String,
    val profileImageUrl: String, //take the biggest image in the list
    val createdAt: OffsetDateTime? = null,
    val updatedAt: OffsetDateTime? = null
)


/**
 * --- RowMappers for Spring JDBC ---
 */
object UserRowMapper : RowMapper<User> {
    override fun mapRow(rs: ResultSet, rowNum: Int): User {
        return User(
            id = UUID.fromString(rs.getString("user_id")),
            email = rs.getString("email"),
            passwordHash = rs.getString("password_hash"),
            role = Role.valueOf(rs.getString("role")),
            createdAt = rs.getObject("created_at", OffsetDateTime::class.java),
            updatedAt = rs.getObject("updated_at", OffsetDateTime::class.java)
        )
    }
}

object SpotifyUserRowMapper : RowMapper<SpotifyUser> {
    override fun mapRow(rs: ResultSet, rowNum: Int): SpotifyUser {
        return SpotifyUser(
            id = UUID.fromString(rs.getString("spotify_user_id")),
            spotifyId = rs.getString("spotify_id"),
            role = Role.valueOf(rs.getString("role")),
            accessToken = rs.getString("access_token"),
            refreshToken = rs.getString("refresh_token"),
            tokenExpirationTime = rs.getObject("token_expiration_time", OffsetDateTime::class.java),
            scope = rs.getString("scope"),
            profileImageUrl = rs.getString("profile_image_url"),
            createdAt = rs.getObject("created_at", OffsetDateTime::class.java),
            updatedAt = rs.getObject("updated_at", OffsetDateTime::class.java)
        )
    }
}

