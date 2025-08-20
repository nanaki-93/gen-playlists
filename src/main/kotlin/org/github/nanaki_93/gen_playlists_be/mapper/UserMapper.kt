package org.github.nanaki_93.gen_playlists_be.mapper

import org.github.nanaki_93.gen_playlists_be.dto.CreateUserDto
import org.github.nanaki_93.gen_playlists_be.dto.SpotifyUserDto
import org.github.nanaki_93.gen_playlists_be.dto.UpdateUserDto
import org.github.nanaki_93.gen_playlists_be.dto.UserDto
import org.github.nanaki_93.gen_playlists_be.model.*
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

@Component
class UserMapper(
    private val passwordEncoder: PasswordEncoder
) {

    // Convert User entity to UserDto
    fun toDto(user: User): UserDto {
        return UserDto(
            id = user.id,
            email = user.email,
            role = user.role.name,
            createdAt = user.createdAt,
            spotifyId = user.spotifyId
        )
    }

    fun toCreateDto(email: String, spotifyId: String, toRegister: Boolean): CreateUserDto {
        return CreateUserDto(
            email = email,
            password = "",
            spotifyId = spotifyId,
            toRegister = toRegister
        )
    }

    // Convert SpotifyUser entity to SpotifyUserDto (without sensitive tokens)
    fun toDto(spotifyUser: SpotifyUser): SpotifyUserDto {
        return SpotifyUserDto(
            spotifyId = spotifyUser.spotifyId,
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
            role = Role.USER,
            spotifyId = createUserDto.spotifyId
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
    ) = SpotifyUser(
        spotifyId = spotifyProfileRes.id,
        accessToken = spotifyAccessInfoRes.accessToken,
        refreshToken = spotifyAccessInfoRes.refreshToken,
        tokenExpirationTime = OffsetDateTime.now()
            .plus(spotifyAccessInfoRes.expiresIn.toLong(), ChronoUnit.SECONDS), // check unit and logic
        scope = spotifyAccessInfoRes.scope,
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