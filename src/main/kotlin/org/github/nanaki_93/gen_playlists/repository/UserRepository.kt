package org.github.nanaki_93.gen_playlists.repository

import org.github.nanaki_93.gen_playlists.model.SpotifyUser
import org.github.nanaki_93.gen_playlists.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface UserRepository : JpaRepository<User, UUID> {
    fun findByEmail(email: String): User?
    fun findBySpotifyUserId(spotifyUserId: UUID): User?
}


@Repository
interface SpotifyUserRepository : JpaRepository<SpotifyUser, UUID> {
    fun findBySpotifyId(spotifyId: String): SpotifyUser?
}
