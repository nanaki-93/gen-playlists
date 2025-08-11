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

    private fun retrieveSpotifyProfile(accessToken: String) = apiClient.get()
        .uri("/v1/me")
        .accept(MediaType.APPLICATION_JSON)
        .header("Authorization", spotifyAuthApi.)
        .retrieve()
        .body(SpotifyProfileRes::class.java)
        ?: error("Empty response from Spotify")

    private fun retrieveSpotifyPlaylist(spotifyUser: SpotifyUser) = apiClient.get()
        .uri("/v1/users/${spotifyUser.spotifyId}/playlists")
        .accept(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer ${spotifyUser.accessToken}")
        .retrieve()
        .body(String::class.java)
        ?: error("Empty response from Spotify")


    private fun toSpotifyUser(spotifyAccessRes: SpotifyAccessRes): SpotifyUser {
        //todo Register and Login with different logics
        val spotifyProfile = retrieveSpotifyProfile(spotifyAccessRes.accessToken)
        val oldUser = spotifyUserRepository.findBySpotifyId(spotifyProfile.id)
        val spotifyUser = mapToSpotifyUser(spotifyAccessRes, spotifyProfile, oldUser?.id)

        spotifyUserRepository.save(spotifyUser)
        return spotifyUser
    }

    private fun mapToSpotifyUser(
        spotifyAccessRes: SpotifyAccessRes,
        spotifyProfileRes: SpotifyProfileRes,
        id: UUID? = null
    ) = SpotifyUser(
        id = id,
        spotifyId = spotifyProfileRes.id,
        role = if (spotifyProfileRes.product == "premium") Role.PREMIUM else Role.USER,
        accessToken = spotifyAccessRes.accessToken,
        refreshToken = spotifyAccessRes.refreshToken,
        tokenExpirationTime = OffsetDateTime.now()
            .plus(spotifyAccessRes.expiresIn.toLong(), ChronoUnit.SECONDS), // check unit and logic
        scope = spotifyAccessRes.scope,
        profileImageUrl = spotifyProfileRes.images.first { it.height > 100 }.url, //todo check for largest image
    )


}