package org.github.nanaki_93.gen_playlists.service

import org.github.nanaki_93.gen_playlists.config.ApplicationProperties
import org.github.nanaki_93.gen_playlists.model.*
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestClient
import java.time.OffsetDateTime
import java.util.*

@Service
class SpotifyApi(val appProps: ApplicationProperties) {

    private val accountClient = RestClient.builder()
        .baseUrl("https://accounts.spotify.com")
        .build()

    private val apiClient = RestClient.builder()
        .baseUrl("https://api.spotify.com")
        .build()


    fun getUser(spotifyAuthCode: SpotifyAuthCode): SpotifyUser {
        val spotifyAccess = getAccess(spotifyAuthCode)
        return toUser(spotifyAccess)
    }

    private fun toUser(spotifyAccess: SpotifyAccess): SpotifyUser {

        println("spotifyAccess: $spotifyAccess")
        val spotifyUser = apiClient.get()
            .uri("/v1/me")
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer ${spotifyAccess.accessToken}")
            .retrieve()
            .body(String::class.java)
            ?: error("Empty response from Spotify")

        println("spotifyUser: $spotifyUser")
        return SpotifyUser(
            id = UUID.randomUUID(),
            user = User(
                id = UUID.randomUUID(),
                email = "mail",
                passwordHash = "",
                role = Role.USER,
                createdAt = OffsetDateTime.now(),
                updatedAt = OffsetDateTime.now()
            ),
            spotifyId = "test",
            accessToken = "aaa",
            refreshToken = "aaa",
            tokenExpirationTime = OffsetDateTime.now().plusHours(2),
            scope = "test",
            createdAt = OffsetDateTime.now(),
            updatedAt = OffsetDateTime.now(),
        )

    }

    private fun getAccess(spotifyAuthCode: SpotifyAuthCode): SpotifyAccess {
        val form: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>().apply {
            add("client_id", appProps.clientId)
            add("client_secret", appProps.clientSecret)
            add("grant_type", "authorization_code")
            add("code", spotifyAuthCode.code)
            add("redirect_uri", "http://localhost:3000")
        }

        return accountClient.post()
            .uri("/api/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .accept(MediaType.APPLICATION_JSON)
            .body(form)
            .retrieve()
            .body(SpotifyAccess::class.java)
            ?: error("Empty response from Spotify")
    }


}