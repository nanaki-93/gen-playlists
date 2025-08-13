package org.github.nanaki_93.gen_playlists.service

import org.github.nanaki_93.gen_playlists.model.SpotifyPageRes
import org.github.nanaki_93.gen_playlists.model.SpotifyPlaylistRes
import org.github.nanaki_93.gen_playlists.model.SpotifyUser
import org.github.nanaki_93.gen_playlists.repository.SpotifyUserRepository
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.web.client.RestClient

class SpotifyApi(private val spotifyAuthApi: SpotifyAuthApi, private val spotifyUserRepository: SpotifyUserRepository) {

    private val apiClient = RestClient.builder()
        .baseUrl("https://api.spotify.com")
        .build()

    private fun retrieveSpotifyPlaylist(spotifyUser: SpotifyUser): SpotifyPageRes<SpotifyPlaylistRes> {
        return spotifyAuthApi.withRefreshToken(spotifyUser) { currentUser ->
            apiClient.get()
                .uri("/v1/users/${currentUser.spotifyId}/playlists")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer ${currentUser.accessToken}")
                .retrieve()
                .body(object : ParameterizedTypeReference<SpotifyPageRes<SpotifyPlaylistRes>>() {})
                ?: error("Empty response from Spotify")
        }
    }

    private fun retrieveSpotifyPlaylistDetail(spotifyUser: SpotifyUser): SpotifyPageRes<SpotifyPlaylistRes> {
        return spotifyAuthApi.withRefreshToken(spotifyUser) { currentUser ->
            apiClient.get()
                .uri("/v1/users/${currentUser.spotifyId}/playlists")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer ${currentUser.accessToken}")
                .retrieve()
                .body(object : ParameterizedTypeReference<SpotifyPageRes<SpotifyPlaylistRes>>() {})
                ?: error("Empty response from Spotify")
        }
    }


}