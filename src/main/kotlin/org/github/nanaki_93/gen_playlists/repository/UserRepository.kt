package org.github.nanaki_93.gen_playlists.repository

import org.github.nanaki_93.gen_playlists.model.User
import org.github.nanaki_93.gen_playlists.model.UserRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

interface UserRepository {
    fun findByEmail(email: String): User?
    fun findById(id: UUID): User?
    fun save(user: User): User
    fun deleteById(id: UUID): Boolean

}

@Repository
class JdbcUserRepository(private val jdbcTemplate: JdbcTemplate) : UserRepository {
    private val rowMapper = UserRowMapper

    override fun findByEmail(email: String): User? {
        val sql = "SELECT user_id, email, password_hash, role, created_at, updated_at FROM users WHERE email = ?"
        return try {
            jdbcTemplate.queryForObject(sql, rowMapper, email)
        } catch (e: org.springframework.dao.EmptyResultDataAccessException) {
            null
        }
    }

    override fun findById(id: UUID): User? {
        val sql = "SELECT user_id, email, password_hash, role, created_at, updated_at FROM users WHERE user_id = ?"
        return try {
            jdbcTemplate.queryForObject(sql, rowMapper, id)
        } catch (e: org.springframework.dao.EmptyResultDataAccessException) {
            null
        }
    }

    override fun save(user: User): User {
        val sql = if (user.id == null) {
            // Insert new user
            val newId = UUID.randomUUID()
            """
            INSERT INTO users (id, email, password_hash, role, created_at, updated_at) 
            VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
            """.trimIndent()
        } else {
            // Update existing user
            """
            UPDATE users SET email = ?, password_hash = ?, role = ?, updated_at = CURRENT_TIMESTAMP 
            WHERE id = ?
            """.trimIndent()
        }

        // Execute the SQL and return the saved user

        jdbcTemplate.update(sql, user.id, user.email, user.passwordHash, user.role.name, user.id)
        return user
    }

    override fun deleteById(id: UUID): Boolean {
        val sql = "DELETE FROM users WHERE user_id = ?"
        return jdbcTemplate.update(sql, id) > 0
    }
}
