package org.github.nanaki_93.gen_playlists.mapper

import org.github.nanaki_93.gen_playlists.dto.CreateUserDto
import org.github.nanaki_93.gen_playlists.dto.SpotifyUserDto
import org.github.nanaki_93.gen_playlists.dto.UpdateUserDto
import org.github.nanaki_93.gen_playlists.dto.UserDto
import org.github.nanaki_93.gen_playlists.model.*
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import java.util.*

@Component
class UserMapper(
    private val passwordEncoder: PasswordEncoder? = null
) {

    // Convert User entity to UserDto
    fun toDto(user: User): UserDto {
        return UserDto(
            id = user.id,
            email = user.email,
            role = user.role.name,
            isActive = user.isActive,
            createdAt = user.createdAt,
            spotifyUserId = user.spotifyUserId
        )
    }

    // Convert SpotifyUser entity to SpotifyUserDto (without sensitive tokens)
    fun toDto(spotifyUser: SpotifyUser): SpotifyUserDto {
        return SpotifyUserDto(
            id = spotifyUser.id,
            spotifyId = spotifyUser.spotifyId,
            role = spotifyUser.role.name,
            profileImageUrl = spotifyUser.profileImageUrl,
            scope = spotifyUser.scope,
            createdAt = spotifyUser.createdAt
        )
    }

    // Convert CreateUserDto to User entity
    fun toEntity(createUserDto: CreateUserDto): User {
        return User(
            email = createUserDto.email,
            passwordHash = passwordEncoder?.encode(createUserDto.password)
                ?: throw IllegalStateException("Password encoder not available"),
            role = Role.valueOf(createUserDto.role.uppercase()),
            isActive = createUserDto.isActive,
            spotifyUserId = createUserDto.spotifyUserId
        )
    }

    // Update existing User entity with UpdateUserDto
    fun updateEntity(user: User, updateUserDto: UpdateUserDto): User {
        return user.copy(
            email = updateUserDto.email ?: user.email,
            role = updateUserDto.role?.let { Role.valueOf(it.uppercase()) } ?: user.role,
            updatedAt = java.time.OffsetDateTime.now()
        )
    }

    fun toSpotifyUser(
        spotifyAccessInfoRes: SpotifyAccessInfoRes,
        spotifyProfileRes: SpotifyProfileRes,
        id: UUID? = null
    ) = SpotifyUser(
        id = id,
        spotifyId = spotifyProfileRes.id,
        role = if (spotifyProfileRes.product == "premium") Role.PREMIUM else Role.USER,
        accessToken = spotifyAccessInfoRes.accessToken,
        refreshToken = spotifyAccessInfoRes.refreshToken,
        tokenExpirationTime = OffsetDateTime.now()
            .plus(spotifyAccessInfoRes.expiresIn.toLong(), ChronoUnit.SECONDS), // check unit and logic
        scope = spotifyAccessInfoRes.scope,
        profileImageUrl = spotifyProfileRes.images.first { it.height > 100 }.url, //todo check for largest image
    )

//    // Convert list of entities to DTOs
//    fun toDtoList(users: List<User>): List<UserDto> {
//        return users.map { toDto(it) }
//    }
//
//    fun toDtoList(spotifyUsers: List<SpotifyUser>): List<SpotifyUserDto> {
//        return spotifyUsers.map { toDto(it) }
//    }
}