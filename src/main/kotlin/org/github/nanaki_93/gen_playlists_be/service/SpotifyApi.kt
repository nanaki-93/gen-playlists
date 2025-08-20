package org.github.nanaki_93.gen_playlists_be.service

import org.github.nanaki_93.gen_playlists_be.config.ApplicationProperties
import org.github.nanaki_93.gen_playlists_be.model.SpotifyPageRes
import org.github.nanaki_93.gen_playlists_be.model.SpotifyPlaylistDetailRes
import org.github.nanaki_93.gen_playlists_be.model.SpotifyPlaylistRes
import org.github.nanaki_93.gen_playlists_be.model.SpotifyUserRepository
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap


@Service
class SpotifyApi(
    appProps: ApplicationProperties,
    spotifyUserRepository: SpotifyUserRepository
) : BaseService(appProps, spotifyUserRepository) {


    fun retrieveSpotifyPlaylistList(limit: String, offset: String): SpotifyPageRes<SpotifyPlaylistRes> =
        withRefreshToken { currentUser ->
            val callGet = callGet(
                baseUrl = API_BASE_URL,
                uri = "/v1/me/playlists",
                params = LinkedMultiValueMap<String, String>().apply {
                    add("limit", limit)
                    add("offset", offset)
                },
                accessToken = currentUser.accessToken,
                resType = object : ParameterizedTypeReference<SpotifyPageRes<SpotifyPlaylistRes>>() {}
            )
            println("to Spotify:$callGet")
            callGet
        }

    fun retrieveSpotifyPlaylistDetail(playlistId: String): SpotifyPlaylistDetailRes =
        withRefreshToken { currentUser ->
            callGet(
                baseUrl = API_BASE_URL,
                uri = "v1/playlists/${playlistId}",
                accessToken = currentUser.accessToken,
                resType = object : ParameterizedTypeReference<SpotifyPlaylistDetailRes>() {}
            )
        }
}