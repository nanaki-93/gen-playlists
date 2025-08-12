package org.github.nanaki_93.gen_playlists.dto

import org.github.nanaki_93.gen_playlists.model.SpotifyProfileRes
import org.github.nanaki_93.gen_playlists.model.SpotifyUser
import org.github.nanaki_93.gen_playlists.model.User
import java.time.OffsetDateTime
import java.util.*

data class UserDto(
    val id: UUID? = null,
    val email: String,
    val createdAt: OffsetDateTime? = null,
    val updatedAt: OffsetDateTime? = null,
    val spotifyUserId: UUID? = null,
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
)


// For user registration/updates (without sensitive data)
data class CreateUserDto(
    val email: String,
    val password: String,
    val role: String = "USER",
    val spotifyUserId: UUID? = null,
    val isActive: Boolean = false
) {
    companion object {
        fun existingUser(user: User): CreateUserDto {
            return CreateUserDto(
                email = user.email,
                password = "",
                spotifyUserId = user.spotifyUserId,
                isActive = user.isActive,
                role = user.role.name
            )
        }

        fun registUser(profile: SpotifyProfileRes, spotifyUser: SpotifyUser): CreateUserDto {
            return CreateUserDto(
                email = profile.email,
                password = "",
                spotifyUserId = spotifyUser.id,
                isActive = true,
                role = spotifyUser.role.name
            )
        }
    }
}

data class UpdateUserDto(
    val email: String?,
    val role: String?
)

