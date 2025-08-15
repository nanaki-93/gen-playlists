package org.github.nanaki_93.gen_playlists.service

import org.github.nanaki_93.gen_playlists.config.ApplicationProperties
import org.github.nanaki_93.gen_playlists.model.SpotifyAccessInfoRes
import org.github.nanaki_93.gen_playlists.model.SpotifyUser
import org.github.nanaki_93.gen_playlists.model.SpotifyUserRepository
import org.github.nanaki_93.gen_playlists.model.User
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestClient
import java.time.OffsetDateTime

@Service
abstract class BaseService(
    private val appProps: ApplicationProperties,
    private val spotifyUserRepository: SpotifyUserRepository,
) {

    companion object {
        const val ACCOUNT_BASE_URL = "https://accounts.spotify.com"
        const val API_BASE_URL = "https://api.spotify.com"
    }


    private fun getCurrentUser(): User = (SecurityContextHolder.getContext().authentication
        ?: throw IllegalStateException("No authenticated user found")).principal as User

    private fun getCurrentSpotifyId(): String =
        getCurrentUser().spotifyId ?: throw IllegalStateException("User does not have a linked Spotify account")

    private fun isAccessTokenExpired(spotifyUser: SpotifyUser) =
        OffsetDateTime.now().isAfter(spotifyUser.tokenExpirationTime)

    fun <T> callGet(
        baseUrl: String, uri: String, accessToken: String, resType: ParameterizedTypeReference<T>
    ): T {
        return RestClient.builder().baseUrl(baseUrl).build().get()
            .uri(uri)
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer $accessToken")
            .retrieve()
            .body(resType)
            ?: error("Empty response from Spotify")
    }

    fun <T> callPost(
        baseUrl: String, uri: String, form: MultiValueMap<String, String>, resType: Class<T>
    ): T {
        return RestClient.builder().baseUrl(baseUrl).build().post()
            .uri(uri)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .accept(MediaType.APPLICATION_JSON)
            .body(form)
            .retrieve()
            .body(resType)
            ?: error("Empty response from Spotify")
    }

    fun getSpotifyAccessInfoRes(form: MultiValueMap<String, String>) =
        callPost(
            baseUrl = ACCOUNT_BASE_URL,
            uri = "/api/token",
            form = form,
            resType = SpotifyAccessInfoRes::class.java
        )

    private fun refreshAccessToken(spotifyUser: SpotifyUser) =
        getSpotifyAccessInfoRes(LinkedMultiValueMap<String, String>().apply {
            add("grant_type", "refresh_token")
            add("client_id", appProps.clientId)
            add("refresh_token", spotifyUser.refreshToken)
        })

    fun <T> withRefreshToken(operation: (SpotifyUser) -> T): T =
        spotifyUserRepository.findById(getCurrentSpotifyId())
            .orElseThrow { IllegalStateException("User not found") }
            .let { user -> if (isAccessTokenExpired(user)) refreshAndSaveUser(user) else user }
            .let(operation)

    private fun refreshAndSaveUser(user: SpotifyUser): SpotifyUser =
        refreshAccessToken(user)
            .let { refreshedTokens ->
                spotifyUserRepository.save(
                    user.copy(
                        accessToken = refreshedTokens.accessToken,
                        refreshToken = refreshedTokens.refreshToken,
                        tokenExpirationTime = OffsetDateTime.now().plusSeconds(refreshedTokens.expiresIn.toLong()),
                        updatedAt = OffsetDateTime.now()
                    )
                )
            }
}