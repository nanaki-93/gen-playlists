package org.github.nanaki_93.gen_playlists.service

import org.github.nanaki_93.gen_playlists.model.Role
import org.github.nanaki_93.gen_playlists.model.SpotifyAccessRes
import org.github.nanaki_93.gen_playlists.model.SpotifyProfileRes
import org.github.nanaki_93.gen_playlists.model.SpotifyUser
import org.github.nanaki_93.gen_playlists.repository.SpotifyUserRepository
import org.springframework.http.MediaType
import org.springframework.web.client.RestClient
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID
import kotlin.text.get

class SpotifyApi(private val spotifyAuthApi: SpotifyAuthApi, private val spotifyUserRepository: SpotifyUserRepository) {

    private val apiClient = RestClient.builder()
        .baseUrl("https://api.spotify.com")
        .build()

    private fun retrieveSpotifyPlaylist(spotifyUser: SpotifyUser) = apiClient.get()
        .uri("/v1/users/${spotifyUser.spotifyId}/playlists")
        .accept(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer ${spotifyUser.accessToken}")
        .retrieve()
        .body(String::class.java)
        ?: error("Empty response from Spotify")




}