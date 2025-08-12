package org.github.nanaki_93.gen_playlists.service

import org.github.nanaki_93.gen_playlists.config.ApplicationProperties
import org.github.nanaki_93.gen_playlists.model.Role
import org.github.nanaki_93.gen_playlists.model.SpotifyAccessRes
import org.github.nanaki_93.gen_playlists.model.SpotifyAuthCode
import org.github.nanaki_93.gen_playlists.model.SpotifyProfileRes
import org.github.nanaki_93.gen_playlists.model.SpotifyUser
import org.github.nanaki_93.gen_playlists.model.User
import org.github.nanaki_93.gen_playlists.repository.SpotifyUserRepository
import org.github.nanaki_93.gen_playlists.repository.UserRepository
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestClient
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID

@Service
class SpotifyAuthApi(
    val appProps: ApplicationProperties,
    val userRepository: UserRepository,
    val spotifyUserRepository: SpotifyUserRepository
) {

    private var expirationTime: OffsetDateTime? = null

    private val accountClient = RestClient.builder()
        .baseUrl("https://accounts.spotify.com")
        .build()

    private val apiClient = RestClient.builder()
        .baseUrl("https://api.spotify.com")
        .build()

    private fun isAccessTokenExpired(spotifyUser: SpotifyUser): Boolean {
        if (expirationTime == null) {
            expirationTime = spotifyUserRepository.findBySpotifyId(spotifyUser.spotifyId)?.tokenExpirationTime
        }
        return OffsetDateTime.now().isAfter(expirationTime)
    }


    private fun getAccessToken(form: MultiValueMap<String, String>) = accountClient.post()
        .uri("/api/token")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .accept(MediaType.APPLICATION_JSON)
        .body(form)
        .retrieve()
        .body(SpotifyAccessRes::class.java)
        ?: error("Empty response from Spotify")


    private fun getSpotifyTokens(spotifyAuthCode: SpotifyAuthCode): SpotifyAccessRes {
        val form: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>().apply {
            add("client_id", appProps.clientId)
            add("client_secret", appProps.clientSecret)
            add("grant_type", "authorization_code")
            add("code", spotifyAuthCode.code)
            add("redirect_uri", "http://localhost:3000")
        }

        return getAccessToken(form)
    }

    private fun refreshAccessToken(spotifyUser: SpotifyUser): SpotifyAccessRes {
        val form: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>().apply {
            add("grant_type", "refresh_token")
            add("client_id", appProps.clientId)
            add("refresh_token", spotifyUser.refreshToken)
        }
        return getAccessToken(form)
    }


    private fun retrieveSpotifyProfile(accessToken: String) = apiClient.get()
        .uri("/v1/me")
        .accept(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer $accessToken")
        .retrieve()
        .body(SpotifyProfileRes::class.java)
        ?: error("Empty response from Spotify")



    public fun loginOrRegister(spotifyAuthCode: SpotifyAuthCode): User? {
        val spotifyAccessRes = getSpotifyTokens(spotifyAuthCode)
        val spotifyProfile = retrieveSpotifyProfile(spotifyAccessRes.accessToken)

        val spotifyUser = toSpotifyUser(spotifyAccessRes,spotifyProfile)
        //todo check user exists or not.
        return userRepository.findByEmail(spotifyProfile.email)

    }
    private fun toSpotifyUser(spotifyAccessRes: SpotifyAccessRes,
                             spotifyProfile: SpotifyProfileRes): SpotifyUser {
        //todo Register and Login with different logics
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