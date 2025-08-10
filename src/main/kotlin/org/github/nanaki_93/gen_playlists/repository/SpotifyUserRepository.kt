package org.github.nanaki_93.gen_playlists.repository

import org.github.nanaki_93.gen_playlists.model.SpotifyUser
import org.github.nanaki_93.gen_playlists.model.SpotifyUserRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*


interface SpotifyUserRepository {
    fun findBySpotifyId(spotifyId: String): SpotifyUser?
    fun findById(id: UUID): SpotifyUser?
    fun save(spotifyUser: SpotifyUser): SpotifyUser
    fun deleteById(id: UUID): Boolean
}

@Repository
class JdbcSpotifyUserRepository(private val jdbcTemplate: JdbcTemplate) : SpotifyUserRepository {
    private val rowMapper = SpotifyUserRowMapper

    override fun findBySpotifyId(spotifyId: String): SpotifyUser? {
        val sql = """
            SELECT spotify_user_id, spotify_id, role, profile_image_url, access_token, 
                   refresh_token, token_expiration_time, scope, created_at, updated_at 
            FROM spotify_users WHERE spotify_id = ?
        """.trimIndent()
        return try {
            jdbcTemplate.queryForObject(sql, rowMapper, spotifyId)
        } catch (e: org.springframework.dao.EmptyResultDataAccessException) {
            null
        }
    }

    override fun findById(id: UUID): SpotifyUser? {
        val sql = """
            SELECT spotify_user_id, spotify_id, role, profile_image_url, access_token, 
                   refresh_token, token_expiration_time, scope, created_at, updated_at 
            FROM spotify_users WHERE spotify_user_id = ?
        """.trimIndent()
        return try {
            jdbcTemplate.queryForObject(sql, rowMapper, id)
        } catch (e: org.springframework.dao.EmptyResultDataAccessException) {
            null
        }
    }

    override fun save(spotifyUser: SpotifyUser): SpotifyUser {
        return if (spotifyUser.id == null) {
            // Insert new spotify user
            val newId = UUID.randomUUID()
            val sql = """
                INSERT INTO spotify_users (spotify_user_id, spotify_id, role, profile_image_url, 
                                         access_token, refresh_token, token_expiration_time, scope, 
                                         created_at, updated_at) 
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
            """.trimIndent()

            jdbcTemplate.update(
                sql, newId, spotifyUser.spotifyId, spotifyUser.role.name,
                "", spotifyUser.accessToken, spotifyUser.refreshToken,
                spotifyUser.tokenExpirationTime, spotifyUser.scope
            )
            spotifyUser.copy(id = newId)
        } else {
            // Update existing spotify user
            val sql = """
                UPDATE spotify_users SET spotify_id = ?, role = ?, access_token = ?, 
                                       refresh_token = ?, token_expiration_time = ?, 
                                       scope = ?, updated_at = CURRENT_TIMESTAMP 
                WHERE spotify_user_id = ?
            """.trimIndent()

            jdbcTemplate.update(
                sql, spotifyUser.spotifyId, spotifyUser.role.name,
                spotifyUser.accessToken, spotifyUser.refreshToken,
                spotifyUser.tokenExpirationTime, spotifyUser.scope, spotifyUser.id
            )
            spotifyUser
        }
    }

    override fun deleteById(id: UUID): Boolean {
        val sql = "DELETE FROM spotify_users WHERE spotify_user_id = ?"
        return jdbcTemplate.update(sql, id) > 0
    }
}