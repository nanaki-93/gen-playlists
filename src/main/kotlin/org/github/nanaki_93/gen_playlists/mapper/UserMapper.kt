package org.github.nanaki_93.gen_playlists.mapper

import org.github.nanaki_93.gen_playlists.dto.CreateUserDto
import org.github.nanaki_93.gen_playlists.dto.SpotifyUserDto
import org.github.nanaki_93.gen_playlists.dto.UpdateUserDto
import org.github.nanaki_93.gen_playlists.dto.UserDto
import org.github.nanaki_93.gen_playlists.model.Role
import org.github.nanaki_93.gen_playlists.model.SpotifyUser
import org.github.nanaki_93.gen_playlists.model.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

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
            spotifyUser = user.spotifyUser?.let { toDto(it) }
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
            role = Role.valueOf(createUserDto.role.uppercase())
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

//    // Convert list of entities to DTOs
//    fun toDtoList(users: List<User>): List<UserDto> {
//        return users.map { toDto(it) }
//    }
//
//    fun toDtoList(spotifyUsers: List<SpotifyUser>): List<SpotifyUserDto> {
//        return spotifyUsers.map { toDto(it) }
//    }
}