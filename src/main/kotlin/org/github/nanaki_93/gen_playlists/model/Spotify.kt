package org.github.nanaki_93.gen_playlists.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

data class SpotifyAuthCode(val code: String)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class SpotifyAccessInfoRes(
    val accessToken: String,
    val expiresIn: Int,
    val refreshToken: String,
    val scope: String,
    val tokenType: String
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class SpotifyExplicit(val filterEnabled: Boolean, val filterLocked: Boolean)

data class SpotifyImage(val height: Int, val url: String, val width: Int)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@JsonIgnoreProperties(ignoreUnknown = true)
data class SpotifyProfileRes(
    val id: String,
    val displayName: String,
    val country: String,
    val email: String,
    val images: List<SpotifyImage>,
    val product: String,
    val type: String,
    val explicitContent: SpotifyExplicit
)


