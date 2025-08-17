package org.github.nanaki_93.gen_playlists.model

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.OffsetDateTime
import java.util.*

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class SpotifyPageRes<T>(
    val href: String,
    val limit: Int,
    val next: String?,
    val offset: Int,
    val previous: String?,
    val total: Int,
    val items: List<T>,
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class SpotifyExplicit(val filterEnabled: Boolean, val filterLocked: Boolean)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class SpotifyImage(val height: Int, val url: String, val width: Int)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
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

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class SpotifyOwnerRes(
    val id: String,
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class SpotifyPlaylistRes(
    val name: String,
    val description: String,
    val images: List<SpotifyImage>,
    val id: String,
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class SpotifyPlaylistDetailRes(
    val owner: SpotifyOwnerRes,
    val collaborative: Boolean,
    val name: String,
    val description: String,
    val apiHref: String,
    val images: List<SpotifyImage>,
    val tracks: SpotifyPageRes<SpotifyTrackRes>
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class SpotifyTrackRes(
    val name: String,
    val album: SpotifyAlbumRes,
    val artists: List<SpotifyArtistRes>,
    val explicit: Boolean,
    val externalUrls: Map<String, String>,
    val href: String,
    val id: String,
    val uri: String,
    val type: String,
    val isLocal: Boolean,
    val discNumber: Int,
    val trackNumber: Int,
    val durationMs: Int,
    val popularity: Int,
    val previewUrl: String? = null,
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class SpotifyAlbumRes(
    val id: UUID,
    val name: String,
    val albumType: String,
    val totalTracks: Int,
    val artists: List<SpotifyArtistRes>,
    val releaseDate: OffsetDateTime,
    val images: List<SpotifyImage>,
    val externalUrls: Map<String, String>,
    val href: String,
    val uri: String,
    val type: String,
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class SpotifyArtistRes(
    val id: String,
    val name: String,
)

