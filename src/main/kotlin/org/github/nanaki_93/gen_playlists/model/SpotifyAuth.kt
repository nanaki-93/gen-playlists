package org.github.nanaki_93.gen_playlists.model

data class SpotifyAuthCode(val code: String)


data class SpotifyAccessInfoRes(
    val accessToken: String,
    val expiresIn: Int,
    val refreshToken: String,
    val scope: String,
    val tokenType: String
)

