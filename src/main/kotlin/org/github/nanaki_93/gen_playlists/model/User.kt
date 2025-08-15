package org.github.nanaki_93.gen_playlists.model

import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    val id: UUID? = null,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(name = "password_hash", nullable = false)
    val passwordHash: String,

    @Column(name = "created_at")
    var createdAt: OffsetDateTime? = null,

    @Column(name = "updated_at")
    var updatedAt: OffsetDateTime? = null,

    @Column(name = "spotify_id")
    val spotifyId: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: Role = Role.USER,

    ) {
    @PrePersist
    fun prePersist() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now()
        }
        updatedAt = createdAt
    }
}

enum class Role {
    USER, PREMIUM
}

@Entity
@Table(name = "spotify_users")
data class SpotifyUser(

    @Id
    @Column(name = "spotify_id", nullable = false, unique = true)
    val spotifyId: String,

    @Column(name = "access_token", nullable = false)
    val accessToken: String,

    @Column(name = "refresh_token", nullable = false, columnDefinition = "TEXT")
    val refreshToken: String,

    @Column(name = "token_expiration_time", nullable = false)
    val tokenExpirationTime: OffsetDateTime,

    @Column(nullable = false)
    val scope: String,

    @Column(name = "playlist_list", columnDefinition = "VARCHAR(255)[]")
    val playlistList: List<String> = emptyList(),

    @Column(name = "created_at")
    var createdAt: OffsetDateTime? = null,

    @Column(name = "updated_at")
    var updatedAt: OffsetDateTime? = null,

    ) {
    @PrePersist
    fun prePersist() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now()
        }
        updatedAt = createdAt
    }


}

@Repository
interface UserRepository : JpaRepository<User, UUID> {
    fun findByEmail(email: String): User?
}


@Repository
interface SpotifyUserRepository : JpaRepository<SpotifyUser, String>


