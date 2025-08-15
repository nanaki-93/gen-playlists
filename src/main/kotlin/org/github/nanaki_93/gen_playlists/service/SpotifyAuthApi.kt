package org.github.nanaki_93.gen_playlists.service

import org.github.nanaki_93.gen_playlists.config.ApplicationProperties
import org.github.nanaki_93.gen_playlists.dto.CreateUserDto
import org.github.nanaki_93.gen_playlists.mapper.UserMapper
import org.github.nanaki_93.gen_playlists.model.*
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap

@Service
class SpotifyAuthApi(
    val appProps: ApplicationProperties,
    val userRepository: UserRepository,
    val spotifyUserRepository: SpotifyUserRepository,
    val userMapper: UserMapper
) : BaseService(appProps, spotifyUserRepository) {

    private fun retrieveSpotifyTokens(spotifyAuthCode: SpotifyAuthCode) =
        getSpotifyAccessInfoRes(LinkedMultiValueMap<String, String>().apply {
            add("client_id", appProps.clientId)
            add("client_secret", appProps.clientSecret)
            add("grant_type", "authorization_code")
            add("code", spotifyAuthCode.code)
            add("redirect_uri", "http://localhost:3000")
        })


    private fun retrieveFirstSpotifyProfile(accessToken: String) = callGet(
        baseUrl = API_BASE_URL,
        uri = "/v1/me",
        accessToken = accessToken,
        resType = object : ParameterizedTypeReference<SpotifyProfileRes>() {})


    private fun retrieveSpotifyProfile() = withRefreshToken { currentUser ->
        callGet(
            baseUrl = API_BASE_URL,
            uri = "/v1/me",
            accessToken = currentUser.accessToken,
            resType = object : ParameterizedTypeReference<SpotifyProfileRes>() {})

    }


    fun loginOrSignup(spotifyAuthCode: SpotifyAuthCode): CreateUserDto {
        return run {
            val accessTokenResponse = retrieveSpotifyTokens(spotifyAuthCode)
            val profileResponse = retrieveFirstSpotifyProfile(accessTokenResponse.accessToken)
            val spotifyUser = saveSpotifyUser(accessTokenResponse, profileResponse)

            userRepository.findByEmail(profileResponse.email)
                ?.let { userMapper.toCreateDto(it.email, it.spotifyId!!, false) } ?: userMapper.toCreateDto(
                profileResponse.email,
                spotifyUser.spotifyId,
                true
            )
        }
    }

    private fun saveSpotifyUser(
        spotifyAccessInfoRes: SpotifyAccessInfoRes, spotifyProfile: SpotifyProfileRes
    ) = spotifyUserRepository.save(userMapper.toSpotifyUser(spotifyAccessInfoRes, spotifyProfile))


}