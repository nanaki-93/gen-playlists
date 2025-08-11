package org.github.nanaki_93.gen_playlists.dto

import java.time.OffsetDateTime
import java.util.*

data class UserDto(
    val id: UUID? = null,
    val email: String,
    val createdAt: OffsetDateTime? = null,
    val updatedAt: OffsetDateTime? = null,
    val spotifyUser: SpotifyUserDto? = null,
    val role: String,
    val isActive: Boolean = true
)

data class SpotifyUserDto(
    val id: UUID? = null,
    val spotifyId: String,
    val role: String,
    val scope: String,
    val profileImageUrl: String,
    val createdAt: OffsetDateTime? = null,
    val updatedAt: OffsetDateTime? = null,
    val user: UserDto? = null
)


// For user registration/updates (without sensitive data)
data class CreateUserDto(
    val email: String,
    val password: String,
    val role: String = "USER"
)

data class UpdateUserDto(
    val email: String?,
    val role: String?
)

