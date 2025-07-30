package org.github.nanaki_93.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpotifyAccess(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String,
    @SerialName("expires_in") val expiresIn: Int,
    @SerialName("scope") val scope: String? = null,
    @SerialName("refresh_token") val refreshToken: String? = null,
)


@Serializable
data class SpotifyProfile(
    val country: String,
    @SerialName("display_name") val displayName: String,
    @SerialName("explicit_content") val explicitContent: ExplicitContent,
    val email: String,
    val href: String,
    val images: List<Image>,
    val id: String,
    val product: String,
    val type: String,
    val uri: String,
    @SerialName("external_urls") val externalUrls: ExternalUrls,
    val followers: Followers

)

@Serializable
data class ExternalUrls(val spotify: String)

@Serializable
data class Followers(val href: String?, val total: Int)

@Serializable
data class Image(val url: String, val height: Int, val width: Int)

@Serializable
data class ExplicitContent(
    @SerialName("filter_enabled") val filterEnabled: Boolean, @SerialName("filter_locked") val filterLocked: Boolean
)
