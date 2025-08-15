package org.github.nanaki_93.gen_playlists.dto

import java.time.OffsetDateTime
import java.util.*

data class UserDto(
    val id: UUID? = null,
    val email: String,
    val createdAt: OffsetDateTime? = null,
    val updatedAt: OffsetDateTime? = null,
    val spotifyId: String? = null,
    val role: String,
    val isActive: Boolean = true
)

data class SpotifyUserDto(
    val spotifyId: String,
    val scope: String,
    val createdAt: OffsetDateTime? = null,
    val updatedAt: OffsetDateTime? = null,
)

data class SpotifyPlaylistReqDto(
    val spotifyId: String,
    val playlistId: String,
)

data class SpotifyPlaylistDto(
    val spotifyId: String,
    val collaborative: Boolean,
    val name: String,
    val description: String,
    val apiHref: String,
    val imageUrl: String,
    val createdAt: OffsetDateTime? = null,
    val updatedAt: OffsetDateTime? = null,
)


// For user registration/updates (without sensitive data)
data class CreateUserDto(
    val email: String,
    val password: String,
    val spotifyId: String? = null,
    val toRegister: Boolean = false
)

data class UpdateUserDto(
    val email: String?,
    val role: String?
)

