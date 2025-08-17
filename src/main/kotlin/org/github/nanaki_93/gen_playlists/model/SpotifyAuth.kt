package org.github.nanaki_93.gen_playlists.model

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class SpotifyAuthCode(val code: String)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class SpotifyAccessInfoRes(
    val accessToken: String,
    val expiresIn: Int,
    val refreshToken: String,
    val scope: String,
    val tokenType: String
)

