package org.github.nanaki_93.gen_playlists.model

import jakarta.persistence.*
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

    @Column(name = "spotify_user_id")
    val spotifyUserId: UUID? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: Role = Role.USER,

    @Column(name = "is_active", nullable = false)
    val isActive: Boolean = true
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
    @GeneratedValue
    @Column(name = "spotify_user_id")
    val id: UUID? = null,

    @Column(name = "spotify_id", nullable = false, unique = true)
    val spotifyId: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: Role = Role.USER,

    @Column(name = "access_token", nullable = false)
    val accessToken: String,

    @Column(name = "refresh_token", nullable = false, columnDefinition = "TEXT")
    val refreshToken: String,

    @Column(name = "token_expiration_time", nullable = false)
    val tokenExpirationTime: OffsetDateTime,

    @Column(nullable = false)
    val scope: String,

    @Column(name = "profile_image_url", nullable = false)
    val profileImageUrl: String,

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


