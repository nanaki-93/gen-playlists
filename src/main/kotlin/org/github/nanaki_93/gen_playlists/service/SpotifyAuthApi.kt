package org.github.nanaki_93.gen_playlists.service

import org.github.nanaki_93.gen_playlists.config.ApplicationProperties
import org.github.nanaki_93.gen_playlists.dto.CreateUserDto
import org.github.nanaki_93.gen_playlists.mapper.UserMapper
import org.github.nanaki_93.gen_playlists.model.SpotifyAccessInfoRes
import org.github.nanaki_93.gen_playlists.model.SpotifyAuthCode
import org.github.nanaki_93.gen_playlists.model.SpotifyProfileRes
import org.github.nanaki_93.gen_playlists.model.SpotifyUser
import org.github.nanaki_93.gen_playlists.repository.SpotifyUserRepository
import org.github.nanaki_93.gen_playlists.repository.UserRepository
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestClient
import java.time.OffsetDateTime

@Service
class SpotifyAuthApi(
    val appProps: ApplicationProperties,
    val userRepository: UserRepository,
    val spotifyUserRepository: SpotifyUserRepository,
    val userMapper: UserMapper
) {

    private val accountClient = RestClient.builder().baseUrl("https://accounts.spotify.com").build()

    private val apiClient = RestClient.builder().baseUrl("https://api.spotify.com").build()

    private fun isAccessTokenExpired(spotifyUser: SpotifyUser) =
        OffsetDateTime.now().isAfter(spotifyUser.tokenExpirationTime)


    private fun getSpotifyAccessInfoRes(form: MultiValueMap<String, String>) =
        accountClient.post()
            .uri("/api/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .accept(MediaType.APPLICATION_JSON)
            .body(form)
            .retrieve()
            .body(SpotifyAccessInfoRes::class.java)
            ?: error("Empty response from Spotify")


    private fun retrieveSpotifyTokens(spotifyAuthCode: SpotifyAuthCode) =
        getSpotifyAccessInfoRes(LinkedMultiValueMap<String, String>().apply {
            add("client_id", appProps.clientId)
            add("client_secret", appProps.clientSecret)
            add("grant_type", "authorization_code")
            add("code", spotifyAuthCode.code)
            add("redirect_uri", "http://localhost:3000")
        })

    private fun refreshAccessToken(spotifyUser: SpotifyUser) =
        getSpotifyAccessInfoRes(LinkedMultiValueMap<String, String>().apply {
            add("grant_type", "refresh_token")
            add("client_id", appProps.clientId)
            add("refresh_token", spotifyUser.refreshToken)
        })


    private fun retrieveFirstSpotifyProfile(accessToken: String) =
        apiClient.get()
            .uri("/v1/me")
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer $accessToken")
            .retrieve()
            .body(SpotifyProfileRes::class.java)
            ?: error("Empty response from Spotify")

    private fun retrieveSpotifyProfile(spotifyUser: SpotifyUser) =
        withRefreshToken(spotifyUser) { currentUser ->
            apiClient.get()
                .uri("/v1/me")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer ${currentUser.accessToken}")
                .retrieve()
                .body(SpotifyProfileRes::class.java)
                ?: error("Empty response from Spotify")

        }


    fun loginOrSignup(spotifyAuthCode: SpotifyAuthCode): CreateUserDto {
        val accessTokenResponse = retrieveSpotifyTokens(spotifyAuthCode)
        val profileResponse = retrieveFirstSpotifyProfile(accessTokenResponse.accessToken)
        val spotifyUser = saveSpotifyUser(accessTokenResponse, profileResponse)

        val existingUser = userRepository.findByEmail(profileResponse.email)

        return if (existingUser != null) {
            CreateUserDto.existingUser(existingUser)
        } else {
            CreateUserDto.registUser(profileResponse, spotifyUser)
        }
    }


    private fun saveSpotifyUser(
        spotifyAccessInfoRes: SpotifyAccessInfoRes,
        spotifyProfile: SpotifyProfileRes
    ): SpotifyUser {
        val oldUser = spotifyUserRepository.findBySpotifyId(spotifyProfile.id)
        val spotifyUser = userMapper.toSpotifyUser(spotifyAccessInfoRes, spotifyProfile, oldUser?.id)
        return spotifyUserRepository.save(spotifyUser)
    }


    fun <T> withRefreshToken(spotifyUser: SpotifyUser, operation: (SpotifyUser) -> T): T {
        var currentUser = spotifyUser
        if (isAccessTokenExpired(spotifyUser)) {
            val refreshedTokens = refreshAccessToken(spotifyUser)

            // Save the updated user to database
            currentUser = spotifyUserRepository.save(
                spotifyUser.copy(
                    accessToken = refreshedTokens.accessToken,
                    refreshToken = refreshedTokens.refreshToken,
                    tokenExpirationTime = OffsetDateTime.now().plusSeconds(refreshedTokens.expiresIn.toLong()),
                    updatedAt = OffsetDateTime.now()
                )
            )
        }
        return operation(currentUser)
    }

}